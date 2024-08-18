// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    // kapt plugin
    id("com.android.library") version "8.1.1" apply false
    // ksp plugin
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0")
        classpath(libs.secrets.gradle.plugin)

    }
}