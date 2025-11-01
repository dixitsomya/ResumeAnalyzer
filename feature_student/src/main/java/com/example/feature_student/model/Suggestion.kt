//package com.example.feature_student.model
//
//data class Suggestion(
//    val id: String,
//    val category: SuggestionCategory,
//    val title: String,
//    val description: String,
//    val priority: Priority,
//    val isFixed: Boolean = false
//)
//
//enum class SuggestionCategory {
//    FORMATTING,
//    CONTENT,
//    KEYWORDS,
//    STRUCTURE,
//    CONTACT_INFO,
//    EXPERIENCE,
//    SKILLS
//}
//
//enum class Priority {
//    HIGH,
//    MEDIUM,
//    LOW
//}
//
//// Sample suggestions generator
//object SuggestionGenerator {
//    fun generateSuggestions(atsScore: Int): List<Suggestion> {
//        val suggestions = mutableListOf<Suggestion>()
//
//        if (atsScore < 70) {
//            suggestions.add(
//                Suggestion(
//                    id = "1",
//                    category = SuggestionCategory.KEYWORDS,
//                    title = "Add More Industry Keywords",
//                    description = "Your resume lacks relevant industry keywords. Add skills like 'Project Management', 'Team Leadership', or technical skills relevant to your field.",
//                    priority = Priority.HIGH
//                )
//            )
//        }
//
//        if (atsScore < 80) {
//            suggestions.add(
//                Suggestion(
//                    id = "2",
//                    category = SuggestionCategory.FORMATTING,
//                    title = "Improve Resume Formatting",
//                    description = "Use consistent fonts, proper bullet points, and clear section headers. ATS systems prefer clean, simple formatting.",
//                    priority = Priority.HIGH
//                )
//            )
//
//            suggestions.add(
//                Suggestion(
//                    id = "3",
//                    category = SuggestionCategory.CONTENT,
//                    title = "Quantify Your Achievements",
//                    description = "Use numbers and metrics. Instead of 'Managed team', write 'Managed team of 10+ members, increasing productivity by 25%'.",
//                    priority = Priority.MEDIUM
//                )
//            )
//        }
//
//        suggestions.add(
//            Suggestion(
//                id = "4",
//                category = SuggestionCategory.STRUCTURE,
//                title = "Add Professional Summary",
//                description = "Include a 2-3 line professional summary at the top highlighting your key skills and experience.",
//                priority = if (atsScore < 75) Priority.HIGH else Priority.MEDIUM
//            )
//        )
//
//        suggestions.add(
//            Suggestion(
//                id = "5",
//                category = SuggestionCategory.CONTACT_INFO,
//                title = "Update Contact Information",
//                description = "Ensure your email, phone number, and LinkedIn profile are clearly visible at the top of your resume.",
//                priority = Priority.MEDIUM
//            )
//        )
//
//        suggestions.add(
//            Suggestion(
//                id = "6",
//                category = SuggestionCategory.EXPERIENCE,
//                title = "Use Action Verbs",
//                description = "Start bullet points with strong action verbs like 'Developed', 'Implemented', 'Led', 'Optimized' instead of 'Responsible for'.",
//                priority = Priority.MEDIUM
//            )
//        )
//
//        if (atsScore < 85) {
//            suggestions.add(
//                Suggestion(
//                    id = "7",
//                    category = SuggestionCategory.SKILLS,
//                    title = "Create a Dedicated Skills Section",
//                    description = "List both technical and soft skills in a separate section. Include programming languages, tools, and certifications.",
//                    priority = Priority.HIGH
//                )
//            )
//        }
//
//        suggestions.add(
//            Suggestion(
//                id = "8",
//                category = SuggestionCategory.CONTENT,
//                title = "Remove Personal Pronouns",
//                description = "Avoid using 'I', 'me', 'my' in your resume. Write in third person or omit pronouns entirely.",
//                priority = Priority.LOW
//            )
//        )
//
//        return suggestions
//    }
//}