package com.hatem.noureddine.bank

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for configuring Kover (Kotlin Code Coverage).
 *
 * This plugin:
 * - Applies the Kover plugin (`org.jetbrains.kotlinx.kover`)
 * - Configures coverage reports (HTML, XML) to run on verification tasks
 * - Sets up filters to exclude UI components, generated classes, and DI modules from coverage metrics
 *
 * Use this to safeguard code quality by ensuring sufficient test coverage where it matters.
 */
class KoverConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlinx.kover")

            extensions.configure<kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension>("kover") {
                reports {
                    total {
                        html {
                            onCheck.set(true)
                        }
                        xml {
                            onCheck.set(true)
                        }
                        verify {
                            onCheck.set(true)
                            rule {
                                bound {
                                    minValue.set(90)
                                }
                            }
                        }
                        
                        filters {
                            excludes {
                                // Exclude UI components as they are hard to unit test and usually covered by screenshot/UI tests
                                packages(
                                    "com.hatem.noureddine.bank.ui",
                                    "com.hatem.noureddine.bank.di", // Dependency Injection setup
                                    "com.hatem.noureddine.bank.domain.model",
                                    "com.hatem.noureddine.bank.domain.repository",
                                    "com.hatem.noureddine.bank.data.dto",
                                    "com.hatem.noureddine.bank.data.datasource",
                                    "com.hatem.noureddine.bank.data.local",
                                    "banktest.composeapp.generated.resources",
                                )
                                // Exclude Android generated classes
                                classes(
                                    "*Activity",
                                    "*Application",
                                    "*.BuildConfig",
                                    "*ComposableSingletons*",
                                    "*Dto",
                                    "*Entity",
                                    "*Model",
                                    "*Response",
                                    "*Screen",
                                    "*Header",
                                    "*Components",
                                    "*Preview*",
                                    "*Platform*",
                                    "Greeting",
                                    "AppKt",
                                    "*Resource*",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
