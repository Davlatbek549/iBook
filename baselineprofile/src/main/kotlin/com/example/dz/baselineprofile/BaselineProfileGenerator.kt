package com.example.dz.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Records which code the app runs on its way to its first screens, so release builds ship that
 * code already compiled rather than interpreting it on every cold start until the runtime gets
 * round to compiling it. Compose is especially slow to start interpreted.
 *
 * Regenerate with `./gradlew :androidApp:generateReleaseBaselineProfile` with an emulator or
 * device running Android 13 or later connected; the result is written under
 * `androidApp/src/release/generated/baselineProfiles/` and committed with the code.
 *
 * The journey stops at the auth screens: everything past them needs an account, and a generator
 * holding real credentials would be worse than a profile that covers startup and first impressions.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() = rule.collect(packageName = "com.example.dz", includeInStartupProfile = true) {
        pressHome()
        startActivityAndWait()

        // A first run lands on onboarding, after the splash; later passes of this block reuse the
        // install and land past it. Page through when it is there, so the pager and its page
        // entrances — the first thing a new reader sees — are in the profile too.
        if (device.wait(Until.hasObject(By.text("Next")), ONBOARDING_WAIT_MILLIS)) {
            repeat(ONBOARDING_PAGES - 1) {
                device.findObject(By.text("Next"))?.click()
                device.waitForIdle()
            }
            device.wait(Until.findObject(By.text("Start")), ONBOARDING_WAIT_MILLIS)?.click()
            device.waitForIdle()
        }
    }

    private companion object {
        const val ONBOARDING_PAGES = 3
        const val ONBOARDING_WAIT_MILLIS = 4_000L
    }
}
