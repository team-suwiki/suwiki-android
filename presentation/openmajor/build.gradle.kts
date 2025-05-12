@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.suwiki.android.presentation.compose)
}

android {
  namespace = "com.suwiki.presentation.openmajor"
}

dependencies {
  api(projects.domain.openmajor)
  api(projects.domain.timetable)

  implementation(libs.koin.compose)
  implementation(libs.koin.compose.viewmodel)
  implementation(libs.koin.compose.viewmodel.navigation)
}
