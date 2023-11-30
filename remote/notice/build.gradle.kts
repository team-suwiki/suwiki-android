@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.suwiki.android.remote)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "com.suwiki.remote.notice"
}

dependencies {
  implementation(projects.data.notice)

  implementation(libs.retrofit.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.datetime)
}
