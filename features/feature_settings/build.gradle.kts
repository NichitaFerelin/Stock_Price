import com.ferelin.Deps

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-kapt")
}

android {
  compileSdk = Deps.currentSDK
  defaultConfig {
    minSdk = Deps.minSDK
  }
  buildFeatures.apply {
    viewBinding = true
  }
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
}

dependencies {
  implementation(project(":core"))

  implementation(Deps.workManager)

  implementation(Deps.dagger)
  kapt(Deps.daggerCompilerKapt)
}