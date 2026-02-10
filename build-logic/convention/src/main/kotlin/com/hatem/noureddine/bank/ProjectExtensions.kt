package com.hatem.noureddine.bank

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Retrieval helper for the "libs" Version Catalog.
 * Allows type-safe access to libraries and versions defined in `libs.versions.toml`.
 */
internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

/**
 * Helper to retrieve a version string from the Version Catalog by its key.
 */
internal fun Project.version(key: String): String = libs.findVersion(key).get().toString()

/**
 * Helper to retrieve a version as an integer (e.g., compileSdk).
 */
internal fun Project.versionInt(key: String): Int = version(key).toInt()

/**
 * Configures common Android options for both Application and Library modules.
 * Sets compileSdk, minSdk, and targetSdk from version catalog.
 */
internal fun ApplicationExtension.configureAndroid(project: Project) {
    compileSdk = project.versionInt("android-compileSdk")
    defaultConfig {
        minSdk = project.versionInt("android-minSdk")
        targetSdk = project.versionInt("android-targetSdk")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    val javaVersion = JavaVersion.toVersion(project.version("jvm-target"))
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}

/**
 * Configures the Kotlin compiler options for the project.
 * Sets JVM target, language version, and strict modes.
 */
internal fun Project.configureKotlin() {
    val jvmTargetValue = version("jvm-target")

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.valueOf("JVM_$jvmTargetValue"))
            freeCompilerArgs.addAll(
                "-Xwhen-guards",
                "-Xnested-type-aliases",
                "-Xexpect-actual-classes",
            )

        }
    }
}

/**
 * Convenience extension to configure both Android and Kotlin settings for an Android App.
 */
internal fun ApplicationExtension.configureKotlinAndroid(project: Project) {
    configureAndroid(project)
    project.configureKotlin()
}
