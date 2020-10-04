fun version(artifact: String): String {
    val key = "version.${artifact.toLowerCase()}"
    return project.ext[key]?.toString()
        ?: throw IllegalStateException("No version found for artifact '$artifact'")
}

fun projectName(): String = project.name.replace("{", "").replace("}", "")

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.0.0.RC6-2"
    id("org.jmailen.kotlinter") version "1.21.0"
    id("org.sonarqube") version "2.6.2"
    id("application") apply true
    id("org.jetbrains.kotlin.jvm") version "1.3.70" apply true
    id("java") apply true
    id("maven-publish")
    id("idea") apply true
}

application {
    mainClassName = "com.mantono.tardigrade.MainKt"
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
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", "1.4.10")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", "1.3.9")

    // Logging
    implementation("io.github.microutils", "kotlin-logging", "1.6.20")
    // Enable for applications
    // runtime("ch.qos.logback", "logback-classic", "1.2.3")

    // Ktor
    implementation("com.squareup.okhttp3:okhttp:3.14.2")

    // Junit
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
