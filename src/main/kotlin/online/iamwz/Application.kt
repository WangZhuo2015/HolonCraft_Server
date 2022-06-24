package online.iamwz

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import online.iamwz.holon.*
import online.iamwz.plugins.configureRouting

//
fun main() {

    // start web server
    embeddedServer(Netty, port = 80, host = "0.0.0.0") {
        install(CallLogging)
        configureRouting()
        registerDescRoutes()
        registerBlocksRoutes()
        registerStoreResult()
    }.start(wait = true)
}

fun Application.module(testing: Boolean = false) {
    registerDescRoutes()
}


