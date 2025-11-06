//package com.example.feature_recruiter.util
//
//object TechStackExtractor {
//
//    private val TECH_KEYWORDS = mapOf(
//        // Languages
//        "kotlin" to "Kotlin",
//        "java" to "Java",
//        "python" to "Python",
//        "javascript" to "JavaScript",
//        "typescript" to "TypeScript",
//        "swift" to "Swift",
//        "go" to "Go",
//        "rust" to "Rust",
//        "cpp" to "C++",
//        "csharp" to "C#",
//        "c#" to "C#",
//
//        // Mobile
//        "android" to "Android",
//        "ios" to "iOS",
//        "flutter" to "Flutter",
//        "react native" to "React Native",
//
//        // Frontend
//        "react" to "React",
//        "vue" to "Vue",
//        "angular" to "Angular",
//        "html" to "HTML",
//        "css" to "CSS",
//        "jetpack compose" to "Jetpack Compose",
//
//        // Backend
//        "spring" to "Spring",
//        "node" to "Node.js",
//        "django" to "Django",
//        "flask" to "Flask",
//        "express" to "Express",
//
//        // Databases
//        "firebase" to "Firebase",
//        "mysql" to "MySQL",
//        "postgresql" to "PostgreSQL",
//        "mongodb" to "MongoDB",
//        "sqlite" to "SQLite",
//        "realm" to "Realm",
//
//        // Tools & Frameworks
//        "git" to "Git",
//        "docker" to "Docker",
//        "kubernetes" to "Kubernetes",
//        "aws" to "AWS",
//        "gcp" to "GCP",
//        "azure" to "Azure",
//        "jenkins" to "Jenkins",
//        "gradle" to "Gradle",
//        "maven" to "Maven",
//        "api" to "API",
//        "rest" to "REST",
//        "graphql" to "GraphQL"
//    )
//
//    fun extractTechStack(text: String): List<String> {
//        if (text.isBlank()) return emptyList()
//
//        val lowerText = text.lowercase()
//        val foundTechs = mutableSetOf<String>()
//
//        for ((keyword, display) in TECH_KEYWORDS) {
//            if (keyword in lowerText) {
//                foundTechs.add(display)
//            }
//        }
//
//        return foundTechs.sorted()
//    }
//
//    fun extractFromMultipleTexts(texts: List<String>): List<String> {
//        return texts
//            .flatMap { extractTechStack(it) }
//            .distinct()
//            .sorted()
//    }
//}


package com.example.feature_recruiter.util

object TechStackExtractor {

    // ✅ Organized by category
    private val TECH_KEYWORDS = mapOf(
        // Languages
        "kotlin" to "Kotlin",
        "java" to "Java",
        "python" to "Python",
        "javascript" to "JavaScript",
        "typescript" to "TypeScript",
        "swift" to "Swift",
        "go" to "Go",
        "rust" to "Rust",
        "cpp" to "C++",
        "csharp" to "C#",
        "c#" to "C#",

        // Mobile
        "android" to "Android",
        "ios" to "iOS",
        "flutter" to "Flutter",
        "react native" to "React Native",

        // Frontend
        "react" to "React",
        "vue" to "Vue",
        "angular" to "Angular",
        "html" to "HTML",
        "css" to "CSS",
        "jetpack compose" to "Jetpack Compose",

        // Backend
        "spring" to "Spring",
        "node" to "Node.js",
        "django" to "Django",
        "flask" to "Flask",
        "express" to "Express",

        // Databases
        "firebase" to "Firebase",
        "mysql" to "MySQL",
        "postgresql" to "PostgreSQL",
        "mongodb" to "MongoDB",
        "sqlite" to "SQLite",
        "realm" to "Realm",

        // Tools & Frameworks
        "git" to "Git",
        "docker" to "Docker",
        "kubernetes" to "Kubernetes",
        "aws" to "AWS",
        "gcp" to "GCP",
        "azure" to "Azure",
        "jenkins" to "Jenkins",
        "gradle" to "Gradle",
        "maven" to "Maven",
        "api" to "API",
        "rest" to "REST",
        "graphql" to "GraphQL"
    )

    // ✅ Categorized tech stacks
    data class CategorizedTechStack(
        val languages: List<String> = emptyList(),
        val mobile: List<String> = emptyList(),
        val frontend: List<String> = emptyList(),
        val backend: List<String> = emptyList(),
        val databases: List<String> = emptyList(),
        val tools: List<String> = emptyList()
    )

    fun extractTechStack(text: String): List<String> {
        if (text.isBlank()) return emptyList()

        val lowerText = text.lowercase()
        val foundTechs = mutableSetOf<String>()

        for ((keyword, display) in TECH_KEYWORDS) {
            if (keyword in lowerText) {
                foundTechs.add(display)
            }
        }

        return foundTechs.sorted()
    }

    fun extractCategorizedTechStack(text: String): CategorizedTechStack {
        if (text.isBlank()) return CategorizedTechStack()

        val lowerText = text.lowercase()

        val languages = mutableListOf<String>()
        val mobile = mutableListOf<String>()
        val frontend = mutableListOf<String>()
        val backend = mutableListOf<String>()
        val databases = mutableListOf<String>()
        val tools = mutableListOf<String>()

        // Languages
        listOf("kotlin", "java", "python", "javascript", "typescript", "swift", "go", "rust", "cpp", "csharp", "c#").forEach { keyword ->
            if (keyword in lowerText) {
                languages.add(TECH_KEYWORDS[keyword] ?: keyword)
            }
        }

        // Mobile
        listOf("android", "ios", "flutter", "react native").forEach { keyword ->
            if (keyword in lowerText) {
                mobile.add(TECH_KEYWORDS[keyword] ?: keyword)
            }
        }

        // Frontend
        listOf("react", "vue", "angular", "html", "css", "jetpack compose").forEach { keyword ->
            if (keyword in lowerText) {
                frontend.add(TECH_KEYWORDS[keyword] ?: keyword)
            }
        }

        // Backend
        listOf("spring", "node", "django", "flask", "express").forEach { keyword ->
            if (keyword in lowerText) {
                backend.add(TECH_KEYWORDS[keyword] ?: keyword)
            }
        }

        // Databases
        listOf("firebase", "mysql", "postgresql", "mongodb", "sqlite", "realm").forEach { keyword ->
            if (keyword in lowerText) {
                databases.add(TECH_KEYWORDS[keyword] ?: keyword)
            }
        }

        // Tools
        listOf("git", "docker", "kubernetes", "aws", "gcp", "azure", "jenkins", "gradle", "maven", "api", "rest", "graphql").forEach { keyword ->
            if (keyword in lowerText) {
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

    fun extractFromMultipleTexts(texts: List<String>): List<String> {
        return texts
            .flatMap { extractTechStack(it) }
            .distinct()
            .sorted()
    }
}