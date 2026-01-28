plugins {
    `kotlin-dsl`
}

group = "com.hatem.noureddine.bank.buildlogic"

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
    implementation(libs.compose.compiler.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "bank.android.application"
            implementationClass = "com.hatem.noureddine.bank.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "bank.android.library"
            implementationClass = "com.hatem.noureddine.bank.AndroidLibraryConventionPlugin"
        }
        register("composeMultiplatform") {
            id = "bank.compose.multiplatform"
            implementationClass = "com.hatem.noureddine.bank.ComposeMultiplatformConventionPlugin"
        }
        register("kotlinMultiplatformLibrary") {
            id = "bank.kotlin.multiplatform.library"
            implementationClass = "com.hatem.noureddine.bank.KotlinMultiplatformLibraryConventionPlugin"
        }
    }
}
