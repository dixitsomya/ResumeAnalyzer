package com.example.feature_recruiter.util

object TechStackExtractor {

    // ✅ Keywords with multiple variations
    private val TECH_KEYWORDS = mapOf(
        // Languages
        "kotlin" to "Kotlin",
        "java" to "Java",
        "python" to "Python",
        "javascript" to "JavaScript",
        "js" to "JavaScript",
        "typescript" to "TypeScript",
        "ts" to "TypeScript",
        "swift" to "Swift",
        "go" to "Go",
        "golang" to "Go",
        "rust" to "Rust",
        "c++" to "C++",
        "cpp" to "C++",
        "csharp" to "C#",
        "c#" to "C#",

        // Mobile
        "android" to "Android",
        "ios" to "iOS",
        "flutter" to "Flutter",
        "react native" to "React Native",
        "reactnative" to "React Native",

        // Frontend
        "react" to "React",
        "reactjs" to "React",
        "vue" to "Vue",
        "vuejs" to "Vue",
        "angular" to "Angular",
        "angularjs" to "Angular",
        "html" to "HTML",
        "html5" to "HTML",
        "css" to "CSS",
        "css3" to "CSS",
        "jetpack compose" to "Jetpack Compose",
        "jetpackcompose" to "Jetpack Compose",

        // Backend
        "spring" to "Spring",
        "springboot" to "Spring",
        "spring boot" to "Spring",
        "node" to "Node.js",
        "nodejs" to "Node.js",
        "node.js" to "Node.js",
        "django" to "Django",
        "flask" to "Flask",
        "express" to "Express",
        "expressjs" to "Express",

        // Databases
        "firebase" to "Firebase",
        "mysql" to "MySQL",
        "postgresql" to "PostgreSQL",
        "postgres" to "PostgreSQL",
        "mongodb" to "MongoDB",
        "mongo" to "MongoDB",
        "sqlite" to "SQLite",
        "realm" to "Realm",
        "oracle" to "Oracle",

        // Tools & Frameworks
        "git" to "Git",
        "docker" to "Docker",
        "kubernetes" to "Kubernetes",
        "k8s" to "Kubernetes",
        "aws" to "AWS",
        "amazon" to "AWS",
        "gcp" to "GCP",
        "google cloud" to "GCP",
        "azure" to "Azure",
        "jenkins" to "Jenkins",
        "gradle" to "Gradle",
        "maven" to "Maven",
        "api" to "API",
        "rest" to "REST",
        "restful" to "REST",
        "graphql" to "GraphQL",
        "graphql" to "GraphQL"
    )

    /**
     * ✅ Extract tech stack from text with better parsing
     * Handles PDF text extraction issues
     */
    private val sortedKeywords = TECH_KEYWORDS.keys.sortedByDescending { it.length }

    fun extractTechStack(text: String): List<String> {
        if (text.isBlank()) return emptyList()

        val content = text.lowercase()
            .replace("\n", " ")
            .replace("\t", " ")
            .replace(Regex("\\s+"), " ")

        val found = mutableSetOf<String>()

        for (keyword in sortedKeywords) {
            val pattern = Regex("\\b${Regex.escape(keyword)}\\b", RegexOption.IGNORE_CASE)
            if (pattern.containsMatchIn(content)) {
                found.add(TECH_KEYWORDS[keyword] ?: keyword)
            }
        }

        return found.toList().distinct()
    }


    /**
     * ✅ Check if keyword is present as whole word
     * Prevents partial matches like "java" in "javascript"
     */
    private fun isKeywordPresent(text: String, keyword: String): Boolean {
        // Create pattern that matches keyword surrounded by word boundaries
        val patterns = listOf(
            Regex("\\b$keyword\\b", RegexOption.IGNORE_CASE),
            Regex("\\s$keyword\\s", RegexOption.IGNORE_CASE),
            Regex("^$keyword\\s", RegexOption.IGNORE_CASE),
            Regex("\\s$keyword$", RegexOption.IGNORE_CASE),
            Regex("^$keyword$", RegexOption.IGNORE_CASE),
            Regex("[,\\-\\(\\)\\.]$keyword[,\\-\\(\\)\\.]", RegexOption.IGNORE_CASE)
        )

        return patterns.any { it.containsMatchIn(text) }
    }

    /**
     * ✅ Categorized tech stacks
     */
    data class CategorizedTechStack(
        val languages: List<String> = emptyList(),
        val mobile: List<String> = emptyList(),
        val frontend: List<String> = emptyList(),
        val backend: List<String> = emptyList(),
        val databases: List<String> = emptyList(),
        val tools: List<String> = emptyList()
    )

