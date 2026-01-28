plugins {
    alias(libs.plugins.bank.android.application)
    alias(libs.plugins.bank.compose.multiplatform)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.bank.detekt)
    alias(libs.plugins.bank.ktlint)
}

android {
    namespace = "com.hatem.noureddine.bank"

    defaultConfig {
        applicationId = "com.hatem.noureddine.bank"
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(platform(libs.koin.bom))
    implementation(projects.composeApp)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.bundles.koin)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.startup)
}
