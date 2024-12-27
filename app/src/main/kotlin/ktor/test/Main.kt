package ktor.test

import com.fasterxml.jackson.core.StreamReadFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::main).start(wait = true)
}

fun Application.main() {
    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(jsonMapper))
    }

    routing {
        route("/api") {
            post("/greeting") {
                data class Request(val name: String)
                data class Response(val message: String)

                val request = call.receive<Request>()
                call.respond(Response("Hello, ${request.name}!"))
            }
        }
    }
}

val jsonMapper: JsonMapper = jacksonMapperBuilder()
    .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .build()
