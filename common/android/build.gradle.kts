@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.suwiki.android.library)
}

android {
  namespace = "com.suwiki.common.android"
}

dependencies {
  implementation(projects.common.model)

  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.crashlytics)

  implementation(libs.timber)
}
