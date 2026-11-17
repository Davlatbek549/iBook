package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.BirthDate
import com.example.dz.domain.model.EditableProfile
import com.example.dz.domain.model.Gender
import com.example.dz.domain.model.PostalAddress
import com.example.dz.domain.model.UsernameAvailability
import com.example.dz.domain.model.UsernameProblem
import com.example.dz.domain.repository.ProfileRepository
import com.example.dz.domain.usecase.user.CheckUsernameUseCase
import com.example.dz.domain.usecase.user.GetEditableProfileUseCase
import com.example.dz.domain.usecase.user.SaveEditableProfileUseCase
import com.example.dz.presentation.profile.profile_edit.EditProfileDialog
import com.example.dz.presentation.profile.profile_edit.EditProfileEffect
import com.example.dz.presentation.profile.profile_edit.EditProfileEvent
import com.example.dz.presentation.profile.profile_edit.EditProfileSheet
import com.example.dz.presentation.profile.profile_edit.EditProfileViewModel
import com.example.dz.presentation.profile.profile_edit.ProfileField
import com.example.dz.presentation.profile.profile_edit.UsernameStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Edit Profile's rules: when Save is live, when the username is checked, what a save sends, and
 * when the screen asks before doing something it cannot take back.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class EditProfileViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest fun setUp() = Dispatchers.setMain(dispatcher)

    @AfterTest fun tearDown() = Dispatchers.resetMain()

    private val amelia = EditableProfile(
        name = "Amelia Hart",
        username = "amelia.reads",
        email = "amelia@hart.co",
        emailVerified = true,
        bio = "Slow mornings",
        address = PostalAddress(countryCode = "GB", city = "London"),
        language = "en-GB",
        currentGoalMinutes = 20,
    )

    private class FakeProfileRepository(var profile: EditableProfile) : ProfileRepository {
        val saved = mutableListOf<EditableProfile>()
        val checked = mutableListOf<String>()
        var checkAnswer: (String) -> AppResult<UsernameAvailability> =
            { AppResult.Success(UsernameAvailability(it, available = true)) }
        var saveAnswer: (EditableProfile) -> AppResult<EditableProfile> = { AppResult.Success(it) }

        override suspend fun getEditableProfile(): AppResult<EditableProfile> = AppResult.Success(profile)

        override suspend fun saveEditableProfile(profile: EditableProfile): AppResult<EditableProfile> {
            saved += profile
            return saveAnswer(profile)
        }

        override suspend fun checkUsername(username: String): AppResult<UsernameAvailability> {
            checked += username
            return checkAnswer(username)
        }
    }

    private val today = BirthDate(2026, 11, 13)

    private fun TestScope.viewModel(
        repository: FakeProfileRepository,
        region: String? = "PL",
    ): Pair<EditProfileViewModel, MutableList<EditProfileEffect>> {
        val vm = EditProfileViewModel(
            GetEditableProfileUseCase(repository),
            SaveEditableProfileUseCase(repository),
            CheckUsernameUseCase(repository),
            today = { today },
            deviceRegion = { region },
        )
        val effects = mutableListOf<EditProfileEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.effects.collect { effects += it } }
        advanceUntilIdle()
        return vm to effects
    }

    @Test
    fun `nothing to save until something changes, and undoing a change undoes it`() = runTest(dispatcher) {
        val (vm, _) = viewModel(FakeProfileRepository(amelia))
        assertFalse(vm.uiState.value.canSave)

        vm.onEvent(EditProfileEvent.BioChanged("Slow mornings, long novels"))
        assertTrue(vm.uiState.value.canSave)
        assertEquals(listOf(ProfileField.Bio), vm.uiState.value.changedFields)

        // A trailing space would save the same, so it is not a change.
        vm.onEvent(EditProfileEvent.BioChanged("Slow mornings "))
        assertFalse(vm.uiState.value.hasChanges)
    }

    @Test
    fun `a username is checked once, 400ms after typing stops`() = runTest(dispatcher) {
        val repository = FakeProfileRepository(amelia)
        val (vm, _) = viewModel(repository)

        listOf("ame", "amel", "ameli", "amelia").forEach {
            vm.onEvent(EditProfileEvent.UsernameChanged(it))
            advanceTimeBy(100)
        }
        advanceTimeBy(299)
        runCurrent()
        assertEquals(emptyList(), repository.checked)
        assertEquals(UsernameStatus.Checking, vm.uiState.value.usernameStatus)
        assertFalse(vm.uiState.value.canSave, "Save waits for the check")

        advanceTimeBy(2)
        runCurrent()
        assertEquals(listOf("amelia"), repository.checked)
        assertEquals(UsernameStatus.Available, vm.uiState.value.usernameStatus)
        assertTrue(vm.uiState.value.canSave)
    }

    @Test
    fun `a username is shown the way it will be saved`() = runTest(dispatcher) {
        val (vm, _) = viewModel(FakeProfileRepository(amelia))
        vm.onEvent(EditProfileEvent.UsernameChanged(" @Amelia Hart "))
        assertEquals("ameliahart", vm.uiState.value.draft.username)
    }

    @Test
    fun `a taken username offers the server's suggestions and blocks saving`() = runTest(dispatcher) {
        val repository = FakeProfileRepository(amelia).apply {
            checkAnswer = { name ->
                if (name == "amelia") {
                    AppResult.Success(
                        UsernameAvailability(name, available = false, problem = UsernameProblem.Taken,
                            suggestions = listOf("amelia.hart", "amelia_reads"))
                    )
                } else {
                    AppResult.Success(UsernameAvailability(name, available = true))
                }
            }
        }
        val (vm, _) = viewModel(repository)

        vm.onEvent(EditProfileEvent.UsernameChanged("amelia"))
        advanceUntilIdle()
        assertEquals(UsernameStatus.Taken(listOf("amelia.hart", "amelia_reads")), vm.uiState.value.usernameStatus)
        assertFalse(vm.uiState.value.canSave)

        vm.onEvent(EditProfileEvent.UsernameSuggestionPicked("amelia.hart"))
        advanceUntilIdle()
        assertEquals("amelia.hart", vm.uiState.value.draft.username)
        assertEquals(UsernameStatus.Available, vm.uiState.value.usernameStatus)
    }

    @Test
    fun `a malformed username is refused without asking the server`() = runTest(dispatcher) {
        val repository = FakeProfileRepository(amelia)
        val (vm, _) = viewModel(repository)

        vm.onEvent(EditProfileEvent.UsernameChanged("a!"))
        advanceUntilIdle()
        assertEquals(UsernameStatus.Invalid, vm.uiState.value.usernameStatus)
        assertEquals(emptyList(), repository.checked)
    }

    @Test
    fun `going back to the saved username needs no check`() = runTest(dispatcher) {
        val repository = FakeProfileRepository(amelia)
        val (vm, _) = viewModel(repository)

        vm.onEvent(EditProfileEvent.UsernameChanged("someone.else"))
        vm.onEvent(EditProfileEvent.UsernameChanged("amelia.reads"))
        advanceUntilIdle()
        assertEquals(UsernameStatus.Unchanged, vm.uiState.value.usernameStatus)
        assertEquals(emptyList(), repository.checked, "the check for someone.else was cancelled before it ran")
    }

    @Test
    fun `an empty username blocks saving`() = runTest(dispatcher) {
        val (vm, _) = viewModel(FakeProfileRepository(amelia))
        vm.onEvent(EditProfileEvent.BioChanged("Changed"))
        vm.onEvent(EditProfileEvent.UsernameChanged(""))
        assertEquals(UsernameStatus.Missing, vm.uiState.value.usernameStatus)
        assertFalse(vm.uiState.value.canSave)
    }

    @Test
    fun `save sends the whole profile, untouched fields included`() = runTest(dispatcher) {
        val repository = FakeProfileRepository(amelia)
        val (vm, effects) = viewModel(repository)

        vm.onEvent(EditProfileEvent.BioChanged("  New bio  "))
        vm.onEvent(EditProfileEvent.SaveClicked)
        advanceUntilIdle()

        val sent = repository.saved.single()
        assertEquals("New bio", sent.bio)
        // The server clears what is left out, so what this screen does not edit must still go.
        assertEquals("en-GB", sent.language)
        assertEquals(20, sent.currentGoalMinutes)
        assertEquals("amelia.reads", sent.username)
        assertEquals(PostalAddress(countryCode = "GB", city = "London"), sent.address)
        assertEquals(listOf<EditProfileEffect>(EditProfileEffect.Saved), effects)
        assertFalse(vm.uiState.value.hasChanges)
    }

    @Test
    fun `the first save of a date of birth asks first, and saves only once confirmed`() = runTest(dispatcher) {
        val repository = FakeProfileRepository(amelia)
        val (vm, effects) = viewModel(repository)

        vm.onEvent(EditProfileEvent.DateOfBirthClicked)
        assertEquals(EditProfileSheet.DateOfBirth, vm.uiState.value.sheet)
        vm.onEvent(EditProfileEvent.DateOfBirthPicked(BirthDate(1999, 3, 14)))
        assertNull(vm.uiState.value.sheet)

        vm.onEvent(EditProfileEvent.SaveClicked)
        advanceUntilIdle()
        val dialog = assertIs<EditProfileDialog.ConfirmOneTime>(vm.uiState.value.dialog)
        assertEquals(BirthDate(1999, 3, 14), dialog.pending.dateOfBirth)
        assertEquals(emptyList(), repository.saved)

        vm.onEvent(EditProfileEvent.OneTimeConfirmed)
        advanceUntilIdle()
        assertEquals(BirthDate(1999, 3, 14), repository.saved.single().dateOfBirth)
        assertEquals(listOf<EditProfileEffect>(EditProfileEffect.Saved), effects)
    }

    @Test
    fun `a date of birth and gender set before cannot be opened again`() = runTest(dispatcher) {
        val (vm, _) = viewModel(FakeProfileRepository(amelia.copy(dateOfBirth = BirthDate(1999, 3, 14), gender = Gender.Female)))

        assertTrue(vm.uiState.value.dateOfBirthLocked)
        vm.onEvent(EditProfileEvent.DateOfBirthClicked)
        vm.onEvent(EditProfileEvent.GenderClicked)
        assertNull(vm.uiState.value.sheet)
    }

    @Test
    fun `a date of birth after today is pulled back to today`() = runTest(dispatcher) {
        val (vm, _) = viewModel(FakeProfileRepository(amelia))
        vm.onEvent(EditProfileEvent.DateOfBirthClicked)
        vm.onEvent(EditProfileEvent.DateOfBirthPicked(BirthDate(2030, 1, 1)))
        assertEquals(today, vm.uiState.value.draft.dateOfBirth)
    }

    @Test
    fun `cancel asks before throwing edits away, and not otherwise`() = runTest(dispatcher) {
        val (vm, effects) = viewModel(FakeProfileRepository(amelia))

        vm.onEvent(EditProfileEvent.CancelClicked)
        advanceUntilIdle()
        assertEquals(listOf<EditProfileEffect>(EditProfileEffect.NavigateBack), effects)

        effects.clear()
        vm.onEvent(EditProfileEvent.NameChanged("Amelia B. Hart"))
        vm.onEvent(EditProfileEvent.CancelClicked)
        advanceUntilIdle()
        assertEquals(EditProfileDialog.Discard, vm.uiState.value.dialog)
        assertEquals(emptyList(), effects)

        vm.onEvent(EditProfileEvent.DiscardConfirmed)
        advanceUntilIdle()
        assertEquals(listOf<EditProfileEffect>(EditProfileEffect.NavigateBack), effects)
    }

    @Test
    fun `a username taken between the check and the save comes back as taken`() = runTest(dispatcher) {
        var takenNow = false
        val repository = FakeProfileRepository(amelia).apply {
            checkAnswer = { name ->
                AppResult.Success(
                    if (takenNow) UsernameAvailability(name, false, UsernameProblem.Taken, listOf("${name}_reads"))
                    else UsernameAvailability(name, true)
                )
            }
            saveAnswer = { AppResult.Error(AppError.Profile(AppError.ProfileReason.UsernameTaken)) }
        }
        val (vm, effects) = viewModel(repository)

        vm.onEvent(EditProfileEvent.UsernameChanged("newname"))
        advanceUntilIdle()
        assertEquals(UsernameStatus.Available, vm.uiState.value.usernameStatus)

        takenNow = true
        vm.onEvent(EditProfileEvent.SaveClicked)
        advanceUntilIdle()
        assertEquals(UsernameStatus.Taken(listOf("newname_reads")), vm.uiState.value.usernameStatus)
        assertNotNull(vm.uiState.value.saveError)
        assertEquals(emptyList(), effects)
    }

    @Test
    fun `the phone's dial code starts from the address, then the device`() = runTest(dispatcher) {
        val (withAddress, _) = viewModel(FakeProfileRepository(amelia), region = "PL")
        assertEquals("GB", withAddress.uiState.value.phoneCountry)

        val (withoutAddress, _) = viewModel(FakeProfileRepository(amelia.copy(address = PostalAddress())), region = "PL")
        assertEquals("PL", withoutAddress.uiState.value.phoneCountry)
    }
}
