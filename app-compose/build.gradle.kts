@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.suwiki.android.application)
  alias(libs.plugins.suwiki.android.application.compose)
  alias(libs.plugins.suwiki.android.hilt)
  alias(libs.plugins.google.services)
  alias(libs.plugins.firebase.crashlytics)
  id("com.google.android.gms.oss-licenses-plugin")
}

android {
  namespace = "com.kunize.uswtimetable"

  defaultConfig {
    applicationId = "com.kunize.uswtimetable"
    versionCode = 43
    versionName = "2.3.9"
  }
}

dependencies {
  implementation(projects.core.designsystem)
  implementation(projects.feature.navigator)

  implementation(projects.remote.openmajor)
  implementation(projects.remote.timetable)
  implementation(projects.remote.lectureevaluation.viewerreporter)
  implementation(projects.remote.lectureevaluation.my)
  implementation(projects.remote.lectureevaluation.editor)
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
  implementation(projects.data.lectureevaluation.viewerreporter)
  implementation(projects.data.lectureevaluation.editor)
  implementation(projects.data.lectureevaluation.my)
  implementation(projects.data.user)
  implementation(projects.data.notice)
  implementation(projects.data.signup)
  implementation(projects.data.login)

  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.crashlytics)
  implementation(libs.firebase.analytics)

  implementation(libs.timber)
}
