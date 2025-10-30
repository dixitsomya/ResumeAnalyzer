//
//package com.example.feature_student.suggestions
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.feature_student.model.Priority
//import com.example.feature_student.model.Suggestion
//import com.example.feature_student.model.SuggestionCategory
//
//@Composable
//fun SuggestionsScreen(
//    modifier: Modifier = Modifier,
//    isDark: Boolean,
//    viewModel: SuggestionsViewModel = viewModel()
//) {
//    val suggestions by viewModel.suggestions.collectAsState()
//    val hasResume by viewModel.hasResume.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.refresh()
//    }
//
//    val backgroundBrush = if (isDark) {
//        Brush.verticalGradient(
//            listOf(Color(0xFF1a1a2e), Color(0xFF16213e))
//        )
//    } else {
//        Brush.verticalGradient(
//            listOf(Color(0xFFf0f4f8), Color(0xFFe4e9f2))
//        )
//    }
//
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .background(backgroundBrush)
//    ) {
//        if (!hasResume) {
//            EmptySuggestionsState(isDark = isDark)
//        } else {
//            SuggestionsContent(
//                suggestions = suggestions,
//                isDark = isDark,
//                onToggleFix = { viewModel.toggleSuggestionFixed(it) }
//            )
//        }
//    }
//}
//
//@Composable
//fun EmptySuggestionsState(isDark: Boolean) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Icon(
//            Icons.Default.Lightbulb,
//            contentDescription = null,
//            modifier = Modifier.size(100.dp),
//            tint = if (isDark) Color(0xFFFFD700).copy(0.5f) else Color(0xFFFFD700)
//        )
//
//        Spacer(Modifier.height(24.dp))
//
//        Text(
//            "No Suggestions Yet",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            color = if (isDark) Color.White else Color(0xFF1a1a2e)
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        Text(
//            "Upload and analyze your resume first\nto get personalized improvement suggestions",
//            fontSize = 16.sp,
//            color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
//            textAlign = androidx.compose.ui.text.style.TextAlign.Center
//        )
//    }
//}
//
//@Composable
//fun SuggestionsContent(
//    suggestions: List<Suggestion>,
//    isDark: Boolean,
//    onToggleFix: (String) -> Unit
//) {
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(20.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        contentPadding = PaddingValues(bottom = 20.dp)
//    ) {
//        item {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
//                ),
//                elevation = CardDefaults.cardElevation(12.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(20.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(60.dp)
//                            .clip(CircleShape)
//                            .background(
//                                Brush.radialGradient(
//                                    listOf(Color(0xFFFFD700), Color(0xFFFF8C00))
//                                )
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Icon(
//                            Icons.Default.TipsAndUpdates,
//                            contentDescription = null,
//                            tint = Color.White,
//                            modifier = Modifier.size(32.dp)
//                        )
//                    }
//
//                    Spacer(Modifier.width(16.dp))
//
//                    Column {
//                        Text(
//                            "Improvement Tips",
//                            fontSize = 22.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = if (isDark) Color.White else Color.Black
//                        )
//                        Text(
//                            "${suggestions.size} suggestions found",
//                            fontSize = 14.sp,
//                            color = if (isDark) Color.White.copy(0.6f) else Color.Gray
//                        )
//                    }
//                }
//            }
//        }
//
//        item {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                val highCount = suggestions.count { it.priority == Priority.HIGH }
//                val mediumCount = suggestions.count { it.priority == Priority.MEDIUM }
//                val lowCount = suggestions.count { it.priority == Priority.LOW }
//
//                PrioritySummaryCard(
//                    count = highCount,
//                    label = "High",
//                    color = Color(0xFFF44336),
//                    isDark = isDark,
//                    modifier = Modifier.weight(1f)
//                )
//                PrioritySummaryCard(
//                    count = mediumCount,
//                    label = "Medium",
//                    color = Color(0xFFFF9800),
//                    isDark = isDark,
//                    modifier = Modifier.weight(1f)
//                )
//                PrioritySummaryCard(
//                    count = lowCount,
//                    label = "Low",
//                    color = Color(0xFF4CAF50),
//                    isDark = isDark,
//                    modifier = Modifier.weight(1f)
//                )
//            }
//        }
//
//        items(suggestions) { suggestion ->
//            SuggestionCard(
//                suggestion = suggestion,
//                isDark = isDark,
//                onToggleFix = { onToggleFix(suggestion.id) }
//            )
//        }
//    }
//}
//
//@Composable
//fun SuggestionCard(
//    suggestion: Suggestion,
//    isDark: Boolean,
//    onToggleFix: () -> Unit
//) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
//        ),
//        elevation = CardDefaults.cardElevation(6.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Icon(
//                        getCategoryIcon(suggestion.category),
//                        contentDescription = null,
//                        tint = getCategoryColor(suggestion.category),
//                        modifier = Modifier.size(24.dp)
//                    )
//
//                    Spacer(Modifier.width(8.dp))
//
//                    Text(
//                        suggestion.category.name.replace("_", " "),
//                        fontSize = 12.sp,
//                        color = getCategoryColor(suggestion.category),
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                PriorityChip(priority = suggestion.priority)
//            }
//
//            Spacer(Modifier.height(12.dp))
//
//            Text(
//                suggestion.title,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                color = if (isDark) Color.White else Color.Black
//            )
//
//            Spacer(Modifier.height(8.dp))
//
//            Text(
//                suggestion.description,
//                fontSize = 14.sp,
//                color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
//                lineHeight = 20.sp
//            )
//
//            Spacer(Modifier.height(12.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.End,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                if (suggestion.isFixed) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            Icons.Default.CheckCircle,
//                            contentDescription = null,
//                            tint = Color(0xFF4CAF50),
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Spacer(Modifier.width(4.dp))
//                        Text(
//                            "Marked as Fixed",
//                            fontSize = 12.sp,
//                            color = Color(0xFF4CAF50),
//                            fontWeight = FontWeight.Medium
//                        )
//                    }
//                } else {
//                    TextButton(
//                        onClick = onToggleFix,
//                        colors = ButtonDefaults.textButtonColors(
//                            contentColor = Color(0xFF6C63FF)
//                        )
//                    ) {
//                        Icon(
//                            Icons.Default.Check,
//                            contentDescription = null,
//                            modifier = Modifier.size(18.dp)
//                        )
//                        Spacer(Modifier.width(4.dp))
//                        Text(
//                            "Mark as Fixed",
//                            fontSize = 13.sp,
//                            fontWeight = FontWeight.Medium
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun PriorityChip(priority: Priority) {
//    val (color, label) = when (priority) {
//        Priority.HIGH -> Pair(Color(0xFFF44336), "High")
//        Priority.MEDIUM -> Pair(Color(0xFFFF9800), "Medium")
//        Priority.LOW -> Pair(Color(0xFF4CAF50), "Low")
//    }
//
//    Surface(
//        shape = RoundedCornerShape(12.dp),
//        color = color.copy(alpha = 0.2f),
//        modifier = Modifier.border(1.dp, color, RoundedCornerShape(12.dp))
//    ) {
//        Text(
//            label,
//            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
//            fontSize = 11.sp,
//            color = color,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}
//
//@Composable
//fun PrioritySummaryCard(
//    count: Int,
//    label: String,
//    color: Color,
//    isDark: Boolean,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.height(80.dp),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
//        ),
//        elevation = CardDefaults.cardElevation(6.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(12.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                count.toString(),
//                fontSize = 28.sp,
//                fontWeight = FontWeight.Bold,
//                color = color
//            )
//            Text(
//                label,
//                fontSize = 12.sp,
//                color = if (isDark) Color.White.copy(0.7f) else Color.Gray
//            )
//        }
//    }
//}
//
//fun getCategoryIcon(category: SuggestionCategory): androidx.compose.ui.graphics.vector.ImageVector {
//    return when (category) {
//        SuggestionCategory.FORMATTING -> Icons.Default.FormatPaint
//        SuggestionCategory.CONTENT -> Icons.Default.Article
//        SuggestionCategory.KEYWORDS -> Icons.Default.Key
//        SuggestionCategory.STRUCTURE -> Icons.Default.AccountTree
//        SuggestionCategory.CONTACT_INFO -> Icons.Default.ContactMail
//        SuggestionCategory.EXPERIENCE -> Icons.Default.WorkHistory
//        SuggestionCategory.SKILLS -> Icons.Default.EmojiObjects
//    }
//}
//
//fun getCategoryColor(category: SuggestionCategory): Color {
//    return when (category) {
//        SuggestionCategory.FORMATTING -> Color(0xFF6C63FF)
//        SuggestionCategory.CONTENT -> Color(0xFFE91E63)
//        SuggestionCategory.KEYWORDS -> Color(0xFFFF9800)
//        SuggestionCategory.STRUCTURE -> Color(0xFF4CAF50)
//        SuggestionCategory.CONTACT_INFO -> Color(0xFF2196F3)
//        SuggestionCategory.EXPERIENCE -> Color(0xFF9C27B0)
//        SuggestionCategory.SKILLS -> Color(0xFFFFD700)
//    }
//}
//


package com.example.feature_student.suggestions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feature_student.model.Priority
import com.example.feature_student.model.Suggestion
import com.example.feature_student.model.SuggestionCategory

@Composable
fun SuggestionsScreen(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    viewModel: SuggestionsViewModel = viewModel()
) {
    val suggestions by viewModel.suggestions.collectAsState()
    val hasResume by viewModel.hasResume.collectAsState()
    val analysisResult by viewModel.analysisResult.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    val backgroundBrush = if (isDark) {
        Brush.verticalGradient(
            listOf(Color(0xFF1a1a2e), Color(0xFF16213e))
        )
    } else {
        Brush.verticalGradient(
            listOf(Color(0xFFf0f4f8), Color(0xFFe4e9f2))
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        if (!hasResume) {
            EmptySuggestionsState(isDark = isDark)
        } else {
            SuggestionsContent(
                suggestions = suggestions,
                overallScore = analysisResult?.overallScore ?: 0,
                isDark = isDark,
                onToggleFix = { viewModel.toggleSuggestionFixed(it) }
            )
        }
    }
}

@Composable
fun EmptySuggestionsState(isDark: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Lightbulb,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = if (isDark) Color(0xFFFFD700).copy(0.5f) else Color(0xFFFFD700)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            "No Suggestions Yet",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else Color(0xFF1a1a2e)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            "Upload and analyze your resume first\nto get personalized AI-powered suggestions",
            fontSize = 16.sp,
            color = if (isDark) Color.White.copy(0.6f) else Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun SuggestionsContent(
    suggestions: List<Suggestion>,
    overallScore: Int,
    isDark: Boolean,
    onToggleFix: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
                ),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(Color(0xFFFFD700), Color(0xFFFF8C00))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.TipsAndUpdates,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(Modifier.width(16.dp))

                            Column {
                                Text(
                                    "AI Suggestions",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else Color.Black
                                )
                                Text(
                                    "${suggestions.size} improvements found",
                                    fontSize = 14.sp,
                                    color = if (isDark) Color.White.copy(0.6f) else Color.Gray
                                )
                            }
                        }

                        // Score badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = getScoreColor(overallScore).copy(alpha = 0.2f),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                "$overallScore",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = getScoreColor(overallScore)
                            )
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val highCount = suggestions.count { it.priority == Priority.HIGH }
                val mediumCount = suggestions.count { it.priority == Priority.MEDIUM }
                val lowCount = suggestions.count { it.priority == Priority.LOW }

                PrioritySummaryCard(
                    count = highCount,
                    label = "High",
                    color = Color(0xFFF44336),
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )
                PrioritySummaryCard(
                    count = mediumCount,
                    label = "Medium",
                    color = Color(0xFFFF9800),
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )
                PrioritySummaryCard(
                    count = lowCount,
                    label = "Low",
                    color = Color(0xFF4CAF50),
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        items(suggestions) { suggestion ->
            SuggestionCard(
                suggestion = suggestion,
                isDark = isDark,
                onToggleFix = { onToggleFix(suggestion.id) }
            )
        }
    }
}

@Composable
fun SuggestionCard(
    suggestion: Suggestion,
    isDark: Boolean,
    onToggleFix: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        getCategoryIcon(suggestion.category),
                        contentDescription = null,
                        tint = getCategoryColor(suggestion.category),
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        suggestion.category.name.replace("_", " "),
                        fontSize = 12.sp,
                        color = getCategoryColor(suggestion.category),
                        fontWeight = FontWeight.Bold
                    )
                }

                PriorityChip(priority = suggestion.priority)
            }

            Spacer(Modifier.height(12.dp))

            Text(
                suggestion.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color.Black
            )

            Spacer(Modifier.height(8.dp))

            Text(
                suggestion.description,
                fontSize = 14.sp,
                color = if (isDark) Color.White.copy(0.7f) else Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (suggestion.isFixed) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Marked as Fixed",
                            fontSize = 12.sp,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    TextButton(
                        onClick = onToggleFix,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF6C63FF)
                        )
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Mark as Fixed",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PriorityChip(priority: Priority) {
    val (color, label) = when (priority) {
        Priority.HIGH -> Pair(Color(0xFFF44336), "High")
        Priority.MEDIUM -> Pair(Color(0xFFFF9800), "Medium")
        Priority.LOW -> Pair(Color(0xFF4CAF50), "Low")
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.2f),
        modifier = Modifier.border(1.dp, color, RoundedCornerShape(12.dp))
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 11.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PrioritySummaryCard(
    count: Int,
    label: String,
    color: Color,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF2a2a3e) else Color.White
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                count.toString(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                label,
                fontSize = 12.sp,
                color = if (isDark) Color.White.copy(0.7f) else Color.Gray
            )
        }
    }
}

