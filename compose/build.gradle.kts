plugins {
    alias(pluginLibs.plugins.android.library)
    alias(pluginLibs.plugins.kotlin.android)
    alias(pluginLibs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.compose"
    compileSdk = sdk.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = sdk.versions.minSdk.get().toInt()
        targetSdk = sdk.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests {
            animationsDisabled = true
            isIncludeAndroidResources = true
        }
    }
}

composeCompiler {
//    stabilityConfigurationFile = project.rootDir.resolve("compose_compiler_config.conf")
    metricsDestination = layout.buildDirectory.dir("compose-metrics")
    reportsDestination = layout.buildDirectory.dir("compose-metrics")
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)

    implementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.bom)

    implementation(libs.compose.material3)
    implementation(libs.compose.material3.window.size)

    // Optional - Integration with activities
    implementation(libs.compose.activity)

    implementation(libs.compose.foundation)
    implementation(libs.compose.material.icons)
    implementation(libs.lifecycle.compose)
    implementation(libs.compose.navigation)

    implementation("androidx.graphics:graphics-core:1.0.2")
    implementation("androidx.input:input-motionprediction:1.0.0-beta05")

    implementation(libs.coil)
    implementation(libs.coil.compose)

    implementation(libs.compose.tooling.preview)

    debugImplementation(libs.compose.tooling)
    debugImplementation(testLibs.compose.test.manifest)

    // Unit Tests
    testImplementation(testLibs.android.junit.ktx)
    testImplementation(testLibs.compose.test.junit)
    // UI Tests
    androidTestImplementation(testLibs.compose.test.junit)
}

