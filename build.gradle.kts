fun version(artifact: String): String {
    val key = "version.${artifact.toLowerCase()}"
    return project.ext[key]?.toString()
        ?: throw IllegalStateException("No version found for artifact '$artifact'")
}

fun projectName(): String = project.name.replace("{", "").replace("}", "")

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.20" apply true
    id("java") apply true
    id("maven-publish")
    id("idea") apply true
}

group = "com.mantono"
version = "0.1.0"
description = "Tardigrade"

defaultTasks = mutableListOf("test")

repositories {
    jcenter()
    mavenCentral()
    maven(url = "https://maven.pkg.github.com/")
    maven(url = "https://dl.bintray.com/kotlin/ktor")
}

dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", "1.5.20")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", "1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")

    // Logging
    implementation("io.github.microutils", "kotlin-logging", "1.6.20")

    // Testing
    testImplementation("org.jetbrains.kotlinx", "kotlinx-coroutines-test", "1.5.1")
    testImplementation("org.junit.jupiter", "junit-jupiter-api", version("junit"))
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", version("junit"))
}

publishing {
    repositories {
        maven {
            name = "GithubPackages"
            url = uri("https://maven.pkg.github.com/mantono/${projectName()}")
            credentials {
                username = "mantono"
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register("gpr", MavenPublication::class) {
            this.artifactId = projectName()
            this.groupId = project.group.toString()
            this.version = project.version.toString()
            from(components["java"])
        }
    }
}

tasks {
    test {
        useJUnitPlatform()

        // Show test results.
        testLogging {
            events("passed", "skipped", "failed")
        }
        reports {
            junitXml.isEnabled = false
            html.isEnabled = true
        }
    }

    compileKotlin {
        sourceCompatibility = version("jvm")
        kotlinOptions {
            jvmTarget = version("jvm")
        }
    }


    wrapper {
        description = "Generates gradlew[.bat] scripts for faster execution"
        gradleVersion = version("gradle")
    }
}
