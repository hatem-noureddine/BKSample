package com.hatem.noureddine.bank

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for configuring testing across the project.
 *
 * This plugin:
 * - Adds common test dependencies (`kotlin-test`, `kotlinx-coroutines-test`, `compose-ui-test`)
 *   to the `commonTest` source set for KMP modules.
 * - Configures JUnit Platform for all Test tasks.
 *
 * Use this plugin to ensure consistent test setups and dependency versions.
 */
class TestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.getByName("commonTest").dependencies {
                    implementation(libs.findLibrary("kotlin-test").get())
                    implementation(libs.findLibrary("kotlinx-coroutines-test").get())
                    implementation(libs.findLibrary("compose-ui-test").get())
                }
            }

        }
    }
}
