package com.example.resumeanalyzer.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.resumeanalyzer.core.database.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun registerUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun loginUser(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("UPDATE users SET name = :name, email = :email, role = :role WHERE id = :id")
    suspend fun updateUser(id: Int, name: String, email: String, role: String)

    // Update user using old email as reference (for email changes)
    @Query("UPDATE users SET name = :name, email = :newEmail, role = :role WHERE email = :oldEmail")
    suspend fun updateUserByOldEmail(oldEmail: String, name: String, newEmail: String, role: String)

    // Update only name
    @Query("UPDATE users SET name = :name WHERE email = :email")
    suspend fun updateUserName(email: String, name: String)

    // Check if email exists (for validation)
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun emailExists(email: String): Int
}