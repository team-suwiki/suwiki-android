enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  includeBuild("build-logic")
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
  }
}

rootProject.name = "uswtimetable"
include(":app-compose")

include(":common:android")
include(":common:model")
include(":common:security")

include(":remote:openmajor")
include(":remote:timetable")

include(":local:openmajor")
include(":local:timetable")

include(":data:openmajor")
include(":data:timetable")

include(":domain:openmajor")
include(":domain:timetable")

include(":presentation:navigator")
include(":presentation:openmajor")
include(":presentation:timetable")
include(":local:common")
include(":remote:common")
include(":domain:common")
include(":presentation:common:ui")
include(":presentation:common:designsystem")
