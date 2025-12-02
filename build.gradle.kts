// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Nota: Los archivos .kts son configuración de Gradle en Kotlin, pero el código fuente es Java
plugins {
    alias(libs.plugins.android.application) apply false
    // No necesitamos plugins de Kotlin ya que el código fuente es 100% Java
}