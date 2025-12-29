package `in`.co.abdev.greenguardian.data

import `in`.co.abdev.greenguardian.data.model.Issues
import `in`.co.abdev.greenguardian.data.model.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    
    fun init() {
        val database = Database.connect(createHikariDataSource())
        
        transaction(database) {
            SchemaUtils.create(Users, Issues)
        }
    }
    
    private fun createHikariDataSource(): HikariDataSource {
        val config = HikariConfig().apply {
            // Use H2 in-memory database for development
            // For production, switch to PostgreSQL
            val useH2 = System.getenv("USE_H2")?.toBoolean() ?: true
            
            if (useH2) {
                driverClassName = "org.h2.Driver"
                jdbcUrl = "jdbc:h2:mem:greenguardian;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false"
                username = "sa"
                password = ""
            } else {
                driverClassName = "org.postgresql.Driver"
                jdbcUrl = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/greenguardian"
                username = System.getenv("DB_USER") ?: "postgres"
                password = System.getenv("DB_PASSWORD") ?: "postgres"
            }
            
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        
        return HikariDataSource(config)
    }
}
