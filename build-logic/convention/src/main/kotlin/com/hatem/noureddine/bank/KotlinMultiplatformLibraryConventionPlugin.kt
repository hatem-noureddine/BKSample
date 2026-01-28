package com.hatem.noureddine.bank

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for configuring a Kotlin Multiplatform Library module.
 *
 * This plugin applies:
 * - Kotlin Multiplatform plugin
 * - Android Kotlin Multiplatform Library plugin
 *
 * It configures:
 * - Shared source sets and dependencies
 * - Android Library extension for KMP (namespace, sdk versions)
 * - iOS targets (Arm64, SimulatorArm64) and framework configuration
 * - Default Kotlin compiler options
 *
 * This helps standardize the setup for all shared logic modules in the project.
 */
class KotlinMultiplatformLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
            }

            project.configureKotlin()

            extensions.configure<KotlinMultiplatformExtension> {
                val container = (this as ExtensionAware).extensions
                container.configure<KotlinMultiplatformAndroidLibraryTarget>("androidLibrary") {
                    namespace = "${project.group}.shared"
                    compileSdk = versionInt("android-compileSdk")
                    minSdk = versionInt("android-minSdk")

                    compilerOptions {
                        configureKotlin()
                    }

                    androidResources {
                        enable = true
                    }

                    withDeviceTest {
                        instrumentationRunner = "com.hatem.noureddine.bank.BankTestRunner"
                    }
                }

                listOf(
                    iosArm64(),
                    iosSimulatorArm64(),
                ).forEach { iosTarget ->
                    iosTarget.binaries.framework {
                        baseName = "ComposeApp"
                        isStatic = true
                    }
                }
            }
        }
    }
}
