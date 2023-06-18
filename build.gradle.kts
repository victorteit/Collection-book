plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
    id("dev.icerock.moko.kswift") apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenLocal()
        gradlePluginPortal()
    }
}

buildscript {
    dependencies {
        classpath ("com.android.tools.build:gradle:8.0.2")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("dev.icerock.moko:resources-generator:0.21.2")

    }
    repositories {
        mavenCentral()
        google()
    }
}
