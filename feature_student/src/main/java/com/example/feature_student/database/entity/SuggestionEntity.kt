package com.example.feature_student.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suggestions")
data class SuggestionEntity(
    @PrimaryKey val id: String,
    val resumeId: String, // Foreign key reference
    val category: String,
    val title: String,
    val description: String,
    val priority: String,
    val isFixed: Boolean = false
)