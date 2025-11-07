package com.example.feature_recruiter.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

//@Entity(tableName = "search_history")
//data class SearchHistoryEntity(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val recruiterEmail: String,        // Link to recruiter
//    val techStack: String,             // Comma-separated tech
//    val minExperience: Int,            // Min years
//    val maxExperience: Int,            // Max years
//    val resultCount: Int,              // How many matches
//    val searchedAt: Long = System.currentTimeMillis()  // When searched
//)

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val recruiterEmail: String,
    val techStack: String,             // Comma-separated tech
    val minExperience: Int,            // Min years
    val maxExperience: Int,
    //val matchedCount: Int,
    val resultCount: Int,              // How many matches
    val searchedAt: Long = System.currentTimeMillis()  // When searched
)