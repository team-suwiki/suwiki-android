@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.suwiki.android.library)
  alias(libs.plugins.suwiki.android.hilt)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.suwiki.common.security"
}

dependencies {
  implementation(projects.common.model)

  implementation(libs.bundles.coroutine)

  implementation(libs.androidx.datastore.core)
  implementation(libs.androidx.datastore.preferences)

  implementation(libs.timber)

  testImplementation(libs.junit4)
  androidTestImplementation(libs.junit4)
}
