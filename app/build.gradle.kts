plugins {
    alias(libs.plugins.android.application)
    // No necesitamos plugins de Kotlin si solo usamos Java
    // Los archivos .kts (Kotlin Script) son solo para configuración de Gradle
}

android {
    namespace = "com.bizly.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.bizly.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    // No necesitamos kotlinOptions si solo usamos Java
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // Room - Usando annotationProcessor para Java (no kapt que es para Kotlin)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    
    // ViewModel & LiveData
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}