plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.hikaricp)
    runtimeOnly(libs.postgresql)
    runtimeOnly(libs.logback.classic)
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useKotlinTest(libs.versions.kotlin)
            dependencies {
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.server.test.host)
                implementation(libs.testcontainers)
            }
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "ktor.test.MainKt"
}
