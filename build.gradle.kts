import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.3.41"
val jUnitVersion = "5.5.1"
val mockitoKotlinVersion = "2.1.0"
val mockitoVersion = "3.0.0"

plugins {
    kotlin("jvm") version "1.3.41"
    application
}

group = "cz.jlinhart.barcamp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testCompile("com.nhaarman.mockitokotlin2:mockito-kotlin:${mockitoKotlinVersion}")
    testCompile("org.mockito:mockito-core:${mockitoVersion}")
    testCompile("org.mockito:mockito-junit-jupiter:${mockitoVersion}")
    testCompile("org.junit.jupiter:junit-jupiter-params:${jUnitVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${jUnitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${jUnitVersion}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}