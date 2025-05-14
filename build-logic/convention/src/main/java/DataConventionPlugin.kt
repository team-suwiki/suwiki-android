import com.kunize.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

internal class DataConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("suwiki.android.library")
      }

      dependencies {
        "implementation"(project(":common:model"))

        "implementation"(libs.findBundle("coroutine").get())

        "androidTestImplementation"(libs.findLibrary("junit4").get())
        "implementation"(libs.findLibrary("timber").get())
      }
    }
  }
}
