//package com.example.feature_recruiter.util
//
//import com.tom_roush.pdfbox.pdfparser.PDFParser
//import com.tom_roush.pdfbox.pdfdocument.PDFDocument
//import com.tom_roush.pdfbox.text.PDFTextStripper
//import java.io.File
//import java.io.FileInputStream
//
//object PDFParserUtil {  // Renamed to avoid conflict with PDFParser class
//
//    fun extractTextFromPDF(filePath: String): String {
//        return try {
//            val file = File(filePath)
//            if (!file.exists()) return ""
//
//            val inputStream = FileInputStream(file)
//            val parser = PDFParser(inputStream)
//            val document = PDFDocument(parser.parse())
//
//            val stripper = PDFTextStripper()
//            val text = stripper.getText(document)
//
//            document.close()
//            inputStream.close()
//
//            text
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ""
//        }
//    }
//
//    fun extractTextFromMultiplePDFs(filePaths: List<String>): Map<String, String> {
//        return filePaths.associate { path ->
//            path to extractTextFromPDF(path)
//        }
//    }
//}

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
