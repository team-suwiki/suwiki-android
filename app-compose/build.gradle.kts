@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.suwiki.android.application)
  alias(libs.plugins.suwiki.android.application.compose)
  alias(libs.plugins.suwiki.android.hilt)
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
  implementation(projects.remote.lectureevaluation)
  implementation(projects.remote.signup)
  implementation(projects.remote.notice)
  implementation(projects.remote.user)
  implementation(projects.remote.login)

  implementation(projects.local.openmajor)
  implementation(projects.local.timetable)
  implementation(projects.local.user)
  implementation(projects.local.login)

  implementation(projects.data.openmajor)
  implementation(projects.data.timetable)
  implementation(projects.data.lectureevaluation)
  implementation(projects.data.user)
  implementation(projects.data.notice)
  implementation(projects.data.signup)
  implementation(projects.data.login)

  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.crashlytics)
  implementation(libs.firebase.analytics)

  implementation(libs.timber)
}
