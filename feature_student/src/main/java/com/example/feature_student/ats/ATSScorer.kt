//package com.example.feature_student.ats
//
//import com.example.feature_student.model.*
//
///**
// * FREE ATS Scoring Algorithm
// * No external API - completely local
// */
//class ATSScorer {
//
//    private val commonKeywords = listOf(
//        // Technical Skills
//        "java", "kotlin", "python", "javascript", "react", "android", "ios",
//        "sql", "database", "api", "git", "agile", "scrum",
//
//        // Soft Skills
//        "leadership", "teamwork", "communication", "problem-solving",
//        "analytical", "creative", "motivated", "organized",
//
//        // Professional
//        "managed", "developed", "implemented", "designed", "created",
//        "led", "coordinated", "achieved", "improved", "optimized",
//
//        // Education
//        "bachelor", "master", "phd", "degree", "university", "college",
//
//        // Experience
//        "experience", "years", "project", "team", "client", "customer"
//    )
//
//    private val actionVerbs = listOf(
//        "achieved", "improved", "trained", "managed", "created",
//        "designed", "developed", "implemented", "led", "coordinated",
//        "organized", "resolved", "analyzed", "built", "increased",
//        "decreased", "reduced", "optimized", "streamlined"
//    )
//
//    fun analyzeResume(resumeId: String, text: String): ATSAnalysisResult {
//        val cleanText = text.lowercase()
//
//        // 1. Analyze Sections
//        val sectionAnalysis = analyzeSections(cleanText)
//
//        // 2. Analyze Keywords
//        val keywordAnalysis = analyzeKeywords(cleanText)
//
//        // 3. Calculate Scores
//        val breakdown = calculateBreakdown(cleanText, sectionAnalysis, keywordAnalysis)
//
//        // 4. Generate Suggestions
//        val suggestions = generateSuggestions(breakdown, sectionAnalysis, keywordAnalysis, cleanText)
//
//        // 5. Calculate Overall Score
//        val overallScore = calculateOverallScore(breakdown)
//
//        return ATSAnalysisResult(
//            resumeId = resumeId,
//            overallScore = overallScore,
//            extractedText = text,
//            breakdown = breakdown,
//            suggestions = suggestions,
//            keywords = keywordAnalysis,
//            sections = sectionAnalysis
//        )
//    }
//
//    private fun analyzeSections(text: String): SectionAnalysis {
//        return SectionAnalysis(
//            hasContactInfo = hasContactInfo(text),
//            hasObjective = text.contains("objective") || text.contains("summary") || text.contains("profile"),
//            hasExperience = text.contains("experience") || text.contains("work history") || text.contains("employment"),
//            hasEducation = text.contains("education") || text.contains("qualification") || text.contains("degree"),
//            hasSkills = text.contains("skills") || text.contains("technical") || text.contains("proficiencies"),
//            hasCertifications = text.contains("certification") || text.contains("certified") || text.contains("license")
//        )
//    }
//
//    private fun hasContactInfo(text: String): Boolean {
//        val hasEmail = text.contains("@")
//        val hasPhone = text.matches(Regex(".*\\d{10}.*"))
//        return hasEmail || hasPhone
//    }
//
//    private fun analyzeKeywords(text: String): KeywordAnalysis {
//        val found = mutableListOf<String>()
//        val missing = mutableListOf<String>()
//
//        commonKeywords.forEach { keyword ->
//            if (text.contains(keyword, ignoreCase = true)) {
//                found.add(keyword)
//            } else {
//                missing.add(keyword)
//            }
//        }
//
//        val wordCount = text.split("\\s+".toRegex()).size
//        val keywordDensity = if (wordCount > 0) {
//            (found.size.toFloat() / wordCount) * 100
//        } else 0f
//
//        return KeywordAnalysis(
//            foundKeywords = found.take(20),
//            missingKeywords = missing.take(10),
//            keywordDensity = keywordDensity
//        )
//    }
//
//    private fun calculateBreakdown(
//        text: String,
//        sections: SectionAnalysis,
//        keywords: KeywordAnalysis
//    ): ScoreBreakdown {
//        // Format Score (0-100)
//        val formatScore = calculateFormatScore(text)
//
//        // Keyword Score (0-100)
//        val keywordScore = ((keywords.foundKeywords.size.toFloat() / commonKeywords.size) * 100).toInt().coerceIn(0, 100)
//
//        // Content Score (0-100)
//        val contentScore = calculateContentScore(text)
//
//        // Structure Score (0-100)
//        val structureScore = calculateStructureScore(sections)
//
//        // Contact Score (0-100)
//        val contactScore = if (sections.hasContactInfo) 100 else 0
//
//        return ScoreBreakdown(
//            formatScore = formatScore,
//            keywordScore = keywordScore,
//            contentScore = contentScore,
//            structureScore = structureScore,
//            contactScore = contactScore
//        )
//    }
//
//    private fun calculateFormatScore(text: String): Int {
//        var score = 60 // Base score
//
//        // Check for bullet points or structured content
//        if (text.contains("•") || text.contains("*") || text.contains("-")) score += 10
//
//        // Check for proper capitalization
//        val lines = text.split("\n")
//        val capitalizedLines = lines.count { it.firstOrNull()?.isUpperCase() == true }
//        if (capitalizedLines > lines.size / 2) score += 10
//
//        // Check for reasonable length
//        val wordCount = text.split("\\s+".toRegex()).size
//        if (wordCount in 300..1000) score += 20
//
//        return score.coerceIn(0, 100)
//    }
//
//    private fun calculateContentScore(text: String): Int {
//        var score = 50 // Base score
//
//        // Check for action verbs
//        val actionVerbCount = actionVerbs.count { text.contains(it, ignoreCase = true) }
//        score += (actionVerbCount * 2).coerceAtMost(25)
//
//        // Check for quantifiable achievements (numbers)
//        val numberMatches = "\\d+".toRegex().findAll(text).count()
//        score += (numberMatches * 2).coerceAtMost(25)
//
//        return score.coerceIn(0, 100)
//    }
//
//    private fun calculateStructureScore(sections: SectionAnalysis): Int {
//        var score = 0
//        if (sections.hasContactInfo) score += 20
//        if (sections.hasObjective) score += 15
//        if (sections.hasExperience) score += 25
//        if (sections.hasEducation) score += 20
//        if (sections.hasSkills) score += 15
//        if (sections.hasCertifications) score += 5
//
//        return score.coerceIn(0, 100)
//    }
//
//    private fun calculateOverallScore(breakdown: ScoreBreakdown): Int {
//        return ((breakdown.formatScore * 0.15) +
//                (breakdown.keywordScore * 0.30) +
//                (breakdown.contentScore * 0.25) +
//                (breakdown.structureScore * 0.25) +
//                (breakdown.contactScore * 0.05)).toInt()
//    }
//
//    private fun generateSuggestions(
//        breakdown: ScoreBreakdown,
//        sections: SectionAnalysis,
//        keywords: KeywordAnalysis,
//        text: String
//    ): List<Suggestion> {
//        val suggestions = mutableListOf<Suggestion>()
//
//        // Contact Info
//        if (!sections.hasContactInfo) {
//            suggestions.add(
//                Suggestion(
//                    id = "contact_1",
//                    category = SuggestionCategory.CONTACT_INFO,
//                    title = "Add Contact Information",
//                    description = "Include your email address, phone number, and LinkedIn profile at the top of your resume.",
//                    priority = Priority.HIGH
//                )
//            )
//        }
//
//        // Keywords
//        if (breakdown.keywordScore < 60) {
//            suggestions.add(
//                Suggestion(
//                    id = "keyword_1",
//                    category = SuggestionCategory.KEYWORDS,
//                    title = "Add More Relevant Keywords",
//                    description = "Include industry-specific keywords like: ${keywords.missingKeywords.take(5).joinToString(", ")}. This helps ATS systems match your resume to job descriptions.",
//                    priority = Priority.HIGH
//                )
//            )
//        }
//
//        // Structure
//        if (!sections.hasExperience) {
//            suggestions.add(
//                Suggestion(
//                    id = "structure_1",
//                    category = SuggestionCategory.STRUCTURE,
//                    title = "Add Work Experience Section",
//                    description = "Include a dedicated 'Work Experience' or 'Professional Experience' section detailing your past roles.",
//                    priority = Priority.HIGH
//                )
//            )
//        }
//
//        if (!sections.hasSkills) {
//            suggestions.add(
//                Suggestion(
//                    id = "structure_2",
//                    category = SuggestionCategory.SKILLS,
//                    title = "Create a Skills Section",
//                    description = "Add a 'Skills' section listing your technical and soft skills relevant to your target role.",
//                    priority = Priority.HIGH
//                )
//            )
//        }
//
//        // Content Quality
//        if (breakdown.contentScore < 70) {
//            val hasActionVerbs = actionVerbs.any { text.contains(it, ignoreCase = true) }
//            if (!hasActionVerbs) {
//                suggestions.add(
//                    Suggestion(
//                        id = "content_1",
//                        category = SuggestionCategory.CONTENT,
//                        title = "Use Strong Action Verbs",
//                        description = "Start bullet points with action verbs like 'Developed', 'Led', 'Implemented', 'Achieved' instead of 'Responsible for' or passive language.",
//                        priority = Priority.MEDIUM
//                    )
//                )
//            }
//
//            suggestions.add(
//                Suggestion(
//                    id = "content_2",
//                    category = SuggestionCategory.CONTENT,
//                    title = "Quantify Your Achievements",
//                    description = "Add numbers and metrics to your accomplishments. Example: 'Increased sales by 25%' or 'Managed team of 10 developers'.",
//                    priority = Priority.HIGH
//                )
//            )
//        }
//
//        // Formatting
//        if (breakdown.formatScore < 70) {
//            suggestions.add(
//                Suggestion(
//                    id = "format_1",
//                    category = SuggestionCategory.FORMATTING,
//                    title = "Improve Resume Formatting",
//                    description = "Use consistent bullet points, clear section headers, and professional fonts. Avoid tables, graphics, or complex formatting that ATS systems can't read.",
//                    priority = Priority.MEDIUM
//                )
//            )
//        }
//
//        // Word count
//        val wordCount = text.split("\\s+".toRegex()).size
//        if (wordCount < 300) {
//            suggestions.add(
//                Suggestion(
//                    id = "content_3",
//                    category = SuggestionCategory.CONTENT,
//                    title = "Expand Your Resume Content",
//                    description = "Your resume appears too brief. Aim for 300-800 words with detailed descriptions of your experience and achievements.",
//                    priority = Priority.MEDIUM
//                )
//            )
//        } else if (wordCount > 1000) {
//            suggestions.add(
//                Suggestion(
//                    id = "content_4",
//                    category = SuggestionCategory.CONTENT,
//                    title = "Condense Your Resume",
//                    description = "Your resume may be too lengthy. Focus on the most relevant and recent experience, keeping it concise and impactful.",
//                    priority = Priority.LOW
//                )
//            )
//        }
//
//        return suggestions
//    }
//}

