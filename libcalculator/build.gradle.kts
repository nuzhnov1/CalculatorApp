@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-library`

    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.dokka.get().pluginId)
}

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlin.bigmath)
    implementation(libs.bigmath)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)

    dokkaHtmlPlugin(libs.dokka.kotlinAsJava)
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to artifactName,
                "Implementation-Version" to project.version
            )
        )
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val artifactName = "calculator-api"
group = "com.sunman"
version = "1.0"
status = "completed"
description = "A simple calculator API to perform basic mathematical operations and functions"
