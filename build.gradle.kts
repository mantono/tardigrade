object Version {
    const val KOTLIN = "1.3.21"
    const val JVM = "1.8"
    const val COROUTINES = "1.1.1"
    const val JUNIT = "5.3.2"
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.21" apply true
    id("java") apply true
    id("maven") apply true
    id("idea") apply true
}

group = "com.mantono"
version = "0.1.0"
description = "Tardigrade"

defaultTasks = listOf("test")

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
}

dependencies {
    compile("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", Version.KOTLIN)
    compile("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", Version.COROUTINES)
    
    // Logging
    implementation("io.github.microutils", "kotlin-logging", "1.6.20")
    // Enable for applications
    runtime("ch.qos.logback", "logback-classic", "1.2.3")

    implementation("com.squareup.okhttp3:okhttp:3.14.0")

    // Junit
    testCompile("org.junit.jupiter", "junit-jupiter-api", Version.JUNIT)
    testRuntime("org.junit.jupiter", "junit-jupiter-engine", Version.JUNIT)
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
        sourceCompatibility = Version.JVM
        kotlinOptions {
            jvmTarget = Version.JVM
        }
    }


    wrapper {
        description = "Generates gradlew[.bat] scripts for faster execution"
        gradleVersion = "5.2.1"
    }
}
