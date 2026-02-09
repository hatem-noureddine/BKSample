plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.multiplatform.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.kotlinCocoapods) apply false
    alias(libs.plugins.bank.sonar)
    alias(libs.plugins.detekt)
}

val reportMerge by tasks.registering(io.gitlab.arturbosch.detekt.report.ReportMergeTask::class) {
    output.set(layout.buildDirectory.file("reports/detekt/merge.sarif"))
    input.from(
        subprojects.map { subproject ->
            subproject.layout.buildDirectory.file("reports/detekt/detekt.sarif")
        }
    )
}
