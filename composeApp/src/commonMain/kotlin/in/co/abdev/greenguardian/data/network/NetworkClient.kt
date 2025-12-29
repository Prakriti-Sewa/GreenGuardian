package `in`.co.abdev.greenguardian.data.network

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object NetworkClient {
    
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }
    
    // Base URL - can be configured based on environment
    const val BASE_URL = "http://localhost:8080/api"
}
