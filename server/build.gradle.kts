plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
}

group = "in.co.abdev.greenguardian"
version = "1.0.0"
application {
    mainClass.set("in.co.abdev.greenguardian.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    
    // Ktor Server
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverContentNegotiation)
    implementation(libs.ktor.serializationKotlinxJson)
    implementation(libs.ktor.serverCors)
    implementation(libs.ktor.serverAuth)
    implementation(libs.ktor.serverAuthJwt)
    implementation(libs.ktor.serverCallLogging)
    implementation(libs.ktor.serverStatusPages)
    
    // Database
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.postgresql)
    implementation(libs.h2database)
    implementation(libs.hikaricp)
    
    // Security
    implementation(libs.bcrypt)
    
    // Serialization
    implementation(libs.kotlinx.serialization.json)
    
    // Testing
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}