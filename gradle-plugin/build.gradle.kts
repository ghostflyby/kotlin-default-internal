import com.vanniktech.maven.publish.GradlePublishPlugin
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm")
    id("com.github.gmazzo.buildconfig")
    id("java-gradle-plugin")
    id("com.vanniktech.maven.publish")
    id("com.gradle.plugin-publish")
}

group = "${rootProject.group}.gradle"

sourceSets {
    main {
        java.setSrcDirs(listOf("src"))
        resources.setSrcDirs(listOf("resources"))
    }
    test {
        java.setSrcDirs(listOf("test"))
        resources.setSrcDirs(listOf("testResources"))
    }
}

dependencies {
    implementation(kotlin("gradle-plugin-api"))

    testImplementation(kotlin("test-junit5"))
}

buildConfig {
    packageName(project.group.toString())

    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${rootProject.group}.kotlin.defaultInternal\"")

    val pluginProject = project(":kotlin-default-internal-compiler-plugin")
    buildConfigField("String", "KOTLIN_PLUGIN_GROUP", "\"${pluginProject.group}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_NAME", "\"kotlin-default-internal-compiler-plugin\"")
    buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"${pluginProject.version}\"")
}

kotlin {
    jvmToolchain(21)
}

gradlePlugin {
    plugins {
        website = "https://github.com/ghostflyby/kotlin-default-internal"
        vcsUrl = "https://github.com/ghostflyby/kotlin-default-internal.git"
        create("KotlinDefaultInternalPlugin") {
            id = rootProject.group.toString() + ".kotlin-default-internal"
            tags = listOf("kotlin")
            displayName = "KotlinDefaultInternalPlugin"
            description = "Kotlin compiler plugin to make `internal` the default visibility"
            implementationClass = "dev.ghostflyby.kotlin.plugin.DefaultInternalGradlePlugin"
        }
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    configure(GradlePublishPlugin())
    signAllPublications()
}