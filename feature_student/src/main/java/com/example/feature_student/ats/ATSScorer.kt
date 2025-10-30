package com.example.feature_student.ats

import com.example.feature_student.model.*

/**
 * FREE ATS Scoring Algorithm
 * No external API - completely local
 */
class ATSScorer {

    private val commonKeywords = listOf(
        // Technical Skills
        "java", "kotlin", "python", "javascript", "react", "android", "ios",
        "sql", "database", "api", "git", "agile", "scrum",

        // Soft Skills
        "leadership", "teamwork", "communication", "problem-solving",
        "analytical", "creative", "motivated", "organized",

        // Professional
        "managed", "developed", "implemented", "designed", "created",
        "led", "coordinated", "achieved", "improved", "optimized",

        // Education
        "bachelor", "master", "phd", "degree", "university", "college",

        // Experience
        "experience", "years", "project", "team", "client", "customer"
    )

    private val actionVerbs = listOf(
        "achieved", "improved", "trained", "managed", "created",
        "designed", "developed", "implemented", "led", "coordinated",
        "organized", "resolved", "analyzed", "built", "increased",
        "decreased", "reduced", "optimized", "streamlined"
    )

    fun analyzeResume(resumeId: String, text: String): ATSAnalysisResult {
        val cleanText = text.lowercase()

        // 1. Analyze Sections
        val sectionAnalysis = analyzeSections(cleanText)

        // 2. Analyze Keywords
        val keywordAnalysis = analyzeKeywords(cleanText)

        // 3. Calculate Scores
        val breakdown = calculateBreakdown(cleanText, sectionAnalysis, keywordAnalysis)

        // 4. Generate Suggestions
        val suggestions = generateSuggestions(breakdown, sectionAnalysis, keywordAnalysis, cleanText)

        // 5. Calculate Overall Score
        val overallScore = calculateOverallScore(breakdown)

        return ATSAnalysisResult(
            resumeId = resumeId,
            overallScore = overallScore,
            extractedText = text,
            breakdown = breakdown,
            suggestions = suggestions,
            keywords = keywordAnalysis,
            sections = sectionAnalysis
        )
    }

    private fun analyzeSections(text: String): SectionAnalysis {
        return SectionAnalysis(
            hasContactInfo = hasContactInfo(text),
            hasObjective = text.contains("objective") || text.contains("summary") || text.contains("profile"),
            hasExperience = text.contains("experience") || text.contains("work history") || text.contains("employment"),
            hasEducation = text.contains("education") || text.contains("qualification") || text.contains("degree"),
            hasSkills = text.contains("skills") || text.contains("technical") || text.contains("proficiencies"),
            hasCertifications = text.contains("certification") || text.contains("certified") || text.contains("license")
        )
    }

    private fun hasContactInfo(text: String): Boolean {
        val hasEmail = text.contains("@")
        val hasPhone = text.matches(Regex(".*\\d{10}.*"))
        return hasEmail || hasPhone
    }

    private fun analyzeKeywords(text: String): KeywordAnalysis {
        val found = mutableListOf<String>()
        val missing = mutableListOf<String>()

        commonKeywords.forEach { keyword ->
            if (text.contains(keyword, ignoreCase = true)) {
                found.add(keyword)
            } else {
                missing.add(keyword)
            }
        }

        val wordCount = text.split("\\s+".toRegex()).size
        val keywordDensity = if (wordCount > 0) {
            (found.size.toFloat() / wordCount) * 100
        } else 0f

        return KeywordAnalysis(
            foundKeywords = found.take(20),
            missingKeywords = missing.take(10),
            keywordDensity = keywordDensity
        )
    }

    private fun calculateBreakdown(
        text: String,
        sections: SectionAnalysis,
        keywords: KeywordAnalysis
    ): ScoreBreakdown {
        // Format Score (0-100)
        val formatScore = calculateFormatScore(text)

        // Keyword Score (0-100)
        val keywordScore = ((keywords.foundKeywords.size.toFloat() / commonKeywords.size) * 100).toInt().coerceIn(0, 100)

        // Content Score (0-100)
        val contentScore = calculateContentScore(text)

        // Structure Score (0-100)
        val structureScore = calculateStructureScore(sections)

        // Contact Score (0-100)
        val contactScore = if (sections.hasContactInfo) 100 else 0

        return ScoreBreakdown(
            formatScore = formatScore,
            keywordScore = keywordScore,
            contentScore = contentScore,
            structureScore = structureScore,
            contactScore = contactScore
        )
    }

    private fun calculateFormatScore(text: String): Int {
        var score = 60 // Base score

        // Check for bullet points or structured content
        if (text.contains("•") || text.contains("*") || text.contains("-")) score += 10

        // Check for proper capitalization
        val lines = text.split("\n")
        val capitalizedLines = lines.count { it.firstOrNull()?.isUpperCase() == true }
        if (capitalizedLines > lines.size / 2) score += 10

        // Check for reasonable length
        val wordCount = text.split("\\s+".toRegex()).size
        if (wordCount in 300..1000) score += 20

        return score.coerceIn(0, 100)
    }

