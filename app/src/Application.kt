package tech.challenge

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.serialization.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        json()
    }
    routing {
            get("/") {
                call.respondText("Hello, World")
            }
            post("/") {
                val req = call.receive<Bundle>()

                val res = calculateOverheadPasses(req)

                call.respond(res)

            }
        }

}

