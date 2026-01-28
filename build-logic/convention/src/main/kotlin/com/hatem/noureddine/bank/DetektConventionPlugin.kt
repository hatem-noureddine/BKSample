package com.hatem.noureddine.bank

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

/**
 * Convention plugin for configuring Detekt (Static Code Analysis).
 *
 * This plugin:
 * - Applies the Detekt plugin (`io.gitlab.arturbosch.detekt`)
 * - Configures tool version and configuration file location (`config/detekt/detekt.yml`)
 * - Adds Detekt Compose rules for Jetpack Compose linting
 * - Enables HTML, XML, and TXT reports
 *
 * It is used to enforce code style and detect potential bugs.
 */
class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            extensions.configure<DetektExtension> {
                toolVersion = libs.findVersion("detekt").get().toString()
                config.setFrom(target.rootProject.layout.projectDirectory.file("config/detekt/detekt.yml"))
                buildUponDefaultConfig = true
            }

            dependencies.add("detektPlugins", libs.findLibrary("compose-rules-detekt").get())

            tasks.withType<Detekt>().configureEach {
                reports {
                    html.required.set(true)
                    xml.required.set(true)
                    txt.required.set(true)
                    sarif.required.set(true)
                }
                setSource(files(project.projectDir))
                include("**/*.kt")
                include("**/*.kts")
                exclude("**/resources/**")
                exclude("**/build/**")
            }
        }
    }
}
