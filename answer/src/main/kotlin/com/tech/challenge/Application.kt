package com.tech.challenge

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8000) {
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
    }.start(wait = true)
}