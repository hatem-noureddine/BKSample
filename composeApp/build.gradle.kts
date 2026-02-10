import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.bank.kotlin.multiplatform.library)
    alias(libs.plugins.bank.compose.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.bank.test)
    alias(libs.plugins.bank.kover)
    alias(libs.plugins.bank.detekt)
    alias(libs.plugins.bank.ktlint)
    alias(libs.plugins.kotlinCocoapods)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}
val sqlCipherPassphrase =
    localProperties.getProperty("SQLCIPHER_PASSPHRASE")
        ?: System.getenv("SQLCIPHER_PASSPHRASE")
        ?: "DUMMY_PASSPHRASE_FOR_BUILD"

val generateSecrets by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/source/common")
    val passphrase = sqlCipherPassphrase
    inputs.property("passphrase", passphrase)
    outputs.dir(outputDir)
    doLast {
        val secretsFile = outputDir.get().file("com/hatem/noureddine/bank/data/local/Secrets.kt").asFile
        secretsFile.parentFile.mkdirs()
        secretsFile.writeText(
            """
            package com.hatem.noureddine.bank.data.local
            
            internal object Secrets {
                const val SQLCIPHER_PASSPHRASE = "$passphrase"
            }
            """.trimIndent(),
        )
    }
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.bundles.compose)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.navigation.compose)
                implementation(libs.bundles.lifecycle)
                implementation(libs.bundles.kotlinx)
                implementation(libs.bundles.ktor)
                implementation(libs.bundles.koin)
                implementation(libs.bundles.koin.compose)
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
                implementation(libs.ktor.client.mock)
                implementation(libs.bundles.datastore)
            }
            kotlin.srcDir(generateSecrets)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sqlcipher.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.androidx.sqlite.framework)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.koin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        jvmTest {
            dependencies {
                implementation(libs.kotlin.test.junit)
            }
        }

        androidDeviceTest {
            dependencies {
                implementation(libs.androidx.test.ext.junit)
                implementation(libs.androidx.espresso.core)
                implementation(libs.compose.ui.test)
                implementation(libs.androidx.compose.ui.test.junit4)
                implementation(libs.androidx.compose.ui.test.manifest)
            }
        }
    }

    cocoapods {
        name = "ComposeApp"
        summary = "ADD-YOUR-SUMMARY"
        homepage = "ADD-YOUR-HOMEPAGE"
        version = "1.0.0"
        ios.deploymentTarget = "15.3"

        framework {
            baseName = "ComposeApp"
            isStatic = true
        }
        pod(name = "SQLCipher") {
            version = "4.10.0"
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.ui.tooling)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
