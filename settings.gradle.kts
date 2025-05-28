pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
    }
    plugins {
        kotlin("multiplatform") version "2.1.20" apply false
        kotlin("jvm") version "2.1.20" apply false
        id("com.github.gmazzo.buildconfig") version "5.6.5" apply false
        id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.16.3" apply false
        id("com.vanniktech.maven.publish") version "0.32.0" apply false
        id("com.gradle.plugin-publish") version "1.2.1" apply false
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
    }
}

rootProject.name = "kotlin-default-internal"

include("compiler-plugin")
include("gradle-plugin")

project(":compiler-plugin").name = "kotlin-default-internal-compiler-plugin"
project(":gradle-plugin").name = "kotlin-default-internal-gradle-plugin"