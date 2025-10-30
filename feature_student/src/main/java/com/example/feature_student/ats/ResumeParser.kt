package com.example.feature_student.ats

import android.content.Context
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import java.io.File

/**
 * FREE Resume Parser - No external API needed
 * Uses iTextPDF library for PDF parsing
 */
class ResumeParser {

    suspend fun parseResume(filePath: String): String {
        return try {
            val file = File(filePath)
            when {
                filePath.endsWith(".pdf", ignoreCase = true) -> {
                    extractTextFromPDF(file)
                }
                filePath.endsWith(".txt", ignoreCase = true) -> {
                    file.readText()
                }
                else -> {
                    // For DOC/DOCX, fallback to basic extraction
                    "Unable to parse this format. Please use PDF."
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error parsing resume: ${e.message}"
        }
    }

    private fun extractTextFromPDF(file: File): String {
        return try {
            val reader = PdfReader(file.absolutePath)
            val sb = StringBuilder()

            for (i in 1..reader.numberOfPages) {
                sb.append(PdfTextExtractor.getTextFromPage(reader, i))
                sb.append("\n")
            }

            reader.close()
            sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "Error reading PDF: ${e.message}"
        }
    }

    fun extractEmail(text: String): String? {
        val emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        return emailRegex.find(text)?.value
    }

    fun extractPhone(text: String): String? {
        val phoneRegex = "\\+?\\d[\\d -]{8,12}\\d".toRegex()
        return phoneRegex.find(text)?.value
    }

    fun extractLinkedIn(text: String): String? {
        val linkedInRegex = "(linkedin\\.com/in/[a-zA-Z0-9_-]+)".toRegex()
        return linkedInRegex.find(text)?.value
    }
}