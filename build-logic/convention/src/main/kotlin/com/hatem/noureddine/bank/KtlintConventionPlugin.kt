package com.hatem.noureddine.bank

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

/**
 * Convention plugin for configuring Ktlint (Kotlin Linter).
 *
 * This plugin:
 * - Applies the Ktlint Gradle plugin (`org.jlleitschuh.gradle.ktlint`)
 * - Configures the Ktlint CLI version
 * - Enables Android support and console output
 * - Sets up Checkstyle reporters (compatible with CI tools)
 * - Excludes build and generated files from linting
 *
 * Use this to enforce standard Kotlin coding conventions.
 */
class KtlintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jlleitschuh.gradle.ktlint")

            extensions.configure<KtlintExtension> {
                val notFoundException = NoSuchElementException("Version 'ktlint-cli' not found in version catalog")
                version.set(libs.findVersion("ktlint-cli").orElseThrow { notFoundException }.toString())
                android.set(true)
                outputToConsole.set(true)
                ignoreFailures.set(false)
                reporters {
                    reporter(ReporterType.CHECKSTYLE) // Generates XML
                }
                filter {
                    exclude("**/build/**")
                    exclude("**/bin/**")
                    exclude("**/generated/**")
                }
            }
        }
    }
}
