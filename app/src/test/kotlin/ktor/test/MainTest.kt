package ktor.test

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.RepeatedTest
import kotlin.test.assertEquals

class MainTest {
    @RepeatedTest(500)
    fun `Should call greeting endpoint without error`() = testApplication {
        application { main(dataSource, true) }

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
        assertEquals("Hello, Test!", response.body<Map<String, Any?>>()["message"])
    }

    companion object {
        lateinit var dataSource: HikariDataSource

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            dataSource = HikariDataSource(HikariConfig("/hikari.properties"))
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            dataSource.close()
        }
    }
}
