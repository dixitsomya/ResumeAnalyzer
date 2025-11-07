package com.example.feature_recruiter.util

import android.content.Context
import java.io.File
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor

object PDFParser {

    /**
     * Extract text from PDF using iTextPDF (lightweight)
     * Returns empty string if extraction fails
     */
    fun extractTextFromPDF(context: Context, filePath: String): String {
        return try {
            val file = File(filePath)
            if (!file.exists()) return ""

            val reader = PdfReader(filePath)
            val text = StringBuilder()

            // Extract text from all pages
            for (i in 1..reader.numberOfPages) {
                text.append(PdfTextExtractor.getTextFromPage(reader, i))
                text.append(" ")
            }

            reader.close()
            text.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Extract text from multiple PDFs
     * Returns map of filepath -> extracted text
     */
    fun extractTextFromMultiplePDFs(context: Context, filePaths: List<String>): Map<String, String> {
        return filePaths.associate { path ->
            path to extractTextFromPDF(context, path)
        }
    }
}
