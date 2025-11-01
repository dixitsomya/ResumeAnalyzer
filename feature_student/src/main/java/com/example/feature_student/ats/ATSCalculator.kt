package com.example.feature_student.ats

import android.content.Context
import android.util.Log
import com.example.feature_student.model.*
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ATSCalculator(private val context: Context) {

    private val TAG = "ATS_Analyzer"
    private val httpClient = HttpClient(OkHttp) {
        engine {
            config {
                connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                retryOnConnectionFailure(true)
            }
        }
    }

    suspend fun analyzeResume(resumeId: String, extractedText: String): ATSAnalysisResult {
        return withContext(Dispatchers.IO) {
            Log.d(TAG, "📊 Analyzing resume")
            analyzeWithRules(resumeId, extractedText)
        }
    }

    private fun analyzeWithRules(resumeId: String, text: String): ATSAnalysisResult {
        val lower = text.lowercase()
        val words = text.split(Regex("\\s+")).filter { it.isNotBlank() }
        val wordCount = words.size

        // ==================== COMPREHENSIVE DETECTION ====================

        // Contact Info - Check symbols AND text
        val hasEmail = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}").find(text) != null
        val hasPhone = Regex("\\+?1?\\s*\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}|\\+91\\s*\\d{5}\\s*\\d{5}|\\(\\d{3}\\)\\s*\\d{3}").find(text) != null
        //val hasLinkedIn = lower.contains("linkedin.com/in") || lower.contains("linkedin.com/company") || text.contains("in/") || Regex("linkedin\\.com/in/[a-z0-9-]+", RegexOption.IGNORE_CASE).find(text) != null
        //val hasGitHub = lower.contains("github.com") || Regex("github\\.com/[a-z0-9-]+", RegexOption.IGNORE_CASE).find(text) != null || lower.contains("github/")
        // Detect LinkedIn including symbols or partial hints
        val hasLinkedIn = listOf(
            "linkedin.com", "linkedin", "in/", "", "", "🔗", "li:", "in:"
        ).any { lower.contains(it) } ||
                Regex("linkedin\\.com/in/[a-z0-9-]+", RegexOption.IGNORE_CASE).find(text) != null

       // Detect GitHub including symbols or partial hints
        val hasGitHub = listOf(
            "github.com", "github", "gh", "", "", "git:", "gh:"
        ).any { lower.contains(it) } ||
                Regex("github\\.com/[a-z0-9-]+", RegexOption.IGNORE_CASE).find(text) != null

        //val hasPortfolio = lower.contains("portfolio") || lower.contains("website") || lower.contains("personal website") || text.contains("www.") || Regex("https?://", RegexOption.IGNORE_CASE).find(text) != null
        val hasPortfolio = listOf(
            "portfolio", "personal website","portfolio site", "work samples"
        ).any { lower.contains(it) } ||
                listOf(
                    "🌐", "💻"
                ).any { text.contains(it) } ||
                Regex("https?://(?!linkedin|github)[a-z0-9.-]+\\.[a-z]{2,}", RegexOption.IGNORE_CASE).find(text) != null

        // Sections
        val hasExperience = lower.contains("experience") || lower.contains("work history") || lower.contains("employment")
        val hasEducation = lower.contains("education") || lower.contains("degree") || lower.contains("university")
        val hasSkills = lower.contains("skills") || lower.contains("technical skills") || lower.contains("competencies") || lower.contains("expertise")
        val hasSummary = lower.contains("summary") || lower.contains("objective") || lower.contains("profile") || lower.contains("about")
        val hasCerts = lower.contains("certification") || lower.contains("certified") || lower.contains("license")
        val hasProjects = lower.contains("projects") || lower.contains("portfolio") || lower.contains("github") || lower.contains("contributions")

        // Action Verbs - Extended list
        val actionVerbs = listOf(
            "developed", "led", "managed", "implemented", "architected", "designed", "created",
            "achieved", "improved", "optimized", "accelerated", "streamlined", "delivered",
            "innovated", "engineered", "spearheaded", "collaborated", "established", "launched",
            "transformed", "built", "deployed", "directed", "mentored", "executed", "drove",
            "orchestrated", "automated", "scaled", "enhanced", "reduced", "increased", "migrated",
            "pioneered", "restructured", "modernized", "refactored", "integrated", "consolidated",
            "initiated", "contributed", "facilitated", "coordinated", "oversaw", "supervised",
            "established", "strengthened", "expanded", "maximized", "boosted", "streamlined",
            "revolutionized", "spearheaded", "championed", "propelled", "elevated", "pioneered"
        )
        val foundVerbsList = actionVerbs.filter { lower.contains(it) }.distinct()
        val foundVerbsCount = foundVerbsList.size

        // Metrics
        val percentMatches = Regex("\\d+\\s*%").findAll(text).toList()
        val hasPercent = percentMatches.isNotEmpty()
        val moneyMatches = Regex("\\$\\s*\\d+[KMB]?|€\\s*\\d+[KMB]?|₹\\s*\\d+[KMB]?").findAll(text).toList()
        val hasMoney = moneyMatches.isNotEmpty()
        val teamMatches = Regex("team\\s*(?:of\\s*)?\\d+|led\\s+\\d+|managed\\s+\\d+|supervised\\s+\\d+|oversaw\\s+\\d+").findAll(text).toList()
        val hasTeam = teamMatches.isNotEmpty()
        val yearMatches = Regex("\\d+\\+?\\s*years?\\s*(?:of|in)").findAll(text).toList()
        val hasYears = yearMatches.isNotEmpty()

        // Additional metric checks
        val hasTimelineMetrics = Regex("reduced\\s*(?:by\\s*)?\\d+\\s*(?:%|days?|weeks?|months?)|improved\\s*(?:by\\s*)?\\d+\\s*(?:%|times?)|increased\\s*(?:by\\s*)?\\d+\\s*%").find(text) != null
        val hasPerformanceMetrics = Regex("performance|efficiency|productivity|revenue|sales|conversion|engagement|retention|churn|growth|adoption").find(text) != null

        val metricsCount = listOf(hasPercent, hasMoney, hasTeam, hasYears).count { it }

        // Tech Keywords - Maximum comprehensive list
        val techKeywords = listOf(
            "java", "python", "javascript", "typescript", "kotlin", "swift", "go", "rust", "c++", "c#", "scala", "r",
            "php", "ruby", "perl", "groovy", "dart", "elixir", "clojure", "haskell", "ocaml",
            "spring", "hibernate", "springboot", "quarkus", "micronaut",
            "django", "flask", "fastapi", "tornado", "pyramid",
            "react", "angular", "vue", "svelte", "next", "nuxt", "gatsby",
            "node", "express", "fastify", "hapi", "koa",
            "mongodb", "postgresql", "mysql", "mariadb", "sqlite", "firestore", "firebase",
            "elasticsearch", "redis", "memcached", "cassandra", "dynamodb", "couchdb", "influxdb",
            "aws", "gcp", "azure", "ibm", "heroku", "digitalocean",
            "docker", "kubernetes", "docker-compose", "helm", "openshift",
            "jenkins", "gitlab-ci", "github-actions", "circleci", "travis", "bitbucket", "teamcity",
            "git", "svn", "mercurial", "perforce",
            "maven", "gradle", "npm", "pip", "yarn", "cargo", "gems", "composer",
            "agile", "scrum", "kanban", "jira", "asana", "trello", "monday",
            "rest", "graphql", "grpc", "soap", "websocket",
            "microservices", "serverless", "lambda", "faas",
            "ci/cd", "devops", "sre", "infrastructure", "iac",
            "linux", "unix", "windows", "macos",
            "nginx", "apache", "httpd", "tomcat",
            "html", "css", "xml", "json", "yaml", "toml",
            "testing", "junit", "pytest", "mocha", "jasmine", "rspec",
            "soap", "openapi", "swagger", "postman",
            "security", "oauth", "jwt", "ssl", "tls", "encryption",
            "machine learning", "tensorflow", "pytorch", "scikit", "nlp", "ai", "ml", "neural",
            "blockchain", "ethereum", "solidity", "web3",
            "mobile", "android", "ios", "react-native", "flutter", "xamarin",
            "cloud", "onpremise", "hybrid", "multi-cloud"
        )
        val foundTechList = techKeywords.filter { lower.contains(it) }.distinct()
        val foundTechCount = foundTechList.size

        // Soft Skills - Maximum comprehensive list
        val softSkillsList = listOf(
            "leadership", "communication", "teamwork", "collaboration", "problem solving",
            "critical thinking", "analytical", "creative", "innovation", "adaptability",
            "mentoring", "coaching", "delegation", "negotiation", "presentation",
            "conflict resolution", "time management", "organization", "attention to detail",
            "proactive", "initiative", "drive", "motivation", "strategic thinking",
            "decision making", "stakeholder management", "public speaking", "writing",
            "interpersonal", "empathy", "emotional intelligence", "patience", "reliability"
        )
        val foundSoftList = softSkillsList.filter { lower.contains(it) }.distinct()
        val foundSoftCount = foundSoftList.size

        // Industry/Domain keywords
        val industryKeywords = listOf(
            "fintech", "ecommerce", "healthcare", "pharma", "retail", "logistics",
            "saas", "paas", "iaas", "enterprise", "startup", "scale-up",
            "api-first", "mobile-first", "cloud-native", "microservices"
        )
        val foundIndustryCount = industryKeywords.filter { lower.contains(it) }.distinct().size

        // ==================== REALISTIC SCORING ====================
        var score = 0

        // Contact Info (max 21) - With online profiles bonus
        if (hasEmail && hasPhone) score += 12
        else if (hasEmail) score += 8
        else score += 2

        if (hasLinkedIn) score += 3
        if (hasGitHub) score += 3
        if (hasPortfolio) score += 3

        // Bonus for having both LinkedIn AND GitHub (3 points)
        if (hasLinkedIn && hasGitHub) score += 1

        // Essential Sections (max 26)
        if (hasExperience) score += 8
        if (hasEducation) score += 6
        if (hasSkills) score += 6
        if (hasSummary) score += 4

        // Certs & Projects (max 4)
        if (hasCerts) score += 2
        if (hasProjects) score += 2

        // Action Verbs (max 16)
        score += when {
            foundVerbsCount >= 12 -> 16
            foundVerbsCount >= 8 -> 12
            foundVerbsCount >= 5 -> 8
            foundVerbsCount >= 3 -> 4
            else -> 0
        }

        // Metrics (max 18) - CRUCIAL
        score += when {
            metricsCount == 4 -> 18
            metricsCount == 3 -> 14
            metricsCount == 2 -> 9
            metricsCount == 1 -> 4
            else -> 0
        }

        // Bonus for timeline and performance metrics
        if (hasTimelineMetrics && hasPerformanceMetrics) score += 2

        // Tech Keywords (max 22)
        score += when {
            foundTechCount >= 15 -> 22
            foundTechCount >= 12 -> 18
            foundTechCount >= 9 -> 15
            foundTechCount >= 6 -> 11
            foundTechCount >= 3 -> 6
            foundTechCount >= 1 -> 2
            else -> 0
        }

        // Soft Skills (max 6)
        score += when {
            foundSoftCount >= 5 -> 6
            foundSoftCount >= 3 -> 4
            foundSoftCount >= 1 -> 2
            else -> 0
        }

        // Industry Keywords (max 3)
        score += when {
            foundIndustryCount >= 3 -> 3
            foundIndustryCount >= 1 -> 1
            else -> 0
        }

        // Length (max 5) - ideal 400-1200
        score += when {
            wordCount in 400..1200 -> 5
            wordCount in 300..399 -> 3
            wordCount in 200..299 -> 1
            else -> 0
        }

        val finalScore = score.coerceIn(25, 90)

        // ==================== RESUME-SPECIFIC SUGGESTIONS ====================
        val suggestions = mutableListOf<Suggestion>()
        var id = 1

        // Contact Info - Resume specific
        if (!hasEmail || !hasPhone) {
            val missing = mutableListOf<String>()
            if (!hasEmail) missing.add("professional email")
            if (!hasPhone) missing.add("phone number")
            suggestions.add(Suggestion("s_$id", SuggestionCategory.CONTACT_INFO,
                "Complete Contact Information",
                "Missing: ${missing.joinToString(", ")}. Format: firstname.lastname@domain.com", Priority.HIGH))
            id++
        }

        if (!hasLinkedIn && !hasGitHub && !hasPortfolio) {
            suggestions.add(
                Suggestion(
                    "s_$id",
                    SuggestionCategory.CONTACT_INFO,
                    "Add Professional Online Profiles",
                    "Include: LinkedIn (linkedin.com/in/yourname), GitHub (github.com/username), or Portfolio (yourname.github.io)",
                    Priority.MEDIUM
                )
            )
            id++
        } else if (hasLinkedIn && !hasGitHub && !hasPortfolio) {
            suggestions.add(
                Suggestion(
                    "s_$id",
                    SuggestionCategory.CONTACT_INFO,
                    "Add GitHub or Portfolio Link",
                    "Include your GitHub (github.com/username) or a personal portfolio to showcase your projects",
                    Priority.MEDIUM
                )
            )
            id++
        } else if (!hasLinkedIn && hasGitHub && !hasPortfolio) {
            suggestions.add(
                Suggestion(
                    "s_$id",
                    SuggestionCategory.CONTACT_INFO,
                    "Add LinkedIn or Portfolio",
                    "LinkedIn (linkedin.com/in/yourname) and a personal portfolio site improve professional visibility",
                    Priority.MEDIUM
                )
            )
            id++
        } else if (!hasLinkedIn && !hasGitHub && hasPortfolio) {
            suggestions.add(
                Suggestion(
                    "s_$id",
                    SuggestionCategory.CONTACT_INFO,
                    "Add LinkedIn or Github",
                    "LinkedIn (linkedin.com/in/yourname) and GitHub (github.com/username) to improve professional visibility",
                    Priority.MEDIUM
                )
            )
            id++
        }
        else if (!hasLinkedIn) {
            suggestions.add(
                Suggestion(
                    "s_$id",
                    SuggestionCategory.CONTACT_INFO,
                    "Add a LinkedIn account",
                    "Adding your LinkedIn profile helps recruiters verify your experience, see endorsements, and understand your professional journey better."
                    ,
                    Priority.MEDIUM
                )
            )
            id++
        }
        else if (!hasPortfolio) {
            suggestions.add(
                Suggestion(
                    "s_$id",
                    SuggestionCategory.CONTACT_INFO,
                    "Add a Personal Portfolio",
                    "A portfolio website (e.g., yourname.github.io or yourname.vercel.app) highlights your projects and achievements",
                    Priority.LOW
                )
            )
            id++
        }

        // Experience
        if (!hasExperience) {
            suggestions.add(Suggestion("s_$id", SuggestionCategory.EXPERIENCE,
                "Add Work Experience Section",
                "List positions with: Company, Role, Duration (dates), Key achievements and metrics", Priority.HIGH))
            id++
        } else if (foundVerbsCount < 5) {
            suggestions.add(Suggestion("s_$id", SuggestionCategory.CONTENT,
                "Strengthen Action Verbs (Current: $foundVerbsCount, Target: 8+)",
                "Use: Led, Architected, Optimized, Transformed, Innovated instead of: did, worked, was responsible for", Priority.HIGH))
            id++
        } else if (foundVerbsCount < 8) {
            suggestions.add(Suggestion("s_$id", SuggestionCategory.CONTENT,
                "Add More Action Verbs (Current: $foundVerbsCount, Target: 12+)",
                "Include more impactful verbs to demonstrate leadership and impact", Priority.MEDIUM))
            id++
        }

        // Education
        if (!hasEducation) {
            suggestions.add(Suggestion("s_$id", SuggestionCategory.STRUCTURE,
                "Add Education Section",
                "Include: Degree, University, Graduation Year, GPA (if 3.7+), Relevant Coursework", Priority.HIGH))
            id++
        }

        // Skills
        if (!hasSkills) {
            suggestions.add(Suggestion("s_$id", SuggestionCategory.SKILLS,
                "Create Skills Section",
                "Organize 25-35 skills by: Languages, Frameworks, Databases, DevOps, Tools, Soft Skills", Priority.HIGH))
            id++
        } else if (foundTechCount < 6) {
            suggestions.add(Suggestion("s_$id", SuggestionCategory.SKILLS,
                "Expand Technical Keywords (Current: $foundTechCount, Target: 12+)",
                "Add: Docker, Kubernetes, AWS, Jenkins, CI/CD, REST APIs, Git, SQL, Cloud platforms", Priority.HIGH))
            id++
        } else if (foundTechCount < 12) {
            suggestions.add(Suggestion("s_$id", SuggestionCategory.SKILLS,
                "Add More Technical Keywords (Current: $foundTechCount, Target: 15+)",
                "Include specific tools, frameworks, and technologies from your experience", Priority.MEDIUM))
            id++
        }

        // Metrics
        if (metricsCount == 0) {
            suggestions.add(Suggestion("s_$id", SuggestionCategory.CONTENT,
                "Add Quantifiable Metrics - CRITICAL for ATS",
                "Essential: Percentages (35% improvement), Money ($250K), Team size (Led 7), Years (2+ years)", Priority.HIGH))
            id++
        } else if (metricsCount < 3) {
            val missing = mutableListOf<String>()
            if (!hasPercent) missing.add("percentage improvements")
            if (!hasMoney) missing.add("monetary values")
            if (!hasTeam) missing.add("team sizes")
            if (!hasYears) missing.add("years of experience")
            suggestions.add(Suggestion("s_$id", SuggestionCategory.CONTENT,
                "Add More Quantifiable Metrics (Current: $metricsCount/4)",
                "Missing: ${missing.joinToString(", ")}. Be specific with numbers and units", Priority.HIGH))
            id++
        }

        // Summary
        if (!hasSummary) {
            suggestions.add(Suggestion("s_$id", SuggestionCategory.STRUCTURE,
                "Add Professional Summary",
                "Write 2-3 sentences: Years of experience, key expertise, career focus, unique value", Priority.MEDIUM))
            id++
        }

        // Soft Skills
        if (foundSoftCount < 3 && hasExperience) {
            suggestions.add(Suggestion("s_$id", SuggestionCategory.CONTENT,
                "Highlight Soft Skills (Current: $foundSoftCount, Target: 5+)",
                "Add: Leadership, Communication, Teamwork, Problem-solving, Strategic thinking", Priority.MEDIUM))
            id++
        }

        // Formatting
        suggestions.add(Suggestion("s_$id", SuggestionCategory.FORMATTING,
            "Use ATS-Optimized Formatting",
            "Avoid: Tables, images, graphics, fancy fonts. Use: Standard fonts, bullet points, plain text", Priority.MEDIUM))
        id++

        suggestions.add(Suggestion("s_$id", SuggestionCategory.FORMATTING,
            "Standardize Section Headers",
            "Use: Professional Summary, Work Experience, Education, Skills, Certifications, Projects", Priority.MEDIUM))

        // ==================== STRENGTHS & WEAKNESSES ====================
        val strengths = mutableListOf<String>()

        if (hasEmail && hasPhone && (hasLinkedIn || hasGitHub)) strengths.add("Complete contact information with online profiles")
        //if (hasLinkedIn && hasGitHub) strengths.add("Strong online presence (both LinkedIn and GitHub)")
        if (hasLinkedIn && hasGitHub && hasPortfolio) strengths.add("Strong online presence (LinkedIn, GitHub, and Portfolio)")
        if (hasPortfolio) strengths.add("Portfolio link included – showcases personal or creative work")
        if (hasExperience && hasEducation && hasSkills) strengths.add("Well-structured with all core sections")
        if (foundVerbsCount >= 10) strengths.add("Excellent use of action verbs ($foundVerbsCount found)")
        if (metricsCount >= 3) strengths.add("Strong quantifiable metrics usage ($metricsCount metrics)")
        if (hasTimelineMetrics && hasPerformanceMetrics) strengths.add("Demonstrates measurable business impact")
        if (foundTechCount >= 12) strengths.add("Comprehensive tech stack coverage ($foundTechCount keywords)")
        if (foundSoftCount >= 3) strengths.add("Clear soft skills demonstration ($foundSoftCount skills)")
        if (hasSummary && hasExperience) strengths.add("Professional summary and clear experience")
        if (wordCount in 400..1200) strengths.add("Appropriate resume length ($wordCount words)")
        if (hasProjects) strengths.add("Portfolio or projects section included")

        if (strengths.isEmpty()) strengths.add("Resume has basic structure")

        val weaknesses = mutableListOf<String>()

        if (!hasEmail || !hasPhone) weaknesses.add("Missing critical contact information")
        if (!hasLinkedIn && !hasGitHub) weaknesses.add("No online professional profiles")
        if (!hasLinkedIn) weaknesses.add("Missing LinkedIn profile — add it to strengthen your professional presence")
        //if (!hasLinkedIn && !hasGitHub && !hasPortfolio) weaknesses.add("No online professional profiles or portfolio link")
        if (!hasPortfolio) weaknesses.add("No portfolio link to showcase personal work or projects")
        if (!hasExperience) weaknesses.add("No work experience section")
        if (!hasEducation) weaknesses.add("Missing education credentials")
        if (!hasSkills) weaknesses.add("No dedicated skills section")
        if (foundTechCount < 6) weaknesses.add("Low technical keyword coverage ($foundTechCount, need 12+)")
        if (metricsCount == 0) weaknesses.add("No quantifiable metrics - major ATS blocker")
        if (foundVerbsCount < 5) weaknesses.add("Weak action verbs ($foundVerbsCount used, need 8+)")
        if (!hasSummary) weaknesses.add("Missing professional summary")
        if (wordCount < 300) weaknesses.add("Too short ($wordCount words, minimum 300)")
        if (wordCount > 1500) weaknesses.add("Too long ($wordCount words, maximum 1200)")
        if (foundSoftCount < 2) weaknesses.add("Limited soft skills demonstration")

        if (weaknesses.isEmpty()) weaknesses.add("Resume is well-optimized")

        // ==================== DETAILED REPORT ====================
        val report = buildString {
            append("SCORE: $finalScore/100\n")
            append("─".repeat(50) + "\n")
            append(when {
                finalScore >= 80 -> "Status: 🟢 EXCELLENT - Ready for Submission"
                finalScore >= 70 -> "Status: 🟢 GOOD - Minor improvements recommended"
                finalScore >= 60 -> "Status: 🟡 FAIR - Several improvements needed"
                else -> "Status: 🔴 NEEDS WORK - Significant changes required"
            } + "\n\n")
            append("ANALYSIS BREAKDOWN\n")
            append("─".repeat(50) + "\n")
            append("✓ Contact Info: ${if (hasEmail && hasPhone) "Complete" else "Incomplete"}\n")
            append("✓ Experience: ${if (hasExperience) "Present" else "Missing"}\n")
            append("✓ Education: ${if (hasEducation) "Present" else "Missing"}\n")
            append("✓ Skills: ${if (hasSkills) "Present ($foundTechCount keywords)" else "Missing"}\n")
            append("✓ Summary: ${if (hasSummary) "Present" else "Missing"}\n\n")
            append("CONTENT METRICS\n")
            append("─".repeat(50) + "\n")
            append("Action Verbs: $foundVerbsCount (Target: 12+)\n")
            append("Tech Keywords: $foundTechCount (Target: 15+)\n")
            append("Soft Skills: $foundSoftCount (Target: 5+)\n")
            append("Quantifiable Metrics: $metricsCount/4\n")
            append("Word Count: $wordCount (Ideal: 400-1200)\n")
        }

        // ==================== SCORE BREAKDOWN ====================
        val breakdown = ScoreBreakdown(
            formatScore = when {
                wordCount in 300..1200 -> 80
                wordCount in 200..299 -> 60
                wordCount > 1500 -> 50
                else -> 40
            },
            keywordScore = when {
                foundTechCount >= 15 -> 85
                foundTechCount >= 12 -> 80
                foundTechCount >= 9 -> 70
                foundTechCount >= 6 -> 60
                foundTechCount >= 3 -> 45
                else -> 25
            },
            contentScore = when {
                foundVerbsCount >= 10 && metricsCount >= 3 -> 85
                foundVerbsCount >= 8 && metricsCount >= 2 -> 75
                foundVerbsCount >= 5 || metricsCount >= 2 -> 65
                else -> 40
            },
            structureScore = when {
                hasExperience && hasEducation && hasSkills && hasSummary -> 90
                hasExperience && hasEducation && hasSkills -> 75
                hasExperience && hasEducation -> 60
                else -> 40
            },
            contactScore = when {
                hasEmail && hasPhone && (hasLinkedIn || hasGitHub) -> 95
                hasEmail && hasPhone -> 85
                hasEmail -> 60
                else -> 30
            }
        )

        Log.d(TAG, "✅ Analysis complete - Score: $finalScore")

        return ATSAnalysisResult(
            resumeId = resumeId,
            overallScore = finalScore,
            extractedText = text,
            breakdown = breakdown,
            suggestions = suggestions.take(18),
            keywords = KeywordAnalysis(
                foundKeywords = foundTechList.ifEmpty { listOf("Communication", "Teamwork") },
                missingKeywords = listOf("Docker", "Kubernetes", "AWS", "CI/CD", "Jenkins", "REST APIs", "Git", "Microservices"),
                keywordDensity = (foundTechCount.toFloat() / wordCount * 100).coerceIn(0f, 15f)
            ),
            sections = SectionAnalysis(
                hasContactInfo = hasEmail,
                hasObjective = hasSummary,
                hasExperience = hasExperience,
                hasEducation = hasEducation,
                hasSkills = hasSkills,
                hasCertifications = hasCerts
            ),
            strengths = strengths,
            weaknesses = weaknesses,
            detailedReport = report,
            analyzedBy = "Advanced ATS Parser",
            analyzedAt = System.currentTimeMillis()
        )
    }
}