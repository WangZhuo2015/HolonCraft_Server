val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("online.iamwz.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/net.sourceforge.owlapi/owlapi-distribution
    implementation("net.sourceforge.owlapi:owlapi-distribution:5.1.19")
    implementation("net.sourceforge.owlapi:org.semanticweb.hermit:1.4.5.519")

    implementation("com.scalified:tree:0.2.5")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
    implementation("io.ktor:ktor-freemarker:$ktor_version")

}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "online.iamwz.ApplicationKt"))
        }
    }
}