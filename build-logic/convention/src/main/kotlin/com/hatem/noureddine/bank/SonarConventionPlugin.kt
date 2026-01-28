package com.hatem.noureddine.bank

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.sonarqube.gradle.SonarExtension

class SonarConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("org.sonarqube")

        target.extensions.configure(SonarExtension::class.java) {
            properties {
                property("sonar.projectKey", "BankTest")
                property("sonar.organization", "hatem-noureddine")
                property("sonar.host.url", "https://sonarcloud.io")
                property("sonar.coverage.jacoco.xmlReportPaths", "**/build/reports/kover/report.xml")
            }
        }
    }
}
