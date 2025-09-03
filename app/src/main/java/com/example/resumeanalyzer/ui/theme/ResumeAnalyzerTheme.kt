package com.example.resumeanalyzer.ui.theme


//@Composable
//fun ResumeAnalyzerTheme(theme: String = "system", content: @Composable () -> Unit) {
//    val isDark = when (theme) {
//        "light" -> false
//        "dark" -> true
//        else -> isSystemInDarkTheme()
//    }
//
//    val colorScheme = if (isDark) darkColorScheme() else lightColorScheme()
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        content = content
//    )
//}

import androidx.compose.foundation.isSystemInDarkTheme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color



@Composable
fun ResumeAnalyzerTheme(theme: String = "system", content: @Composable () -> Unit) {
    val isSystemDarkTheme = isSystemInDarkTheme()

    val isDark = when (theme) {
        "light" -> false
        "dark" -> true
        "system" -> isSystemDarkTheme
        else -> isSystemDarkTheme
    }

    // For system theme, create slightly different colors to distinguish it
    val colorScheme = when (theme) {
        "system" -> if (isDark) {
            darkColorScheme().copy(
                primary = Color(0xFF7C73FF), // slightly different purple for system dark
                surface = Color(0xFF1A1A1A),  // slightly different dark surface
            )
        } else {
            lightColorScheme().copy(
                primary = Color(0xFF5A52E8), // slightly different purple for system light
                surface = Color(0xFFFAFAFA),  // slightly different light surface
            )
        }
        "dark" -> darkColorScheme()
        "light" -> lightColorScheme()
        else -> if (isDark) darkColorScheme() else lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}