fun getCategoryIcon(category: SuggestionCategory): androidx.compose.ui.graphics.vector.ImageVector {
    return when (category) {
        SuggestionCategory.FORMATTING -> Icons.Default.FormatPaint
        SuggestionCategory.CONTENT -> Icons.Default.Article
        SuggestionCategory.KEYWORDS -> Icons.Default.Key
        SuggestionCategory.STRUCTURE -> Icons.Default.AccountTree
        SuggestionCategory.CONTACT_INFO -> Icons.Default.ContactMail
        SuggestionCategory.EXPERIENCE -> Icons.Default.WorkHistory
        SuggestionCategory.SKILLS -> Icons.Default.EmojiObjects
    }
}

fun getCategoryColor(category: SuggestionCategory): Color {
    return when (category) {
        SuggestionCategory.FORMATTING -> Color(0xFF6C63FF)
        SuggestionCategory.CONTENT -> Color(0xFFE91E63)
        SuggestionCategory.KEYWORDS -> Color(0xFFFF9800)
        SuggestionCategory.STRUCTURE -> Color(0xFF4CAF50)
        SuggestionCategory.CONTACT_INFO -> Color(0xFF2196F3)
        SuggestionCategory.EXPERIENCE -> Color(0xFF9C27B0)
        SuggestionCategory.SKILLS -> Color(0xFFFFD700)
    }
}

fun getScoreColor(score: Int): Color {
    return when {
        score >= 80 -> Color(0xFF4CAF50)
        score >= 60 -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }
}