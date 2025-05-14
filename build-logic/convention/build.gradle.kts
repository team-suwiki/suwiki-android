plugins {
  `kotlin-dsl`
}

group = "com.kunize.convention"

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
  compileOnly(libs.android.gradle.plugin)
  compileOnly(libs.kotlin.gradle.plugin)
  compileOnly(libs.ksp.gradle.plugin)
}

gradlePlugin {
  plugins {
    register("androidApplication") {
      id = "suwiki.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }
    register("androidApplicationCompose") {
      id = "suwiki.android.application.compose"
      implementationClass = "AndroidApplicationComposeConventionPlugin"
    }
    register("androidLibrary") {
      id = "suwiki.android.library"
      implementationClass = "AndroidLibraryConventionPlugin"
    }
    register("androidLibraryCompose") {
      id = "suwiki.android.library.compose"
      implementationClass = "AndroidLibraryComposeConventionPlugin"
    }
    register("javaLibrary") {
      id = "suwiki.java.library"
      implementationClass = "JavaLibraryConventionPlugin"
    }
    register("presentationCompose") {
      id = "suwiki.android.presentation.compose"
      implementationClass = "PresentationComposeConventionPlugin"
    }
    register("remote") {
      id = "suwiki.android.remote"
      implementationClass = "RemoteConventionPlugin"
    }
    register("data") {
      id = "suwiki.android.data"
      implementationClass = "DataConventionPlugin"
    }
  }
}
