import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.detekt)
  alias(libs.plugins.ktlint)
  alias(libs.plugins.firebase.crashlytics) apply false
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.google.services) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.protobuf) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.android.kotlin.multiplatform.library) apply false
  alias(libs.plugins.compose.compiler) apply false
}

allprojects {
  val projectPath = rootProject.file(".").absolutePath

  apply {
    plugin(rootProject.libs.plugins.detekt.get().pluginId)
    plugin(rootProject.libs.plugins.ktlint.get().pluginId)
  }

  afterEvaluate {
    detekt {
      parallel = true
      buildUponDefaultConfig = true
      toolVersion = libs.versions.detekt.get()
      config.setFrom(files("$rootDir/detekt-config.yml"))
    }
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = freeCompilerArgs + listOf(
        "-opt-in=kotlin.OptIn",
        "-opt-in=kotlin.RequiresOptIn",
      )
      freeCompilerArgs = freeCompilerArgs + listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$projectPath/report/compose-metrics",
      )
      freeCompilerArgs = freeCompilerArgs + listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$projectPath/report/compose-reports",
      )
    }
  }
}

buildscript {
  repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
  }

  dependencies {
    classpath(libs.oss.licenses.plugin)
  }
}

// Workaround for "Expecting an expression" build error
println("")
