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
}

composeCompiler {
    enableStrongSkippingMode = true
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

    implementation(libs.coil)
    implementation(libs.coil.compose)

    // For AppWidgets support
    implementation(libs.glance.appwidget)
    // For interop APIs with Material 3
    implementation(libs.glance.material)

    // Android Studio Preview support

    implementation(libs.compose.tooling.preview)
    debugImplementation(libs.compose.tooling)

    // UI Tests
    androidTestImplementation(testLibs.compose.test.junit)
    debugImplementation(testLibs.compose.test.manifest)
}

