plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.6.2"
    id("io.micronaut.test-resources") version "3.6.2"
}

version = "0.1"
group = "com.csruletka"


val kotlinVersion = project.properties["kotlinVersion"]
repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    kapt("io.micronaut.data:micronaut-data-processor")
    kapt("io.micronaut:micronaut-http-validation")

    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut.security:micronaut-security-jwt")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.reactor:micronaut-reactor")

    // data
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("com.vladmihalcea:hibernate-types-52:2.20.0")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // steam api lib
    implementation("com.github.irasikhin:java-steam-client:59ad330c4c")

    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")

    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.postgresql:postgresql:42.5.0")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

}


application {
    mainClass.set("com.csruletka.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    jar {
        manifest {
            attributes["Main-Class"] = "com.csruletka.ApplicationKt"
        }
    }
    register("stage") {
        dependsOn(build)

    }
}

graalvmNative.toolchainDetection.set(false)

micronaut {
    runtime("netty")
    testRuntime("kotest")
    processing {
        incremental(true)
        annotations("com.csruletka.*")
    }
}