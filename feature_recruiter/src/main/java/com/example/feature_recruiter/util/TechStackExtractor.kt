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

    fun extractFromMultipleTexts(texts: List<String>): List<String> {
        return texts
            .flatMap { extractTechStack(it) }
            .distinct()
            .sorted()
    }
}