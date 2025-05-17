import com.kunize.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

internal class PresentationComposeConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("suwiki.android.library")
        apply("suwiki.android.library.compose")
      }

      dependencies {
        "implementation"(project(":common:model"))
        "implementation"(project(":common:android"))
        "implementation"(project(":presentation:common:ui"))
        "implementation"(project(":presentation:common:designsystem"))

        "implementation"(libs.findBundle("orbit").get())

        "implementation"(libs.findLibrary("kotlinx.immutable").get())
        "implementation"(libs.findLibrary("kotlinx.coroutines.android").get())
        "implementation"(libs.findLibrary("kotlinx.coroutines.core").get())

        "androidTestImplementation"(libs.findLibrary("junit4").get())
        "implementation"(libs.findLibrary("timber").get())
      }
    }
  }
}
