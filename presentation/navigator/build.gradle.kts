@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.suwiki.android.presentation.compose)
}

android {
  namespace = "com.suwiki.presentation.navigator"
}

dependencies {
  implementation(projects.domain.notice)

  implementation(projects.presentation.openmajor)
  implementation(projects.presentation.timetable)
}
