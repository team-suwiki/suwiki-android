import com.android.build.gradle.LibraryExtension
import com.kunize.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.plugin.compose")
      }

      extensions.configure<LibraryExtension> {
        configureAndroidCompose(this)
      }
    }
  }
}
