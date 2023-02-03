buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }

    dependencies {
        classpath(libs.kotlin.gradle)
        classpath(libs.android.gradle)
        classpath(libs.dokka.gradle)
    }
}

group = "com.sunman"
version = "0.5"
status = "in progress"
description = "Yet another implementation of the calculator for Android"
