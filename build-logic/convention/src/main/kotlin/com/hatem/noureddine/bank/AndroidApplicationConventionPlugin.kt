package com.hatem.noureddine.bank

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin for configuring an Android Application module.
 *
 * This plugin applies the `com.android.application` plugin and configures:
 * - Kotlin Android options (via [configureKotlinAndroid])
 * - Android Application extension settings (implicitly through common configuration)
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(target)
            }
        }
    }
}
