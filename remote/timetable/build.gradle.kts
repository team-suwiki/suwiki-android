@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.suwiki.android.remote)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "com.suwiki.remote.timetable"
}

dependencies {
  implementation(projects.data.timetable)

  implementation(libs.retrofit.core)
  implementation(libs.kotlinx.serialization.json)

  implementation(libs.koin.core)
  implementation(libs.koin.android)

  val bom = libs.firebase.bom
  add("implementation", platform(bom))
  implementation(libs.bundles.firebase)
}
