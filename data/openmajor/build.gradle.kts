@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.suwiki.android.data)
}

android {
  namespace = "com.suwiki.data.openmajor"
}

dependencies {
  implementation(projects.domain.openmajor)
  implementation(libs.koin.core)
  implementation(libs.koin.android)
}
