plugins {
    `kotlin-dsl`
}

group = "com.hatem.noureddine.bank.buildlogic"

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
    implementation(libs.compose.compiler.gradlePlugin)
    implementation(libs.kover.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.ktlint.gradlePlugin)
    implementation(libs.sonarqube.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "bank.android.application"
            implementationClass = "com.hatem.noureddine.bank.AndroidApplicationConventionPlugin"
        }

        register("composeMultiplatform") {
            id = "bank.compose.multiplatform"
            implementationClass = "com.hatem.noureddine.bank.ComposeMultiplatformConventionPlugin"
        }
        register("kotlinMultiplatformLibrary") {
            id = "bank.kotlin.multiplatform.library"
            implementationClass = "com.hatem.noureddine.bank.KotlinMultiplatformLibraryConventionPlugin"
        }
        register("test") {
            id = "bank.test"
            implementationClass = "com.hatem.noureddine.bank.TestConventionPlugin"
        }
        register("kover") {
            id = "bank.kover"
            implementationClass = "com.hatem.noureddine.bank.KoverConventionPlugin"
        }
        register("detekt") {
            id = "bank.detekt"
            implementationClass = "com.hatem.noureddine.bank.DetektConventionPlugin"
        }
        register("ktlint") {
            id = "bank.ktlint"
            implementationClass = "com.hatem.noureddine.bank.KtlintConventionPlugin"
        }
        register("bank.sonar") {
            id = "bank.sonar"
            implementationClass = "com.hatem.noureddine.bank.SonarConventionPlugin"
        }
    }
}
