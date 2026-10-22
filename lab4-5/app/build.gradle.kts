plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization) // @Serializable osztályokhoz
}

android {
    namespace = "com.example.progr3ssapp"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.progr3ssapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // Splash screen
    implementation(libs.androidx.core.splashscreen)
    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose) // viewModel() Compose-ban
    implementation(libs.androidx.lifecycle.runtime.compose) // collectAsStateWithLifecycle()
    // Hálózat
    implementation(libs.kotlinx.serialization.json) // @Serializable, Json, JsonObject
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization) // a Retrofit így (de)szerializál JSON-t
    implementation(libs.okhttp.logging.interceptor) // kérések/válaszok naplózása a Logcatbe
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
