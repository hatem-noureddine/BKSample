package com.hatem.noureddine.bank

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.android.build.api.dsl.CommonExtension

class KotlinMultiplatformLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                // In AGP 9.0, the com.android.kotlin.multiplatform.library plugin adds an 'androidLibrary' extension
                // to the KotlinMultiplatformExtension.
                (this as ExtensionAware).extensions.configure<com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget>("androidLibrary") {
                    namespace = "${project.group}.shared"
                    compileSdk = versionInt("android-compileSdk")
                    minSdk = versionInt("android-minSdk")
                    
                    compilerOptions {
                        configureKotlin()
                    }
                    
                    androidResources {
                        enable = true
                    }
                }

                listOf(
                    iosArm64(),
                    iosSimulatorArm64()
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
