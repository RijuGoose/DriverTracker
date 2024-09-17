import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    id("io.gitlab.arturbosch.detekt") version("1.23.6")
}

buildscript {
    dependencies {
        classpath(libs.secrets.gradle.plugin)
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

tasks.register<Detekt>("detektAll") {
    val autoFix = project.hasProperty("detektAutoFix")
    config.setFrom(file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = false
    description = "detekt all"
    setSource(projectDir)
    parallel = true
    ignoreFailures = false
    autoCorrect = autoFix
    include("**/*.kt")
    include("**/*.kts")
    exclude("resources/")
    exclude("build/")
    reports {
        html.required.set(true)
        xml.required.set(false)
        txt.required.set(false)
    }
}
