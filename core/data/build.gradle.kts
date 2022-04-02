import com.ferelin.Libs

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-kapt")
}

android {
  compileSdk = Libs.Project.currentSDK

  defaultConfig {
    minSdk = Libs.Project.minSDK
  }

  buildTypes {
    val publicFinnhubDebugToken = "c5n906iad3ido15tstu0"
    val publicNomicsDebugToken = "cb99d1ebf28482d6fb54f7c9002319aea14401c7"

    debug {
      resValue(
        "string",
        "api_finnhub_token",
        (properties["apiFinnhubToken"] as String?) ?: publicFinnhubDebugToken
      )
      resValue(
        "string",
        "api_nomics_token",
        (properties["apiNomicsToken"] as String?) ?: publicNomicsDebugToken
      )
    }
    release {
      resValue(
        "string",
        "api_finnhub_token",
        (properties["apiFinnhubToken"] as String?) ?: publicFinnhubDebugToken
      )
      resValue(
        "string",
        "api_nomics_token",
        (properties["apiNomicsToken"] as String?) ?: publicNomicsDebugToken
      )
    }
  }

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
    freeCompilerArgs = freeCompilerArgs +
            ("-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi")
  }
}

dependencies {
  api(project(":core:domain"))

  api(platform(Libs.Firebase.platform))
  api(Libs.Firebase.databaseKtx)
  api(Libs.Firebase.analyticsKtx)
  api(Libs.Firebase.authenticationKtx)
  api(Libs.Firebase.crashlyticsKtx)

  api(Libs.Retrofit.core)
  api(Libs.Retrofit.converter)

  api(Libs.OkHttp.core)
  api(Libs.OkHttp.interceptor)

  api(Libs.Moshi.core)
  kapt(Libs.Moshi.compilerKapt)

  api(Libs.dataStorePreferences)

  api(Libs.Room.core)
  api(Libs.Room.ktx)
  kapt(Libs.Room.compilerKapt)

  implementation(Libs.Dagger.core)
  kapt(Libs.Dagger.compilerKapt)
}