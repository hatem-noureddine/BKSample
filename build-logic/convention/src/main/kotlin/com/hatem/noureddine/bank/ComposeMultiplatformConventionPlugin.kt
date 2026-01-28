package com.hatem.noureddine.bank

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

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
