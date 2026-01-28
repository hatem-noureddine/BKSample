package com.hatem.noureddine.bank

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * Convention plugin for configuring Compose Multiplatform.
 *
 * This plugin applies:
 * - The Compose Multiplatform plugin (`org.jetbrains.compose`)
 * - The Compose Compiler plugin (`org.jetbrains.kotlin.plugin.compose`)
 *
 * It ensures that the necessary plugins are applied to modules that use Jetpack Compose
 * in a multiplatform environment.
 */
class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("compose-multiplatform").get().get().pluginId)
                apply(libs.findPlugin("compose-compiler").get().get().pluginId)
            }
        }
    }
}
