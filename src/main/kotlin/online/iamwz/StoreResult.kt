package online.iamwz

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.registerStoreResult(){
    routing {
        storeResult()
    }
}
fun Route.storeResult(){
    route("/api/store_result"){
        get {
            val xml = call.parameters["xml"] ?: return@get call.respondText(
                "Missing or malformed xml",
                status = HttpStatusCode.BadRequest
            )
            val code = call.parameters["code"] ?: return@get call.respondText(
                "Missing or malformed code",
                status = HttpStatusCode.BadRequest
            )
            val name = call.parameters["name"] ?: return@get call.respondText(
                "Missing or malformed name",
                status = HttpStatusCode.BadRequest
            )
            val timeConsumption = call.parameters["time"] ?: return@get call.respondText(
                "Missing or malformed name",
                status = HttpStatusCode.BadRequest
            )



            call.respondText("succeed")

        }

    }
}