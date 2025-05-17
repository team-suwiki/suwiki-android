@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.suwiki.android.application)
  alias(libs.plugins.suwiki.android.application.compose)
  alias(libs.plugins.google.services)
  alias(libs.plugins.firebase.crashlytics)
  alias(libs.plugins.compose.compiler)
  id("com.google.android.gms.oss-licenses-plugin")
}

android {
  namespace = "com.kunize.uswtimetable"

  defaultConfig {
    applicationId = "com.kunize.uswtimetable"
    versionCode = 41
    versionName = "2.3.7"
  }
}

dependencies {
  implementation(projects.presentation.common.designsystem)
  implementation(projects.presentation.navigator)

  implementation(projects.remote.openmajor)
  implementation(projects.remote.timetable)
  implementation(projects.remote.common)

  implementation(projects.local.openmajor)
  implementation(projects.local.timetable)
  implementation(projects.local.common)

  implementation(projects.data.openmajor)
  implementation(projects.data.timetable)

  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.crashlytics)
  implementation(libs.firebase.analytics)

  implementation(libs.koin.core)
  implementation(libs.koin.android)

  implementation(libs.timber)
}
