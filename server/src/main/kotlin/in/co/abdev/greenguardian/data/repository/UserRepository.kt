package `in`.co.abdev.greenguardian.data.repository

import `in`.co.abdev.greenguardian.data.model.UserDTO
import `in`.co.abdev.greenguardian.data.model.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.time.Instant
import java.util.*

class UserRepository {
    
    fun createUser(name: String, email: String, password: String, role: String = "CITIZEN"): UserDTO? = transaction {
        // Check if user already exists
        val existing = Users.selectAll().where { Users.email eq email }.singleOrNull()
        if (existing != null) return@transaction null
        
        val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())
        
        val userId = Users.insertAndGetId {
            it[Users.name] = name
            it[Users.email] = email
            it[Users.passwordHash] = passwordHash
            it[Users.role] = role
            it[createdAt] = Instant.now()
        }
        
        Users.selectAll().where { Users.id eq userId }
            .map { it.toUserDTO() }
            .singleOrNull()
    }
    
    fun findByEmail(email: String): UserDTO? = transaction {
        Users.selectAll().where { Users.email eq email }
            .mapNotNull { it.toUserDTO() }
            .singleOrNull()
    }
    
    fun findById(id: String): UserDTO? = transaction {
        Users.selectAll().where { Users.id eq UUID.fromString(id) }
            .mapNotNull { it.toUserDTO() }
            .singleOrNull()
    }
    
    fun verifyPassword(email: String, password: String): Boolean = transaction {
        val user = Users.selectAll().where { Users.email eq email }.singleOrNull()
        user?.let { BCrypt.checkpw(password, it[Users.passwordHash]) } ?: false
    }
    
    fun getAllUsers(): List<UserDTO> = transaction {
        Users.selectAll().map { it.toUserDTO() }
    }
    
    fun updateUserRole(id: String, newRole: String): Boolean = transaction {
        Users.update({ Users.id eq UUID.fromString(id) }) {
            it[role] = newRole
        } > 0
    }
    
    fun deleteUser(id: String): Boolean = transaction {
        Users.deleteWhere { Users.id eq UUID.fromString(id) } > 0
    }
    
    private fun ResultRow.toUserDTO() = UserDTO(
        id = this[Users.id].toString(),
        name = this[Users.name],
        email = this[Users.email],
        role = this[Users.role]
    )
}