    private fun calculateContentScore(text: String): Int {
        var score = 50 // Base score

        // Check for action verbs
        val actionVerbCount = actionVerbs.count { text.contains(it, ignoreCase = true) }
        score += (actionVerbCount * 2).coerceAtMost(25)

        // Check for quantifiable achievements (numbers)
        val numberMatches = "\\d+".toRegex().findAll(text).count()
        score += (numberMatches * 2).coerceAtMost(25)

        return score.coerceIn(0, 100)
    }

    private fun calculateStructureScore(sections: SectionAnalysis): Int {
        var score = 0
        if (sections.hasContactInfo) score += 20
        if (sections.hasObjective) score += 15
        if (sections.hasExperience) score += 25
        if (sections.hasEducation) score += 20
        if (sections.hasSkills) score += 15
        if (sections.hasCertifications) score += 5

        return score.coerceIn(0, 100)
    }

    private fun calculateOverallScore(breakdown: ScoreBreakdown): Int {
        return ((breakdown.formatScore * 0.15) +
                (breakdown.keywordScore * 0.30) +
                (breakdown.contentScore * 0.25) +
                (breakdown.structureScore * 0.25) +
                (breakdown.contactScore * 0.05)).toInt()
    }

    private fun generateSuggestions(
        breakdown: ScoreBreakdown,
        sections: SectionAnalysis,
        keywords: KeywordAnalysis,
        text: String
    ): List<Suggestion> {
        val suggestions = mutableListOf<Suggestion>()

        // Contact Info
        if (!sections.hasContactInfo) {
            suggestions.add(
                Suggestion(
                    id = "contact_1",
                    category = SuggestionCategory.CONTACT_INFO,
                    title = "Add Contact Information",
                    description = "Include your email address, phone number, and LinkedIn profile at the top of your resume.",
                    priority = Priority.HIGH
                )
            )
        }

        // Keywords
        if (breakdown.keywordScore < 60) {
            suggestions.add(
                Suggestion(
                    id = "keyword_1",
                    category = SuggestionCategory.KEYWORDS,
                    title = "Add More Relevant Keywords",
                    description = "Include industry-specific keywords like: ${keywords.missingKeywords.take(5).joinToString(", ")}. This helps ATS systems match your resume to job descriptions.",
                    priority = Priority.HIGH
                )
            )
        }

        // Structure
        if (!sections.hasExperience) {
            suggestions.add(
                Suggestion(
                    id = "structure_1",
                    category = SuggestionCategory.STRUCTURE,
                    title = "Add Work Experience Section",
                    description = "Include a dedicated 'Work Experience' or 'Professional Experience' section detailing your past roles.",
                    priority = Priority.HIGH
                )
            )
        }

        if (!sections.hasSkills) {
            suggestions.add(
                Suggestion(
                    id = "structure_2",
                    category = SuggestionCategory.SKILLS,
                    title = "Create a Skills Section",
                    description = "Add a 'Skills' section listing your technical and soft skills relevant to your target role.",
                    priority = Priority.HIGH
                )
            )
        }

        // Content Quality
        if (breakdown.contentScore < 70) {
            val hasActionVerbs = actionVerbs.any { text.contains(it, ignoreCase = true) }
            if (!hasActionVerbs) {
                suggestions.add(
                    Suggestion(
                        id = "content_1",
                        category = SuggestionCategory.CONTENT,
                        title = "Use Strong Action Verbs",
                        description = "Start bullet points with action verbs like 'Developed', 'Led', 'Implemented', 'Achieved' instead of 'Responsible for' or passive language.",
                        priority = Priority.MEDIUM
                    )
                )
            }

            suggestions.add(
                Suggestion(
                    id = "content_2",
                    category = SuggestionCategory.CONTENT,
                    title = "Quantify Your Achievements",
                    description = "Add numbers and metrics to your accomplishments. Example: 'Increased sales by 25%' or 'Managed team of 10 developers'.",
                    priority = Priority.HIGH
                )
            )
        }

        // Formatting
        if (breakdown.formatScore < 70) {
            suggestions.add(
                Suggestion(
                    id = "format_1",
                    category = SuggestionCategory.FORMATTING,
                    title = "Improve Resume Formatting",
                    description = "Use consistent bullet points, clear section headers, and professional fonts. Avoid tables, graphics, or complex formatting that ATS systems can't read.",
                    priority = Priority.MEDIUM
                )
            )
        }

        // Word count
        val wordCount = text.split("\\s+".toRegex()).size
        if (wordCount < 300) {
            suggestions.add(
                Suggestion(
                    id = "content_3",
                    category = SuggestionCategory.CONTENT,
                    title = "Expand Your Resume Content",
                    description = "Your resume appears too brief. Aim for 300-800 words with detailed descriptions of your experience and achievements.",
                    priority = Priority.MEDIUM
                )
            )
        } else if (wordCount > 1000) {
            suggestions.add(
                Suggestion(
                    id = "content_4",
                    category = SuggestionCategory.CONTENT,
                    title = "Condense Your Resume",
                    description = "Your resume may be too lengthy. Focus on the most relevant and recent experience, keeping it concise and impactful.",
                    priority = Priority.LOW
                )
            )
        }

        return suggestions
    }
}