package com.example.dz.presentation.auth.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.auth.MailedCodeKind
import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.usecase.account.DiscardSignUpUseCase
import com.example.dz.domain.usecase.auth.LogoutUseCase
import com.example.dz.domain.usecase.auth.RequestPasswordResetUseCase
import com.example.dz.domain.usecase.auth.ResendVerificationCodeUseCase
import com.example.dz.domain.usecase.auth.VerifyEmailUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Code entry for both flows that have to prove someone reads a mailbox.
 *
 * Where a correct code leads is decided by [VerificationUiState.purpose], because the screen is
 * reached from sign-up and from a password reset and those end in different places.
 *
 * A sign-up code is spent here, against `/auth/verify`. A reset code is not: the server allows a
 * fixed number of guesses against a code, and the reset itself needs one of them, so checking
 * first would burn a guess to learn nothing the next call would not say — see [verify].
 */
class VerificationViewModel(
    email: String = "",
    purpose: VerificationPurpose = VerificationPurpose.VerifyEmail,
    /**
     * The sign-up form made this account a moment ago, so backing out takes it back — see
     * [discardAndLeave]. Never set for an account someone signed in to: that one existed before
     * this screen did, and may hold everything its owner has.
     */
    private val accountJustCreated: Boolean = false,
    private val verifyEmail: VerifyEmailUseCase,
    private val resendCode: ResendVerificationCodeUseCase,
    private val requestPasswordReset: RequestPasswordResetUseCase,
    private val logout: LogoutUseCase,
    private val discardSignUp: DiscardSignUpUseCase,
    private val local: LocalDataSource,
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerificationUiState(email = email, purpose = purpose))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<VerificationEffect>()
    val effects = _effects.asSharedFlow()

    private var timerJob: Job? = null

    init {
        val sinceLastSend = secondsSinceLastSend()
        if (purpose == VerificationPurpose.VerifyEmail && !codeIsLive(sinceLastSend)) {
            // Nothing live to type. The account may never have been sent a code at all — every
            // account made before verification existed was stored unverified, and signing in
            // with one lands here — or the last code has expired. The screen says a code is on
            // its way, so one has to be.
            sendCode()
        } else {
            // Picked up from when a code was last actually sent, not from now. Reached on a
            // relaunch the countdown may already have run out, and restarting it would make the
            // reader wait again before they could ask for the one they need.
            startResendTimer(remainingCooldownSeconds(sinceLastSend))
        }
    }

    fun onEvent(event: VerificationEvent) {
        when (event) {
            is VerificationEvent.CodeChanged -> onCodeChanged(event.code)
            VerificationEvent.VerifyClicked -> verify()
            VerificationEvent.ResendClicked -> resend()
            VerificationEvent.BackClicked ->
                if (accountJustCreated && _uiState.value.purpose == VerificationPurpose.VerifyEmail) {
                    discardAndLeave()
                } else {
                    emitEffect(VerificationEffect.NavigateBack)
                }
            VerificationEvent.AbandonSession -> abandonSession()
        }
    }

    private fun onCodeChanged(code: String) {
        // OrganicCodeField already filters to digits and caps the length; this guards the state
        // against any other caller.
        if (code.length <= VERIFICATION_CODE_LENGTH && code.all { it.isDigit() }) {
            _uiState.update { it.copy(code = code, errorMessage = null) }
        }
    }

    private fun verify() {
        val state = _uiState.value
        // Not while leaving: a code accepted mid-discard would open Home on an account that is
        // about to stop existing.
        if (state.isLoading || state.isLeaving || !state.isComplete) return

        when (state.purpose) {
            VerificationPurpose.VerifyEmail -> checkWithServer(state.email, state.code)
            // Carried rather than checked. `/auth/password/reset` spends the code as part of
            // setting the password, and a wrong one is reported there — one guess, one refusal.
            VerificationPurpose.ResetPassword ->
                emitEffect(VerificationEffect.NavigateToNewPassword(state.email, state.code))
        }
    }

    private fun checkWithServer(email: String, code: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = verifyEmail(email, code)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emitEffect(VerificationEffect.NavigateToHome)
                }
                is AppResult.Error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            // Cleared so a refused code can be retyped without deleting it first.
                            code = "",
                            errorMessage = result.error.toPresentationMessage(),
                        )
                    }
            }
        }
    }

    /**
     * The timer restarts on the way out rather than on the way back, so a refusal cannot leave the
     * button live and invite a second send the server would reject anyway.
     *
     * Which code gets sent follows the purpose. The two are separate on the server — a
     * confirmation code is no use against a password, and the reverse — so asking the wrong
     * endpoint would mail something that cannot finish the flow the reader is in.
     */
    private fun resend() {
        val state = _uiState.value
        if (!state.canResend || state.isLoading || state.isSendingCode || state.isLeaving) return
        sendCode()
    }

    /**
     * Mails a new code of this screen's kind. The countdown restarts on the way out whatever the
     * outcome, so a refusal cannot leave the link live and invite a send the server would refuse.
     */
    private fun sendCode() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSendingCode = true, errorMessage = null, code = "") }
            val result = when (state.purpose) {
                VerificationPurpose.VerifyEmail -> resendCode(state.email)
                VerificationPurpose.ResetPassword -> requestPasswordReset(state.email)
            }
            _uiState.update {
                it.copy(
                    isSendingCode = false,
                    errorMessage = (result as? AppResult.Error)?.error?.toPresentationMessage(),
                )
            }
            startResendTimer(VERIFICATION_RESEND_SECONDS)
        }
    }

    /**
     * Gives up the unverified session and leaves for sign-in.
     *
     * Reached only when there is nothing behind this screen, which is the state a relaunch on an
     * unverified session arrives in. Clearing the session is what makes leaving stick: keeping it
     * would land the very next launch back on this screen with no more way out than before.
     */
    private fun abandonSession() {
        if (_uiState.value.isLoading || _uiState.value.isLeaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLeaving = true, errorMessage = null) }
            logout()
            _uiState.update { it.copy(isLeaving = false) }
            emitEffect(VerificationEffect.NavigateToLogin)
        }
    }

    /**
     * Takes back the account the sign-up form just made, then returns to the form — still filled
     * in, so a mistyped address is one edit away from a sign-up that makes the only account,
     * rather than a second one beside an abandoned first.
     *
     * The wait is bounded by [DiscardSignUpUseCase]. A server that does not answer in time keeps
     * the account, unverified, but this device lets go of it all the same: the reader has walked
     * away from it, and a session kept for it would bring the next launch back to this screen.
     */
    private fun discardAndLeave() {
        if (_uiState.value.isLoading || _uiState.value.isLeaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLeaving = true, errorMessage = null) }
            if (!discardSignUp()) local.clearSession()
            // The code on its way went to the address being given up. Left on record, it would
            // have the next code screen, whichever account that is for, think one was live.
            local.removeSetting(MailedCodeKind.EmailVerification.lastSentSettingKey)
            _uiState.update { it.copy(isLeaving = false) }
            emitEffect(VerificationEffect.NavigateBack)
        }
    }

    /**
     * Seconds since this device last sent a code of this kind, or null when it never did or the
     * stamp is unreadable. A clock that has moved backwards since — a timezone fix, a manual
     * change — gives a negative age, and is treated as unknown rather than as a send in the future.
     */
    private fun secondsSinceLastSend(): Long? {
        val sentAt = local.getSetting(_uiState.value.purpose.mailedCodeKind.lastSentSettingKey)
            .toLongOrNull()
            ?: return null
        return ((currentEpochMillis() - sentAt) / 1000).takeIf { it >= 0 }
    }

    private fun codeIsLive(sinceLastSend: Long?): Boolean =
        sinceLastSend != null && sinceLastSend < VERIFICATION_CODE_LIFETIME_SECONDS

    /** What is left of the cooldown owed for the last code, or zero when none is owed. */
    private fun remainingCooldownSeconds(sinceLastSend: Long?): Int {
        if (sinceLastSend == null) return 0
        return (VERIFICATION_RESEND_SECONDS - sinceLastSend)
            .coerceIn(0, VERIFICATION_RESEND_SECONDS.toLong())
            .toInt()
    }

    private fun startResendTimer(secondsLeft: Int) {
        timerJob?.cancel()
        _uiState.update { it.copy(secondsLeft = secondsLeft) }
        if (secondsLeft <= 0) return
        timerJob = viewModelScope.launch {
            while (_uiState.value.secondsLeft > 0) {
                delay(1000)
                _uiState.update { it.copy(secondsLeft = it.secondsLeft - 1) }
            }
        }
    }

    private fun emitEffect(effect: VerificationEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
