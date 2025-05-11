@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.suwiki.android.library)
  alias(libs.plugins.suwiki.android.hilt)
  alias(libs.plugins.ksp)
  alias(libs.plugins.protobuf)
  alias(libs.plugins.kotlin.serialization)
}

ksp {
  arg("room.schemaLocation", "$rootDir/schemas")
}

android {
  namespace = "com.suwiki.local.common"

  sourceSets {
    // Adds exported schema location as test app assets.
    getByName("androidTest").assets.srcDir("$rootDir/schemas")
  }
}

protobuf {
  protoc {
    artifact = libs.protobuf.protoc.get().toString()
  }
  generateProtoTasks {
    all().forEach { task ->
      task.builtins {
        register("java") {
          option("lite")
        }
      }
    }
  }
}

dependencies {
  implementation(projects.common.model)

  ksp(libs.room.compiler)
  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  androidTestImplementation(libs.room.testing)

  implementation(libs.bundles.coroutine)
  implementation(libs.androidx.datastore.core)
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.protobuf.kotlin.lite)

  implementation(libs.kotlinx.serialization.json)

  implementation(libs.koin.core)
  implementation(libs.koin.android)

  androidTestImplementation(libs.androidx.junit.ktx)
  androidTestImplementation(libs.androidx.test.runner)
}
