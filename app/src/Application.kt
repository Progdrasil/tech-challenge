package tech.challenge

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.serialization.*
import org.orekit.data.DataContext
import org.orekit.data.DirectoryCrawler
import java.io.File

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    // setup orekit
    setup()
    install(ContentNegotiation) {
        json()
    }
    routing {
            get("/") {
                call.respondText("Hello, World")
            }
            post("/") {
                val req = call.receive<Bundle>()

                calculateOverheadPasses(req)
                    .onFailure { err ->
                        call.response.status(HttpStatusCode.BadRequest)
                        call.respond(ErrorMessage(err))
                    }
                    .onSuccess { res ->
                        call.respond(res)
                    }
            }
        }

}

fun setup() {
    val orekitData = File("resources/orekit-data")
    val manager = DataContext.getDefault().dataProvidersManager
    manager.addProvider(DirectoryCrawler(orekitData))
}