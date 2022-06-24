package online.iamwz

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

//import io.ktor.serialzation

fun Application.registerDescRoutes(){
    routing {
        descriptionUploadRouting()
    }
}
fun Route.descriptionUploadRouting(){
    route("/api/description"){
        post {
            // retrieve all multipart data (suspending)
            val multipart = call.receiveMultipart()
            multipart.forEachPart { part ->
                // if part is a file (could be form item)
                if(part is PartData.FileItem) {
                    // retrieve file name of upload
//                    val name = part.originalFileName!!
//                    val file = File("/uploads/$name")
                    val file = File("/uploads/holon.owl")

                    // use InputStream from part to save file
                    part.streamProvider().use { its ->
                        // copy the stream to the file with buffering
                        file.outputStream().buffered().use {
                            // note that this is blocking
                            its.copyTo(it)
                        }
                    }
                }
                // make sure to dispose of the part after use to prevent leaks
                part.dispose()
            }
            call.respondText { "desc" }
            print("upload")
        }
    }
}