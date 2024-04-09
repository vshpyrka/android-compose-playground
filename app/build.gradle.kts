plugins {
    alias(pluginLibs.plugins.android.application)
    alias(pluginLibs.plugins.kotlin.android)
}

android {
    compileSdk = sdk.versions.compileSdk.get().toInt()

    namespace = "com.example.compose_playground"

    defaultConfig {
        applicationId = "com.example.compose_playground"
        minSdk = sdk.versions.minSdk.get().toInt()
        targetSdk = sdk.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            enableAndroidTestCoverage = true
            isPseudoLocalesEnabled = true
        }
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        animationsDisabled = true
        unitTests {
            isIncludeAndroidResources = true

            all { test ->
                with(test) {
                    testLogging {
                        events = setOf(
                            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR,
                        )
                    }
                }
            }
        }
    }

    packaging { resources.excludes.add("META-INF/versions/9/previous-compilation-data.bin") }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":compose"))
    implementation(libs.material)
}
