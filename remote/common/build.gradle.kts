@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.suwiki.android.library)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "com.suwiki.remote.common"

  buildTypes {
    getByName("debug") {
      buildConfigField("String", "BASE_URL", "\"https://api.suwiki.kr\"")
    }

    getByName("release") {
      buildConfigField("String", "BASE_URL", "String.valueOf(\"https://api.suwiki.kr\")")
    }
  }
}

dependencies {
  implementation(projects.common.model)

  implementation(libs.bundles.coroutine)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.retrofit.core)
  implementation(libs.retrofit.kotlin.serialization)
  implementation(libs.okhttp.logging)
  androidTestImplementation(libs.junit4)

  val bom = libs.firebase.bom
  add("implementation", platform(bom))
  implementation(libs.bundles.firebase)

  implementation(libs.timber)

  implementation(libs.koin.core)
  implementation(libs.koin.android)
}
