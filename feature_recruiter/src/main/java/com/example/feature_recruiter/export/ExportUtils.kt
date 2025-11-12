//
//
//package com.example.feature_recruiter.export
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Paint
//import android.graphics.pdf.PdfDocument
//import android.net.Uri
//import androidx.core.content.FileProvider
//import com.example.feature_recruiter.screens.UploadedResumeWithMatch
//import java.io.File
//import java.text.SimpleDateFormat
//import java.util.*
//
//object ExportUtils {
//
//    // ✅ SORT BY PRIORITY (High match % first)
//    private fun sortByPriority(results: List<UploadedResumeWithMatch>): List<UploadedResumeWithMatch> {
//        return results.sortedWith(
//            compareBy<UploadedResumeWithMatch> { !it.isMatch } // Matched first
//                .thenByDescending { it.matchPercentage } // Then by match % descending
//                .thenByDescending { it.experience } // Then by experience descending
//        )
//    }
//
//    // ✅ EXPORT TO CSV WITH BETTER FORMAT
//    fun exportAndPreviewCSV(
//        context: Context,
//        results: List<UploadedResumeWithMatch>,
//        selectedTechs: List<String>
//    ): Uri? {
//        return try {
//            val fileName = "Resume_Results_${System.currentTimeMillis()}.csv"
//            val file = File(context.cacheDir, fileName)
//
//            // Sort by priority
//            val sortedResults = sortByPriority(results)
//
//            file.bufferedWriter().use { writer ->
//                // ✅ HEADER
//                writer.write("Priority,Candidate Name,File Name,Experience,Detected Technologies,Matched Technologies,Match Status,Match Percentage,Total Techs\n")
//
//                // ✅ DATA ROWS
//                sortedResults.forEachIndexed { index, resume ->
//                    val priority = index + 1
//                    val detectedTech = resume.detectedTechStack.joinToString(" | ")
//                    val matchedTech = resume.matchedTechs.joinToString(" | ")
//                    val status = if (resume.isMatch) "✓ MATCHED" else "✗ NOT MATCHED"
//
//                    writer.write(
//                        "$priority," +
//                                "\"${resume.candidateName}\"," +
//                                "\"${resume.fileName}\"," +
//                                "${resume.experience} years," +
//                                "\"$detectedTech\"," +
//                                "\"$matchedTech\"," +
//                                "$status," +
//                                "${resume.matchPercentage}%," +
//                                "${resume.detectedTechStack.size}\n"
//                    )
//                }
//
//                // ✅ SUMMARY
//                writer.write("\n\n")
//                writer.write("=== FILTER CRITERIA ===\n")
//                writer.write("\"Required Technologies: ${selectedTechs.joinToString(", ")}\"\n")
//                writer.write("\"Total Resumes Processed: ${results.size}\"\n")
//                writer.write("\"Matched Resumes: ${results.count { it.isMatch }}\"\n")
//                writer.write("\"Match Percentage: ${if (results.isEmpty()) 0 else (results.count { it.isMatch } * 100) / results.size}%\"\n")
//                writer.write("\"Export Date: ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())}\"\n")
//            }
//
//            FileProvider.getUriForFile(
//                context,
//                "${context.packageName}.fileprovider",
//                file
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    // ✅ EXPORT TO PDF WITH BETTER FORMAT & TEXT WRAPPING
//    fun exportAndPreviewPDF(
//        context: Context,
//        results: List<UploadedResumeWithMatch>,
//        selectedTechs: List<String>
//    ): Uri? {
//        return try {
//            val fileName = "Resume_Results_${System.currentTimeMillis()}.pdf"
//            val file = File(context.cacheDir, fileName)
//
//            val pdfDocument = PdfDocument()
//            val pageWidth = 595
//            val pageHeight = 842
//
//            // Sort by priority
//            val sortedResults = sortByPriority(results)
//
//            var pageNumber = 1
//            var page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
//            var canvas = page.canvas
//            var yPosition = 30f
//
//            // ✅ TITLE
//            val titlePaint = Paint().apply {
//                textSize = 20f
//                isFakeBoldText = true
//            }
//            canvas.drawText("RESUME FILTER RESULTS - PRIORITY RANKED", 20f, yPosition, titlePaint)
//            yPosition += 25f
//
//            // ✅ FILTER INFO
//            val infoPaint = Paint().apply {
//                textSize = 9f
//            }
//            canvas.drawText("Required Skills: ${selectedTechs.joinToString(", ")}", 20f, yPosition, infoPaint)
//            yPosition += 15f
//            canvas.drawText("Generated: ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())}", 20f, yPosition, infoPaint)
//            yPosition += 15f
//            canvas.drawText("Total: ${results.size} | Matched: ${results.count { it.isMatch }} | Match Rate: ${if (results.isEmpty()) 0 else (results.count { it.isMatch } * 100) / results.size}%", 20f, yPosition, infoPaint)
//            yPosition += 20f
//
//            // ✅ TABLE HEADER WITH BACKGROUND
//            val headerPaint = Paint().apply {
//                textSize = 8f
//                isFakeBoldText = true
//            }
//
//            // Header background
//            val headerBgPaint = Paint().apply {
//                color = android.graphics.Color.parseColor("#4A90E2")
//            }
//            canvas.drawRect(20f, yPosition - 12f, 575f, yPosition + 5f, headerBgPaint)
//
//            // Header text (white)
//            val headerTextPaint = Paint().apply {
//                textSize = 8f
//                isFakeBoldText = true
//                color = android.graphics.Color.WHITE
//            }
//
//            canvas.drawText("Rank", 25f, yPosition, headerTextPaint)
//            canvas.drawText("Candidate", 55f, yPosition, headerTextPaint)
//            canvas.drawText("Exp", 140f, yPosition, headerTextPaint)
//            canvas.drawText("Matched Tech", 165f, yPosition, headerTextPaint)
//            canvas.drawText("All Tech", 320f, yPosition, headerTextPaint)
//            canvas.drawText("Status", 450f, yPosition, headerTextPaint)
//            canvas.drawText("Match %", 520f, yPosition, headerTextPaint)
//            yPosition += 20f
//
//            // ✅ TABLE DATA WITH BETTER SPACING
//            val dataPaint = Paint().apply {
//                textSize = 8f
//            }
//
//            val separatorPaint = Paint().apply {
//                strokeWidth = 0.5f
//                color = android.graphics.Color.parseColor("#CCCCCC")
//            }
//
//            sortedResults.forEachIndexed { index, resume ->
//                // Check if new page needed
//                if (yPosition > 780f) {
//                    pdfDocument.finishPage(page)
//                    pageNumber++
//                    page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
//                    canvas = page.canvas
//                    yPosition = 30f
//                }
//
//                val rank = index + 1
//                val candidateName = resume.candidateName.take(18)
//                val experience = "${resume.experience}y"
//                val matchedTechs = resume.matchedTechs.take(4).joinToString(",").take(30)
//                val allTechs = resume.detectedTechStack.take(3).joinToString(",").take(25)
//                val status = if (resume.isMatch) "✓ MATCH" else "✗ NO"
//                val matchPercent = "${resume.matchPercentage}%"
//
//                // Row background (alternate colors)
//                if (index % 2 == 0) {
//                    val bgPaint = Paint().apply {
//                        color = android.graphics.Color.parseColor("#F5F5F5")
//                    }
//                    canvas.drawRect(20f, yPosition - 12f, 575f, yPosition + 8f, bgPaint)
//                }
//
//                // Data text
//                canvas.drawText(rank.toString(), 30f, yPosition, dataPaint)
//                canvas.drawText(candidateName, 60f, yPosition, dataPaint)
//                canvas.drawText(experience, 145f, yPosition, dataPaint)
//                canvas.drawText(matchedTechs, 170f, yPosition, dataPaint)
//                canvas.drawText(allTechs, 325f, yPosition, dataPaint)
//
//                // Status with color
//                val statusPaint = Paint().apply {
//                    textSize = 8f
//                    isFakeBoldText = true
//                    color = if (resume.isMatch) android.graphics.Color.parseColor("#4CAF50") else android.graphics.Color.parseColor("#FF6B6B")
//                }
//                canvas.drawText(status, 455f, yPosition, statusPaint)
//                canvas.drawText(matchPercent, 525f, yPosition, dataPaint)
//
//                // Separator line
//                canvas.drawLine(20f, yPosition + 10f, 575f, yPosition + 10f, separatorPaint)
//
//                yPosition += 18f
//            }
//
//            pdfDocument.finishPage(page)
//            pdfDocument.writeTo(file.outputStream())
//            pdfDocument.close()
//
//            FileProvider.getUriForFile(
//                context,
//                "${context.packageName}.fileprovider",
//                file
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    // ✅ OPEN CSV WITH SHARE CHOOSER
//    fun openCSV(context: Context, uri: Uri) {
//        val intent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_STREAM, uri)
//            type = "text/csv"
//            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        }
//        context.startActivity(Intent.createChooser(intent, "Export CSV"))
//    }
//
//    // ✅ OPEN PDF WITH SHARE CHOOSER
//    fun openPDF(context: Context, uri: Uri) {
//        val intent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_STREAM, uri)
//            type = "application/pdf"
//            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        }
//        context.startActivity(Intent.createChooser(intent, "Export PDF"))
//    }
//}


