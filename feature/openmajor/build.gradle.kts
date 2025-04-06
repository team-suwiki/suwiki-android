@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.suwiki.android.feature.compose)
}

android {
  namespace = "com.suwiki.feature.openmajor"
}

dependencies {
  implementation(projects.domain.openmajor)
  implementation(projects.domain.timetable)
}
