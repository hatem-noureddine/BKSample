package com.hatem.noureddine.bank

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.version(key: String): String = libs.findVersion(key).get().toString()
internal fun Project.versionInt(key: String): Int = version(key).toInt()

internal fun LibraryExtension.configureAndroid(project: Project) {
    compileSdk = project.versionInt("android-compileSdk")
    defaultConfig {
        minSdk = project.versionInt("android-minSdk")
    }
    val javaVersion = JavaVersion.toVersion(project.version("jvm-target"))
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}

internal fun ApplicationExtension.configureAndroid(project: Project) {
    compileSdk = project.versionInt("android-compileSdk")
    defaultConfig {
        minSdk = project.versionInt("android-minSdk")
        targetSdk = project.versionInt("android-targetSdk")
    }
    val javaVersion = JavaVersion.toVersion(project.version("jvm-target"))
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}

internal fun Project.configureKotlin() {
    val jvmTargetValue = version("jvm-target")
    val kotlinLanguageVersion = version("kotlin-language")

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.valueOf("JVM_$jvmTargetValue"))
            freeCompilerArgs.addAll(
                "-language-version", kotlinLanguageVersion,
                "-Xwhen-guards",
                "-Xcontext-parameters",
                "-Xexplicit-backing-fields",
                "-Xnested-type-aliases"
            )
        }
    }
}

internal fun LibraryExtension.configureKotlinAndroid(project: Project) {
    configureAndroid(project)
    project.configureKotlin()
}

internal fun ApplicationExtension.configureKotlinAndroid(project: Project) {
    configureAndroid(project)
    project.configureKotlin()
}
