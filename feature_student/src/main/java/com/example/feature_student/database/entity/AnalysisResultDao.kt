package com.example.feature_student.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analysis_results")
data class AnalysisResultEntity(
    @PrimaryKey val resumeId: String,
    val overallScore: Int,
    val extractedText: String,

    // ScoreBreakdown
    val formatScore: Int,
    val keywordScore: Int,
    val contentScore: Int,
    val structureScore: Int,
    val contactScore: Int,

    // KeywordAnalysis
    val foundKeywords: String, // JSON string
    val missingKeywords: String, // JSON string
    val keywordDensity: Float,

    // SectionAnalysis
    val hasContactInfo: Boolean,
    val hasObjective: Boolean,
    val hasExperience: Boolean,
    val hasEducation: Boolean,
    val hasSkills: Boolean,
    val hasCertifications: Boolean,

    // Other
    val strengths: String, // JSON string
    val weaknesses: String, // JSON string
    val detailedReport: String,
    val analyzedBy: String,
    val analyzedAt: Long,

    // Suggestions stored as JSON
    val suggestionsJson: String
)