package com.example.feature_recruiter.export

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.feature_recruiter.screens.UploadedResumeWithMatch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object ExportUtils {

    // ✅ SORT BY PRIORITY (High match % first)
    private fun sortByPriority(results: List<UploadedResumeWithMatch>): List<UploadedResumeWithMatch> {
        return results.sortedWith(
            compareBy<UploadedResumeWithMatch> { !it.isMatch }
                .thenByDescending { it.matchPercentage }
                .thenByDescending { it.experience }
        )
    }

    // ✅ EXPORT TO CSV - ONLY FILE NAME
    fun exportAndPreviewCSV(
        context: Context,
        results: List<UploadedResumeWithMatch>,
        selectedTechs: List<String>
    ): Uri? {
        return try {
            val fileName = "Resume_Results_${System.currentTimeMillis()}.csv"
            val file = File(context.cacheDir, fileName)

            val sortedResults = sortByPriority(results)

            file.bufferedWriter().use { writer ->
                // ✅ HEADER - Only File Name (No Candidate)
                writer.write("Priority,File Name,Experience,Detected Technologies,Matched Technologies,Match Status,Match Percentage,Total Techs\n")

                // ✅ DATA ROWS
                sortedResults.forEachIndexed { index, resume ->
                    val priority = index + 1
                    val detectedTech = resume.detectedTechStack.joinToString(" | ")
                    val matchedTech = resume.matchedTechs.joinToString(" | ")
                    val status = if (resume.isMatch) "✓ MATCHED" else "✗ NOT MATCHED"

                    writer.write(
                        "$priority," +
                                "\"${resume.fileName}\"," +
                                "${resume.experience} years," +
                                "\"$detectedTech\"," +
                                "\"$matchedTech\"," +
                                "$status," +
                                "${resume.matchPercentage}%," +
                                "${resume.detectedTechStack.size}\n"
                    )
                }

                // ✅ SUMMARY
                writer.write("\n\n")
                writer.write("=== FILTER CRITERIA ===\n")
                writer.write("\"Required Technologies: ${selectedTechs.joinToString(", ")}\"\n")
                writer.write("\"Total Resumes Processed: ${results.size}\"\n")
                writer.write("\"Matched Resumes: ${results.count { it.isMatch }}\"\n")
                writer.write("\"Match Percentage: ${if (results.isEmpty()) 0 else (results.count { it.isMatch } * 100) / results.size}%\"\n")
                writer.write("\"Export Date: ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())}\"\n")
            }

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // ✅ EXPORT TO PDF - IMPROVED WITH FULL TECH DISPLAY
    fun exportAndPreviewPDF(
        context: Context,
        results: List<UploadedResumeWithMatch>,
        selectedTechs: List<String>
    ): Uri? {
        return try {
            val fileName = "Resume_Results_${System.currentTimeMillis()}.pdf"
            val file = File(context.cacheDir, fileName)

            val pdfDocument = PdfDocument()
            val pageWidth = 595
            val pageHeight = 842

            val sortedResults = sortByPriority(results)

            var pageNumber = 1
            var page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
            var canvas = page.canvas
            var yPosition = 30f

            // ✅ TITLE
            val titlePaint = Paint().apply {
                textSize = 20f
                isFakeBoldText = true
            }
            canvas.drawText("RESUME FILTER RESULTS - PRIORITY RANKED", 20f, yPosition, titlePaint)
            yPosition += 25f

            // ✅ FILTER INFO
            val infoPaint = Paint().apply {
                textSize = 9f
            }
            canvas.drawText("Required Skills: ${selectedTechs.joinToString(", ")}", 20f, yPosition, infoPaint)
            yPosition += 15f
            canvas.drawText("Generated: ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())}", 20f, yPosition, infoPaint)
            yPosition += 15f
            canvas.drawText("Total: ${results.size} | Matched: ${results.count { it.isMatch }} | Match Rate: ${if (results.isEmpty()) 0 else (results.count { it.isMatch } * 100) / results.size}%", 20f, yPosition, infoPaint)
            yPosition += 20f

            // ✅ TABLE HEADER WITH BACKGROUND
            val headerBgPaint = Paint().apply {
                color = android.graphics.Color.parseColor("#4A90E2")
            }
            canvas.drawRect(20f, yPosition - 12f, 575f, yPosition + 5f, headerBgPaint)

            val headerTextPaint = Paint().apply {
                textSize = 8f
                isFakeBoldText = true
                color = android.graphics.Color.WHITE
            }

            canvas.drawText("Rank", 25f, yPosition, headerTextPaint)
            canvas.drawText("File Name", 55f, yPosition, headerTextPaint)
            canvas.drawText("Exp", 160f, yPosition, headerTextPaint)
            canvas.drawText("Matched Tech", 185f, yPosition, headerTextPaint)
            canvas.drawText("All Tech", 330f, yPosition, headerTextPaint)
            canvas.drawText("Status", 480f, yPosition, headerTextPaint)
            canvas.drawText("Match %", 530f, yPosition, headerTextPaint)
            yPosition += 20f

            // ✅ TABLE DATA WITH FULL TECH DISPLAY
            val dataPaint = Paint().apply {
                textSize = 8f
            }

            val separatorPaint = Paint().apply {
                strokeWidth = 0.5f
                color = android.graphics.Color.parseColor("#CCCCCC")
            }

            sortedResults.forEachIndexed { index, resume ->
                // Check if new page needed
                if (yPosition > 780f) {
                    pdfDocument.finishPage(page)
                    pageNumber++
                    page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
                    canvas = page.canvas
                    yPosition = 30f
                }

                val rank = index + 1
                val fileName = resume.fileName.take(20)
                val experience = "${resume.experience}y"

                // ✅ MATCHED TECH - Show all or +X
                val matchedTechDisplay = if (resume.matchedTechs.size <= 3) {
                    resume.matchedTechs.joinToString(",")
                } else {
                    resume.matchedTechs.take(3).joinToString(",") + "+${resume.matchedTechs.size - 3}"
                }

                // ✅ ALL TECH - Show all or +X
                val allTechDisplay = if (resume.detectedTechStack.size <= 3) {
                    resume.detectedTechStack.joinToString(",").take(30)
                } else {
                    resume.detectedTechStack.take(3).joinToString(",") + "+${resume.detectedTechStack.size - 3}"
                }

                val status = if (resume.isMatch) "✓ MATCH" else "✗ NO"
                val matchPercent = "${resume.matchPercentage}%"

                // Row background (alternate colors)
                if (index % 2 == 0) {
                    val bgPaint = Paint().apply {
                        color = android.graphics.Color.parseColor("#F5F5F5")
                    }
                    canvas.drawRect(20f, yPosition - 12f, 575f, yPosition + 8f, bgPaint)
                }

                // Data text
                canvas.drawText(rank.toString(), 30f, yPosition, dataPaint)
                canvas.drawText(fileName, 60f, yPosition, dataPaint)
                canvas.drawText(experience, 165f, yPosition, dataPaint)
                canvas.drawText(matchedTechDisplay, 190f, yPosition, dataPaint)
                canvas.drawText(allTechDisplay, 335f, yPosition, dataPaint)

                // Status with color
                val statusPaint = Paint().apply {
                    textSize = 8f
                    isFakeBoldText = true
                    color = if (resume.isMatch) android.graphics.Color.parseColor("#4CAF50") else android.graphics.Color.parseColor("#FF6B6B")
                }
                canvas.drawText(status, 485f, yPosition, statusPaint)
                canvas.drawText(matchPercent, 535f, yPosition, dataPaint)

                // Separator line
                canvas.drawLine(20f, yPosition + 10f, 575f, yPosition + 10f, separatorPaint)

                yPosition += 18f
            }

            pdfDocument.finishPage(page)
            pdfDocument.writeTo(file.outputStream())
            pdfDocument.close()

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // ✅ OPEN CSV WITH SHARE CHOOSER
    fun openCSV(context: Context, uri: Uri) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "text/csv"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Export CSV"))
    }

    // ✅ OPEN PDF WITH SHARE CHOOSER
    fun openPDF(context: Context, uri: Uri) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "application/pdf"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Export PDF"))
    }
}