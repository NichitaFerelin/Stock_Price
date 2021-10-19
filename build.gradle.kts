buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    dependencies {
        classpath(com.ferelin.BuildPlugins.gradleTools)
        classpath(com.ferelin.BuildPlugins.kotlin)
        classpath(com.ferelin.BuildPlugins.googleServices)
        classpath(com.ferelin.BuildPlugins.versionsChecker)
    }
}
allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}