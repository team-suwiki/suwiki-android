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
include(":remote:lectureevaluation")
include(":remote:signup")
include(":remote:notice")
include(":remote:user")
include(":remote:login")

include(":local:openmajor")
include(":local:timetable")
include(":local:user")
include(":local:login")

include(":data:openmajor")
include(":data:timetable")
include(":data:lectureevaluation")
include(":data:user")
include(":data:notice")
include(":data:signup")
include(":data:login")

include(":domain:openmajor")
include(":domain:user")
include(":domain:signup")
include(":domain:lectureevaluation")
include(":domain:timetable")
include(":domain:notice")
include(":domain:login")

include(":presentation:navigator")
include(":presentation:openmajor")
include(":presentation:timetable")
include(":presentation:lectureevaluation")
include(":presentation:myinfo")
include(":presentation:notice")
include(":presentation:signup")
include(":presentation:login")
include(":local:common")
include(":remote:common")
include(":domain:common")
include(":presentation:common:ui")
include(":presentation:common:designsystem")
include(":shared")
