package com.example.dz.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.auth.LogoutUseCase
import com.example.dz.domain.usecase.goal.GetReadingGoalUseCase
import com.example.dz.domain.usecase.social.GetFriendsUseCase
import com.example.dz.domain.usecase.user.GetProfileUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfile: GetProfileUseCase,
    private val getReadingGoal: GetReadingGoalUseCase,
    private val getFriends: GetFriendsUseCase,
    private val logout: LogoutUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ProfileEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.SettingsClicked -> emitEffect(ProfileEffect.NavigateToSettings)
            ProfileEvent.FriendsClicked -> emitEffect(ProfileEffect.NavigateToFriends)
            ProfileEvent.InviteClicked -> emitEffect(ProfileEffect.NavigateToInviteFriends)
            ProfileEvent.ReadingGoalClicked -> emitEffect(ProfileEffect.NavigateToReadingGoal)
            ProfileEvent.MembershipClicked -> emitEffect(ProfileEffect.NavigateToMembership)
            ProfileEvent.NotificationsClicked -> emitEffect(ProfileEffect.NavigateToNotifications)
            ProfileEvent.SignOutClicked -> signOut()
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Independent sources, fetched together rather than one after another — see Home's
            // ViewModel, which the same three calls are copied from.
            coroutineScope {
                val profileAsync = async { getProfile() }
                val goalAsync = async { getReadingGoal() }
                val friendsAsync = async { getFriends() }

                val profileResult = profileAsync.await()
                val goalResult = goalAsync.await()
                val friendsResult = friendsAsync.await()

                val profile = (profileResult as? AppResult.Success)?.data
                val friends = (friendsResult as? AppResult.Success)?.data.orEmpty()

                _uiState.update {
                    it.copy(
                        name = profile?.user?.name.orEmpty(),
                        booksOwned = profile?.booksRead ?: 0,
                        membershipLabel = profile?.membershipPlan?.title ?: "Free plan",
                        // A stale streak or a quiet friends row is not worth an error banner over
                        // an otherwise-loaded profile — only the identity fetch failing is.
                        readingGoal = (goalResult as? AppResult.Success)?.data,
                        friends = friends.take(2).map { it.toProfileFriendUi() },
                        isLoading = false,
                        errorMessage = (profileResult as? AppResult.Error)?.error?.toPresentationMessage()
                    )
                }
            }
        }
    }

    private fun signOut() {
        if (_uiState.value.isSigningOut) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSigningOut = true) }
            logout()
            emitEffect(ProfileEffect.NavigateToLogin)
        }
    }

    private fun emitEffect(effect: ProfileEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
