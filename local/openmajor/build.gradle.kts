@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.suwiki.android.library)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.suwiki.local.openmajor"
}

dependencies {
  implementation(projects.common.model)
  implementation(projects.common.android)
  implementation(projects.local.common)

  implementation(projects.data.openmajor)

  ksp(libs.room.compiler)
  implementation(libs.room.runtime)
  implementation(libs.room.ktx)

  implementation(libs.bundles.coroutine)
  implementation(libs.androidx.datastore.core)
  implementation(libs.androidx.datastore.preferences)

  implementation(libs.koin.core)
  implementation(libs.koin.android)

  testImplementation(libs.junit4)
  androidTestImplementation(libs.junit4)
}
