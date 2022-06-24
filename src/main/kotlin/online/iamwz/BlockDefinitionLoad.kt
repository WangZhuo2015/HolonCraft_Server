package online.iamwz

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.iamwz.TaskGenerator.generateTask1
import online.iamwz.TaskGenerator.generateTask2
import online.iamwz.TaskGenerator.generateTask3
import java.io.File

fun Application.registerBlocksRoutes() {

    routing {
        //send blocks' definition to client
        get("/api/load-blocks") {

            val task = call.parameters["task"] ?: return@get call.respondText(
                "Missing or malformed task",
                status = HttpStatusCode.BadRequest
            )
            when (task) {
                "task1" -> call.respondText(generateTask1())
                "task2" -> call.respondText(generateTask2())
                "task3" -> call.respondText(generateTask3())
                else -> call.respondText("not support yet.")
            }
        }
        // save result to disk
        post("/api/result_report") {
            var name = call.parameters["name"] ?: return@post call.respondText(
                "Missing or malformed name",
                status = HttpStatusCode.BadRequest
            )
            val task = call.parameters["task"] ?: return@post call.respondText(
                "Missing or malformed task",
                status = HttpStatusCode.BadRequest
            )
            val content = call.receiveText()
            name = name.replace(" ","_")

            val filename = "data/$name.txt"
            val file = File(filename)
            if (file.exists()) {
                file.appendText("============================================================================================================================================================")
                file.appendText("\n\n$task\n")
                file.appendText(content)
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    file.createNewFile()
                    file.appendText(content)
                }
            }
            call.respondText("/api/upload")
        }
    }
}