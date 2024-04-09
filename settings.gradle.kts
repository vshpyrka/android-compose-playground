pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()

        val gprLocalUser: String by settings
        val gprLocalKey: String by settings
        val gprUser = System.getenv("GITHUB_USER") ?: gprLocalUser
        val gprPassword = System.getenv("GITHUB_TOKEN") ?: gprLocalKey

        maven {
            name = "android-version-catalog"
            url = uri("https://maven.pkg.github.com/vshpyrka/android-version-catalog")
            credentials {
                username = gprUser
                password = gprPassword
            }
        }
    }

    versionCatalogs {
        create("pluginLibs") {
            from("com.vshpyrka.android:version-catalog-plugins:latest.release")
        }
        create("libs") {
            from("com.vshpyrka.android:version-catalog-libs:latest.release")
        }
        create("testLibs") {
            from("com.vshpyrka.android:version-catalog-test:latest.release")
        }
        create("sdk") {
            from("com.vshpyrka.android:version-catalog-sdk:latest.release")
        }
    }
}

rootProject.name = "android-compose-playround"
include(":app", ":compose")