    /**
     * ✅ Extract and categorize tech stack
     */
    fun extractCategorizedTechStack(text: String): CategorizedTechStack {
        if (text.isBlank()) return CategorizedTechStack()

        val lowerText = text.lowercase()

        val languages = mutableListOf<String>()
        val mobile = mutableListOf<String>()
        val frontend = mutableListOf<String>()
        val backend = mutableListOf<String>()
        val databases = mutableListOf<String>()
        val tools = mutableListOf<String>()

        // ✅ Languages
        listOf("kotlin", "java", "python", "javascript", "js", "typescript", "ts", "swift", "go", "golang", "rust", "c++", "cpp", "csharp", "c#")
            .forEach { keyword ->
                if (isKeywordPresent(lowerText, keyword)) {
                    languages.add(TECH_KEYWORDS[keyword] ?: keyword)
                }
            }

        // ✅ Mobile
        listOf("android", "ios", "flutter", "react native", "reactnative")
            .forEach { keyword ->
                if (isKeywordPresent(lowerText, keyword)) {
                    mobile.add(TECH_KEYWORDS[keyword] ?: keyword)
                }
            }

        // ✅ Frontend
        listOf("react", "reactjs", "vue", "vuejs", "angular", "angularjs", "html", "html5", "css", "css3", "jetpack compose", "jetpackcompose")
            .forEach { keyword ->
                if (isKeywordPresent(lowerText, keyword)) {
                    frontend.add(TECH_KEYWORDS[keyword] ?: keyword)
                }
            }

        // ✅ Backend
        listOf("spring", "springboot", "spring boot", "node", "nodejs", "node.js", "django", "flask", "express", "expressjs")
            .forEach { keyword ->
                if (isKeywordPresent(lowerText, keyword)) {
                    backend.add(TECH_KEYWORDS[keyword] ?: keyword)
                }
            }

        // ✅ Databases
        listOf("firebase", "mysql", "postgresql", "postgres", "mongodb", "mongo", "sqlite", "realm", "oracle")
            .forEach { keyword ->
                if (isKeywordPresent(lowerText, keyword)) {
                    databases.add(TECH_KEYWORDS[keyword] ?: keyword)
                }
            }

        // ✅ Tools
        listOf("git", "docker", "kubernetes", "k8s", "aws", "amazon", "gcp", "google cloud", "azure", "jenkins", "gradle", "maven", "api", "rest", "restful", "graphql")
            .forEach { keyword ->
                if (isKeywordPresent(lowerText, keyword)) {
                    tools.add(TECH_KEYWORDS[keyword] ?: keyword)
                }
            }

        return CategorizedTechStack(
            languages = languages.distinct(),
            mobile = mobile.distinct(),
            frontend = frontend.distinct(),
            backend = backend.distinct(),
            databases = databases.distinct(),
            tools = tools.distinct()
        )
    }

    /**
     * ✅ Extract from multiple texts
     */
    fun extractFromMultipleTexts(texts: List<String>): List<String> {
        return texts
            .flatMap { extractTechStack(it) }
            .distinct()
            .sorted()
    }

    /**
     * ✅ Debug function - shows what was found
     */
    fun debugExtractTechStack(text: String): Map<String, List<Any>> {
        return mapOf(
            "extractedTechs" to extractTechStack(text),
            "categorized" to listOfNotNull(
                extractCategorizedTechStack(text).let { cat ->
                    if (cat.languages.isNotEmpty()) cat.languages else null
                }
            )
        )
    }

    private val aliases = mapOf(
        "js" to "javascript",
        "ts" to "typescript",
        "react.js" to "react",
        "node" to "node.js",
        "mongo" to "mongodb",
        "postgres" to "postgresql",
        "compose" to "jetpack compose",
        "gcp" to "google cloud", "aws" to "amazon web services"
    )
    private val dictionary = setOf(
        "kotlin","java","python","javascript","typescript","swift","go","rust","c++","c#",
        "android","ios","flutter","react native","react","vue","angular","html","css","jetpack compose",
        "spring","spring boot","node.js","django","flask","express",
        "firebase","mysql","postgresql","mongodb","sqlite","realm",
        "git","docker","kubernetes","aws","gcp","azure","jenkins","gradle","maven","rest","graphql","api"
    )
//    fun extractTechStack(text: String): List<String> {
//        val t = text.lowercase()
//        val found = mutableSetOf<String>()
//        dictionary.forEach { key ->
//            if (t.contains(key)) found.add(key)
//        }
//        // normalize aliases
//        return found.map { aliases[it] ?: it }.distinct()
//    }
}
