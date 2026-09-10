import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidTest)
    alias(libs.plugins.baselineprofile)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

android {
    namespace = "com.example.dz.baselineprofile"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        // Generating a profile on a device that is not rooted needs Android 13 (API 33); 28 is
        // the floor the tooling runs on at all.
        minSdk = 28
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":androidApp"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// Generated on whatever device or emulator is connected, rather than a Gradle-managed one.
baselineProfile {
    useConnectedDevices = true
}

dependencies {
    implementation(libs.androidx.testExt.junit)
    // Carries androidx.test:runner, which is both the AndroidJUnitRunner named above and where
    // @LargeTest lives. The same set the Android Studio baseline profile template uses.
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}
