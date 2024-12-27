package ktor.test

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MainTest {
    @Test
    fun `Should call greeting endpoint without error`() = testApplication {
        application { main() }

        val client = createClient {
            install(ContentNegotiation) {
                register(ContentType.Application.Json, JacksonConverter(jsonMapper))
            }
        }

        val response = client.post("/api/greeting") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            setBody(mapOf("name" to "Test"))
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"message":"Hello, Test!"}""", response.bodyAsText())
    }
}
