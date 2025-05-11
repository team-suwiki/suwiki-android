@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.suwiki.android.remote)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "com.suwiki.remote.openmajor"
}

dependencies {
  implementation(projects.data.openmajor)

  implementation(libs.retrofit.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.datetime)

  implementation(libs.koin.core)
  implementation(libs.koin.android)
}
