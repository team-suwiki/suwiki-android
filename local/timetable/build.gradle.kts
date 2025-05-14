@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.suwiki.android.library)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "com.suwiki.local.timetable"
}

dependencies {
  implementation(projects.common.model)
  implementation(projects.common.android)
  implementation(projects.local.common)

  implementation(projects.data.timetable)

  ksp(libs.room.compiler)
  implementation(libs.room.runtime)
  implementation(libs.room.ktx)

  implementation(libs.kotlinx.serialization.json)
  implementation(libs.androidx.datastore.core)
  implementation(libs.androidx.datastore.preferences)

  testImplementation(libs.junit4)
  androidTestImplementation(libs.junit4)

  implementation(libs.koin.core)
  implementation(libs.koin.android)
}
