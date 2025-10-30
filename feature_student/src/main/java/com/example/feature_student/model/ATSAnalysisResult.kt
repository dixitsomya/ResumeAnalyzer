package com.example.feature_student.model

data class ATSAnalysisResult(
    val resumeId: String,
    val overallScore: Int,
    val extractedText: String,
    val breakdown: ScoreBreakdown,
    val suggestions: List<Suggestion>,
    val keywords: KeywordAnalysis,
    val sections: SectionAnalysis
)

data class ScoreBreakdown(
    val formatScore: Int,        // Out of 100
    val keywordScore: Int,        // Out of 100
    val contentScore: Int,        // Out of 100
    val structureScore: Int,      // Out of 100
    val contactScore: Int         // Out of 100
)

data class KeywordAnalysis(
    val foundKeywords: List<String>,
    val missingKeywords: List<String>,
    val keywordDensity: Float
)

data class SectionAnalysis(
    val hasContactInfo: Boolean,
    val hasObjective: Boolean,
    val hasExperience: Boolean,
    val hasEducation: Boolean,
    val hasSkills: Boolean,
    val hasCertifications: Boolean
)

data class Suggestion(
    val id: String,
    val category: SuggestionCategory,
    val title: String,
    val description: String,
    val priority: Priority,
    val isFixed: Boolean = false
)

enum class SuggestionCategory {
    FORMATTING,
    CONTENT,
    KEYWORDS,
    STRUCTURE,
    CONTACT_INFO,
    EXPERIENCE,
    SKILLS
}

enum class Priority {
    HIGH,
    MEDIUM,
    LOW
}