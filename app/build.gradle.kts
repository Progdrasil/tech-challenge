import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version = "1.5.3"
val kotlin_version = "1.4.30"
val logback_version = "1.2.3"
val kotest_version = "4.4.3"
plugins {
    application
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.serialization") version "1.4.30"
}

group = "tech.challenge"
version = "0.0.1"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("org.orekit:orekit:10.3")
    // monadic results
    implementation("com.michael-bull.kotlin-result:kotlin-result:1.1.11")
    testImplementation ("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation ("io.kotest:kotest-assertions-core:$kotest_version")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("resources")

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}