package com.example.feature_student.ats

import com.example.feature_student.model.*

/**
 * FREE ATS Scoring Algorithm
 * No external API - completely local
 */
class ATSScorer {

    private val commonKeywords = listOf(
        // Technical Skills - Software Development
        "java", "kotlin", "python", "javascript", "typescript", "react", "angular", "vue",
        "node.js", "express", "django", "flask", "spring", "hibernate",
        "android", "ios", "swift", "flutter", "react native",
        "sql", "mysql", "postgresql", "mongodb", "redis", "firebase",
        "aws", "azure", "gcp", "docker", "kubernetes", "jenkins",
        "git", "github", "gitlab", "bitbucket", "jira", "confluence",
        "rest api", "graphql", "microservices", "devops", "ci/cd",
        "machine learning", "deep learning", "tensorflow", "pytorch",
        "data analysis", "pandas", "numpy", "matplotlib",
        "html", "css", "sass", "bootstrap", "tailwind",

        // Soft Skills & Professional
        "leadership", "teamwork", "communication", "collaboration",
        "problem-solving", "critical thinking", "analytical",
        "creative", "innovative", "motivated", "organized",
        "time management", "project management", "agile", "scrum",
        "presentation", "negotiation", "conflict resolution",

        // Action Verbs
        "managed", "developed", "implemented", "designed", "created",
        "led", "coordinated", "achieved", "improved", "optimized",
        "streamlined", "enhanced", "established", "increased",
        "reduced", "delivered", "executed", "facilitated",

        // Business & Management
        "strategy", "planning", "budget", "revenue", "growth",
        "stakeholder", "client", "customer", "vendor",
        "roi", "kpi", "metrics", "analytics", "reporting",

        // Education & Certifications
        "bachelor", "master", "phd", "degree", "certification",
        "university", "college", "training", "workshop",
        "aws certified", "google certified", "microsoft certified",

        // Industry-Specific
        "fintech", "healthcare", "e-commerce", "saas", "b2b", "b2c",
        "startup", "enterprise", "corporate", "consulting"
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
        var suggestionId = 1

        // ===== CONTACT INFORMATION =====
        if (!sections.hasContactInfo) {
            suggestions.add(
                Suggestion(
                    id = "contact_${suggestionId++}",
                    category = SuggestionCategory.CONTACT_INFO,
                    title = "Add Complete Contact Information",
                    description = "Include your full name, phone number, professional email address, and LinkedIn profile URL at the top of your resume. Make sure your email sounds professional (avoid nicknames).",
                    priority = Priority.HIGH
                )
            )
        }

        val hasEmail = text.contains("@")
        val hasPhone = text.matches(Regex(".*\\d{10}.*"))
        val hasLinkedIn = text.contains("linkedin", ignoreCase = true)

        if (hasEmail && !hasPhone) {
            suggestions.add(
                Suggestion(
                    id = "contact_${suggestionId++}",
                    category = SuggestionCategory.CONTACT_INFO,
                    title = "Add Phone Number",
                    description = "Include your phone number in the format: +1 (XXX) XXX-XXXX or similar. Recruiters often prefer calling for initial screening.",
                    priority = Priority.MEDIUM
                )
            )
        }

        if (!hasLinkedIn) {
            suggestions.add(
                Suggestion(
                    id = "contact_${suggestionId++}",
                    category = SuggestionCategory.CONTACT_INFO,
                    title = "Add LinkedIn Profile",
                    description = "Include your LinkedIn profile URL. Make sure your LinkedIn profile is updated and professional. Format: linkedin.com/in/yourname",
                    priority = Priority.MEDIUM
                )
            )
        }

        // ===== STRUCTURE & SECTIONS =====
        if (!sections.hasObjective) {
            suggestions.add(
                Suggestion(
                    id = "structure_${suggestionId++}",
                    category = SuggestionCategory.STRUCTURE,
                    title = "Add Professional Summary",
                    description = "Include a 2-3 sentence professional summary at the top. Example: 'Results-driven Software Engineer with 5+ years of experience in full-stack development. Proven track record of delivering scalable solutions and leading cross-functional teams.'",
                    priority = Priority.HIGH
                )
            )
        }

        if (!sections.hasExperience) {
            suggestions.add(
                Suggestion(
                    id = "structure_${suggestionId++}",
                    category = SuggestionCategory.STRUCTURE,
                    title = "Add Work Experience Section",
                    description = "Create a dedicated 'Work Experience' or 'Professional Experience' section. List your jobs in reverse chronological order (most recent first).",
                    priority = Priority.HIGH
                )
            )
        }

        if (!sections.hasEducation) {
            suggestions.add(
                Suggestion(
                    id = "structure_${suggestionId++}",
                    category = SuggestionCategory.STRUCTURE,
                    title = "Add Education Section",
                    description = "Include your educational background: degree name, university/college name, graduation year, and GPA (if above 3.5). Add relevant coursework if you're a recent graduate.",
                    priority = Priority.HIGH
                )
            )
        }

        if (!sections.hasSkills) {
            suggestions.add(
                Suggestion(
                    id = "structure_${suggestionId++}",
                    category = SuggestionCategory.SKILLS,
                    title = "Create a Skills Section",
                    description = "Add a dedicated 'Skills' or 'Technical Skills' section. Categorize skills: Programming Languages, Frameworks, Tools, Databases, Cloud Platforms, Soft Skills.",
                    priority = Priority.HIGH
                )
            )
        }

        if (!sections.hasCertifications && text.length > 200) {
            suggestions.add(
                Suggestion(
                    id = "structure_${suggestionId++}",
                    category = SuggestionCategory.STRUCTURE,
                    title = "Add Certifications (if applicable)",
                    description = "If you have relevant certifications (AWS, Google Cloud, Microsoft, CompTIA, etc.), create a 'Certifications' section to showcase them.",
                    priority = Priority.LOW
                )
            )
        }

        // ===== KEYWORDS & ATS OPTIMIZATION =====
        if (breakdown.keywordScore < 70) {
            suggestions.add(
                Suggestion(
                    id = "keyword_${suggestionId++}",
                    category = SuggestionCategory.KEYWORDS,
                    title = "Increase Keyword Density",
                    description = "Add more industry-relevant keywords: ${keywords.missingKeywords.take(10).joinToString(", ")}. Match your resume to job descriptions by including these terms naturally.",
                    priority = Priority.HIGH
                )
            )
        }

        if (keywords.keywordDensity < 2.0) {
            suggestions.add(
                Suggestion(
                    id = "keyword_${suggestionId++}",
                    category = SuggestionCategory.KEYWORDS,
                    title = "Optimize for ATS Systems",
                    description = "Your keyword density is low. ATS systems scan for specific keywords. Review job postings in your field and incorporate relevant technical terms and skills naturally throughout your resume.",
                    priority = Priority.HIGH
                )
            )
        }

        suggestions.add(
            Suggestion(
                id = "keyword_${suggestionId++}",
                category = SuggestionCategory.KEYWORDS,
                title = "Use Industry-Standard Terms",
                description = "Avoid uncommon abbreviations. Use standard industry terms (e.g., 'JavaScript' instead of 'JS', 'Search Engine Optimization' instead of 'SEO' on first mention).",
                priority = Priority.MEDIUM
            )
        )

        // ===== CONTENT QUALITY =====
        if (breakdown.contentScore < 70) {
            val hasActionVerbs = actionVerbs.any { text.contains(it, ignoreCase = true) }
            if (!hasActionVerbs) {
                suggestions.add(
                    Suggestion(
                        id = "content_${suggestionId++}",
                        category = SuggestionCategory.CONTENT,
                        title = "Use Strong Action Verbs",
                        description = "Start each bullet point with powerful action verbs: Achieved, Developed, Led, Implemented, Optimized, Streamlined, Enhanced, Delivered. Avoid passive language like 'Responsible for' or 'Worked on'.",
                        priority = Priority.HIGH
                    )
                )
            }

            suggestions.add(
                Suggestion(
                    id = "content_${suggestionId++}",
                    category = SuggestionCategory.CONTENT,
                    title = "Quantify Your Achievements",
                    description = "Add numbers, percentages, and metrics to demonstrate impact. Examples: 'Increased revenue by 35%', 'Reduced bug count by 40%', 'Managed team of 8 developers', 'Delivered 15+ projects on time'.",
                    priority = Priority.HIGH
                )
            )
        }

        val wordCount = text.split("\\s+".toRegex()).size
        if (wordCount < 300) {
            suggestions.add(
                Suggestion(
                    id = "content_${suggestionId++}",
                    category = SuggestionCategory.CONTENT,
                    title = "Expand Your Resume Content",
                    description = "Your resume appears too brief (${wordCount} words). Aim for 400-800 words. Add detailed descriptions of your responsibilities, achievements, and the technologies you used in each role.",
                    priority = Priority.HIGH
                )
            )
        } else if (wordCount > 1000) {
            suggestions.add(
                Suggestion(
                    id = "content_${suggestionId++}",
                    category = SuggestionCategory.CONTENT,
                    title = "Condense Your Resume",
                    description = "Your resume is lengthy (${wordCount} words). For most professionals, 1 page (400-500 words) is ideal. Focus on the most relevant and recent 5-10 years of experience.",
                    priority = Priority.MEDIUM
                )
            )
        }

        suggestions.add(
            Suggestion(
                id = "content_${suggestionId++}",
                category = SuggestionCategory.CONTENT,
                title = "Remove Personal Pronouns",
                description = "Eliminate 'I', 'me', 'my', 'we' from your resume. Write in third person. Example: Instead of 'I developed...', write 'Developed...'",
                priority = Priority.MEDIUM
            )
        )

        suggestions.add(
            Suggestion(
                id = "content_${suggestionId++}",
                category = SuggestionCategory.CONTENT,
                title = "Focus on Achievements, Not Duties",
                description = "Transform job duties into achievements. Show WHAT you accomplished, not just what you did. Use the formula: Action Verb + Task + Result.",
                priority = Priority.HIGH
            )
        )

        // ===== FORMATTING =====
        if (breakdown.formatScore < 75) {
            suggestions.add(
                Suggestion(
                    id = "format_${suggestionId++}",
                    category = SuggestionCategory.FORMATTING,
                    title = "Use Consistent Formatting",
                    description = "Maintain consistent formatting throughout: Same font (Arial, Calibri, or Times New Roman), consistent bullet points, uniform spacing, and clear section headers.",
                    priority = Priority.MEDIUM
                )
            )

            suggestions.add(
                Suggestion(
                    id = "format_${suggestionId++}",
                    category = SuggestionCategory.FORMATTING,
                    title = "Optimize for ATS Parsing",
                    description = "Avoid tables, text boxes, headers/footers, images, and graphics. Use simple bullet points (• or -). ATS systems may not parse complex formatting correctly.",
                    priority = Priority.HIGH
                )
            )
        }

        suggestions.add(
            Suggestion(
                id = "format_${suggestionId++}",
                category = SuggestionCategory.FORMATTING,
                title = "Use Standard Section Headers",
                description = "Use clear, standard section names: 'Work Experience' (not 'Where I've Worked'), 'Education', 'Skills', 'Certifications'. ATS systems recognize these standard headers.",
                priority = Priority.MEDIUM
            )
        )

        suggestions.add(
            Suggestion(
                id = "format_${suggestionId++}",
                category = SuggestionCategory.FORMATTING,
                title = "Keep Font Size Readable",
                description = "Use 10-12pt font size for body text and 14-16pt for your name. Ensure adequate white space and margins (0.5-1 inch) for readability.",
                priority = Priority.LOW
            )
        )

        // ===== EXPERIENCE-SPECIFIC =====
        suggestions.add(
            Suggestion(
                id = "exp_${suggestionId++}",
                category = SuggestionCategory.EXPERIENCE,
                title = "Include Relevant Projects",
                description = "Add a 'Projects' section showcasing 2-3 significant projects. Include: project name, technologies used, your role, and measurable outcomes.",
                priority = Priority.MEDIUM
            )
        )

        suggestions.add(
            Suggestion(
                id = "exp_${suggestionId++}",
                category = SuggestionCategory.EXPERIENCE,
                title = "Highlight Leadership & Impact",
                description = "Emphasize instances where you led teams, mentored junior developers, or drove key initiatives. Leadership experience is highly valued.",
                priority = Priority.MEDIUM
            )
        )

        // ===== ADDITIONAL TIPS =====
        suggestions.add(
            Suggestion(
                id = "tip_${suggestionId++}",
                category = SuggestionCategory.CONTENT,
                title = "Tailor Resume to Job Description",
                description = "Customize your resume for each application. Mirror the language and keywords from the job posting. Highlight experiences that match the required qualifications.",
                priority = Priority.HIGH
            )
        )

        suggestions.add(
            Suggestion(
                id = "tip_${suggestionId++}",
                category = SuggestionCategory.FORMATTING,
                title = "Save as PDF",
                description = "Always submit your resume as a PDF to preserve formatting across different devices and operating systems. Name it professionally: 'FirstName_LastName_Resume.pdf'.",
                priority = Priority.LOW
            )
        )

        suggestions.add(
            Suggestion(
                id = "tip_${suggestionId++}",
                category = SuggestionCategory.CONTENT,
                title = "Proofread for Errors",
                description = "Eliminate typos, grammatical errors, and inconsistencies. Use tools like Grammarly. Ask someone else to review your resume. Even one typo can hurt your chances.",
                priority = Priority.HIGH
            )
        )

        return suggestions.sortedBy {
            when(it.priority) {
                Priority.HIGH -> 1
                Priority.MEDIUM -> 2
                Priority.LOW -> 3
            }
        }
    }
}