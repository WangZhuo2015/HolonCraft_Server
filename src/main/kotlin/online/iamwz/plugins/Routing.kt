package online.iamwz.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {
    // Starting point for a Ktor app:
    routing {
        static ("/"){
            resources("blockly")
        }
        get("/") {
            call.respondRedirect("/editor.html")
        }
    }

}
