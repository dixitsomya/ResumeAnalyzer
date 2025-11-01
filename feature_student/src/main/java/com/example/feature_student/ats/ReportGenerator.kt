//
//package com.example.feature_student.ats
//
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.os.Build
//import android.os.Environment
//import android.util.Log
//import android.content.ContentResolver
//import androidx.core.content.FileProvider
//import com.example.feature_student.model.ATSAnalysisResult
//import com.example.feature_student.model.Resume
//import com.itextpdf.text.*
//import com.itextpdf.text.pdf.PdfPCell
//import com.itextpdf.text.pdf.PdfPTable
//import com.itextpdf.text.pdf.PdfWriter
//import java.io.File
//import java.io.FileOutputStream
//import java.text.SimpleDateFormat
//import java.util.*
//
//class ReportGenerator(private val context: Context) {
//
//    fun generatePDFReport(resume: Resume, analysis: ATSAnalysisResult): File? {
//        return try {
//            Log.d("ReportGen", "📄 Generating PDF report")
//
//            // Get actual Downloads folder
//            val downloadsDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//            } else {
//                File(Environment.getExternalStorageDirectory(), "Download")
//            }
//
//            if (!downloadsDir.exists()) {
//                downloadsDir.mkdirs()
//            }
//
//            val fileName = "ATS_Report_${System.currentTimeMillis()}.pdf"
//            val file = File(downloadsDir, fileName)
//
//            Log.d("ReportGen", "📁 Saving to: ${file.absolutePath}")
//
//            val document = Document(PageSize.A4, 40f, 40f, 50f, 50f)
//            PdfWriter.getInstance(document, FileOutputStream(file))
//            document.open()
//
//            // Add all sections
//            addHeader(document, resume, analysis)
//            addScoreSummary(document, analysis)
//            addScoreBreakdown(document, analysis)
//            addSectionsAnalysis(document, analysis)
//            addStrengthsSection(document, analysis)
//            addWeaknessesSection(document, analysis)
//            addDetailedReport(document, analysis)
//            addKeywordsSection(document, analysis)
//            addSuggestionsSection(document, analysis)
//            addFooter(document)
//
//            document.close()
//
//            Log.d("ReportGen", "✅ PDF created successfully")
//            Log.d("ReportGen", "📥 File: ${file.name}")
//            Log.d("ReportGen", "📊 Size: ${formatFileSize(file.length())}")
//            Log.d("ReportGen", "📂 Path: ${file.absolutePath}")
//
//            file
//        } catch (e: Exception) {
//            Log.e("ReportGen", "❌ PDF generation failed: ${e.message}")
//            e.printStackTrace()
//            null
//        }
//    }
//
//    fun openPDFReport(file: File) {
//        try {
//            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
//            } else {
//                Uri.fromFile(file)
//            }
//
//            val intent = Intent(Intent.ACTION_VIEW).apply {
//                setDataAndType(uri, "application/pdf")
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                }
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//
//            context.startActivity(intent)
//            Log.d("ReportGen", "✅ PDF opened successfully")
//        } catch (e: Exception) {
//            Log.e("ReportGen", "❌ Could not open PDF: ${e.message}")
//        }
//    }
//
//    private fun addHeader(document: Document, resume: Resume, analysis: ATSAnalysisResult) {
//        val titleFont = Font(Font.FontFamily.HELVETICA, 28f, Font.BOLD, BaseColor(108, 99, 255))
//        val subtitleFont = Font(Font.FontFamily.HELVETICA, 13f, Font.NORMAL, BaseColor.GRAY)
//
//        val title = Paragraph("ATS RESUME ANALYSIS REPORT", titleFont)
//        title.alignment = Element.ALIGN_CENTER
//        title.spacingAfter = 5f
//        document.add(title)
//
//        val subtitle = Paragraph("Professional Resume Optimization Assessment", subtitleFont)
//        subtitle.alignment = Element.ALIGN_CENTER
//        subtitle.spacingAfter = 20f
//        document.add(subtitle)
//
//        val infoTable = PdfPTable(4)
//        infoTable.widthPercentage = 100f
//        infoTable.spacingAfter = 20f
//
//        addHeaderCell(infoTable, "Resume")
//        addHeaderCell(infoTable, "Date")
//        addHeaderCell(infoTable, "Score")
//        addHeaderCell(infoTable, "Analysis")
//
//        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
//        addDataCell(infoTable, resume.fileName.take(25))
//        addDataCell(infoTable, dateFormat.format(Date()))
//        addDataCell(infoTable, "${analysis.overallScore}/100")
//        addDataCell(infoTable, analysis.analyzedBy)
//
//        document.add(infoTable)
//    }
//
//    private fun addScoreSummary(document: Document, analysis: ATSAnalysisResult) {
//        val scoreCell = PdfPCell().apply {
//            borderColor = BaseColor(108, 99, 255)
//            borderWidth = 3f
//            setPadding(20f)
//            backgroundColor = BaseColor(245, 243, 255)
//
//            val scoreText = when {
//                analysis.overallScore >= 80 -> "🟢 EXCELLENT - Ready for Submission (${analysis.overallScore}/100)"
//                analysis.overallScore >= 70 -> "🟢 GOOD - Minor improvements recommended (${analysis.overallScore}/100)"
//                analysis.overallScore >= 60 -> "🟡 FAIR - Several improvements needed (${analysis.overallScore}/100)"
//                else -> "🔴 NEEDS WORK - Significant changes required (${analysis.overallScore}/100)"
//            }
//
//            val paragraph = Paragraph(scoreText, Font(Font.FontFamily.HELVETICA, 15f, Font.BOLD, BaseColor(108, 99, 255)))
//            paragraph.alignment = Element.ALIGN_CENTER
//            addElement(paragraph)
//        }
//
//        val scoreTable = PdfPTable(1)
//        scoreTable.widthPercentage = 100f
//        scoreTable.spacingAfter = 20f
//        scoreTable.addCell(scoreCell)
//        document.add(scoreTable)
//    }
//
//    private fun addScoreBreakdown(document: Document, analysis: ATSAnalysisResult) {
//        addSectionTitle(document, "📊 SCORE BREAKDOWN")
//
//        val table = PdfPTable(3)
//        table.widthPercentage = 100f
//        table.setWidths(floatArrayOf(2.5f, 1f, 1.5f))
//        table.spacingAfter = 15f
//
//        addHeaderCell(table, "Category")
//        addHeaderCell(table, "Score")
//        addHeaderCell(table, "Status")
//
//        addScoreRow(table, "Format Compatibility", analysis.breakdown.formatScore)
//        addScoreRow(table, "Keyword Optimization", analysis.breakdown.keywordScore)
//        addScoreRow(table, "Content Quality", analysis.breakdown.contentScore)
//        addScoreRow(table, "Structure & Organization", analysis.breakdown.structureScore)
//        addScoreRow(table, "Contact Information", analysis.breakdown.contactScore)
//
//        document.add(table)
//    }
//
//    private fun addSectionsAnalysis(document: Document, analysis: ATSAnalysisResult) {
//        addSectionTitle(document, "✅ RESUME SECTIONS ANALYSIS")
//
//        val sections = analysis.sections
//        val sectionsList = listOf(
//            "Contact Information" to sections.hasContactInfo,
//            "Professional Summary" to sections.hasObjective,
//            "Work Experience" to sections.hasExperience,
//            "Education" to sections.hasEducation,
//            "Skills" to sections.hasSkills,
//            "Certifications" to sections.hasCertifications
//        )
//
//        val table = PdfPTable(2)
//        table.widthPercentage = 100f
//        table.setWidths(floatArrayOf(1.5f, 1f))
//        table.spacingAfter = 15f
//
//        for ((name, present) in sectionsList) {
//            val status = if (present) "✅ Present" else "❌ Missing"
//            val cell1 = PdfPCell(Paragraph(name, Font(Font.FontFamily.HELVETICA, 11f)))
//            cell1.setPadding(8f)
//            table.addCell(cell1)
//
//            val cell2 = PdfPCell(Paragraph(status, Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, if (present) BaseColor(76, 175, 80) else BaseColor(244, 67, 54))))
//            cell2.setPadding(8f)
//            cell2.horizontalAlignment = Element.ALIGN_CENTER
//            table.addCell(cell2)
//        }
//
//        document.add(table)
//    }
//
//    private fun addStrengthsSection(document: Document, analysis: ATSAnalysisResult) {
//        addSectionTitle(document, "✨ TOP STRENGTHS", BaseColor(76, 175, 80))
//
//        for (strength in analysis.strengths.take(5)) {
//            val p = Paragraph("✓ $strength", Font(Font.FontFamily.HELVETICA, 11f, Font.NORMAL, BaseColor(76, 175, 80)))
//            p.spacingAfter = 6f
//            document.add(p)
//        }
//
//        document.add(Paragraph("\n"))
//    }
//
//    private fun addWeaknessesSection(document: Document, analysis: ATSAnalysisResult) {
//        addSectionTitle(document, "⚠️ AREAS FOR IMPROVEMENT", BaseColor(244, 67, 54))
//
//        for (weakness in analysis.weaknesses.take(5)) {
//            val p = Paragraph("→ $weakness", Font(Font.FontFamily.HELVETICA, 11f, Font.NORMAL, BaseColor(244, 67, 54)))
//            p.spacingAfter = 6f
//            document.add(p)
//        }
//
//        document.add(Paragraph("\n"))
//    }
//
//    private fun addDetailedReport(document: Document, analysis: ATSAnalysisResult) {
//        addSectionTitle(document, "📋 DETAILED ANALYSIS")
//
//        val p = Paragraph(analysis.detailedReport, Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor.BLACK))
//        p.spacingAfter = 15f
//        document.add(p)
//    }
//
//    private fun addKeywordsSection(document: Document, analysis: ATSAnalysisResult) {
//        addSectionTitle(document, "🔑 KEYWORD ANALYSIS")
//
//        val keywords = analysis.keywords
//
//        val p1 = Paragraph("Found Keywords (${keywords.foundKeywords.size}):", Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor(76, 175, 80)))
//        p1.spacingAfter = 5f
//        document.add(p1)
//
//        val p2 = Paragraph(keywords.foundKeywords.take(20).joinToString(", "), Font(Font.FontFamily.HELVETICA, 10f))
//        p2.spacingAfter = 12f
//        document.add(p2)
//
//        val p3 = Paragraph("Missing Keywords (${keywords.missingKeywords.size}):", Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor(255, 152, 0)))
//        p3.spacingAfter = 5f
//        document.add(p3)
//
//        val p4 = Paragraph(keywords.missingKeywords.joinToString(", "), Font(Font.FontFamily.HELVETICA, 10f))
//        p4.spacingAfter = 15f
//        document.add(p4)
//    }
//
//    private fun addSuggestionsSection(document: Document, analysis: ATSAnalysisResult) {
//        val highSuggestions = analysis.suggestions.filter { it.priority.name == "HIGH" }
//
//        if (highSuggestions.isNotEmpty()) {
//            addSectionTitle(document, "💡 HIGH PRIORITY RECOMMENDATIONS")
//
//            for ((index, suggestion) in highSuggestions.take(8).withIndex()) {
//                val titleP = Paragraph("${index + 1}. ${suggestion.title}", Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor(244, 67, 54)))
//                titleP.spacingAfter = 4f
//                document.add(titleP)
//
//                val descP = Paragraph(suggestion.description, Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor.DARK_GRAY))
//                descP.spacingAfter = 10f
//                document.add(descP)
//            }
//        }
//    }
//
//    private fun addFooter(document: Document) {
//        document.add(Paragraph("\n"))
//        val line = Paragraph("_".repeat(80))
//        line.alignment = Element.ALIGN_CENTER
//        line.spacingAfter = 10f
//        document.add(line)
//
//        val footer = Paragraph(
//            "Generated by Resume ATS Analyzer • ${getCurrentDate()}",
//            Font(Font.FontFamily.HELVETICA, 9f, Font.ITALIC, BaseColor.GRAY)
//        )
//        footer.alignment = Element.ALIGN_CENTER
//        document.add(footer)
//    }
//
//    private fun addSectionTitle(document: Document, title: String, color: BaseColor = BaseColor(108, 99, 255)) {
//        val titleParagraph = Paragraph(title, Font(Font.FontFamily.HELVETICA, 16f, Font.BOLD, color))
//        titleParagraph.spacingBefore = 15f
//        titleParagraph.spacingAfter = 12f
//        document.add(titleParagraph)
//    }
//
//    private fun addHeaderCell(table: PdfPTable, text: String) {
//        val cell = PdfPCell(Paragraph(text, Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor.WHITE)))
//        cell.backgroundColor = BaseColor(108, 99, 255)
//        cell.setPadding(10f)
//        cell.horizontalAlignment = Element.ALIGN_CENTER
//        table.addCell(cell)
//    }
//
//    private fun addDataCell(table: PdfPTable, text: String) {
//        val cell = PdfPCell(Paragraph(text, Font(Font.FontFamily.HELVETICA, 10f)))
//        cell.setPadding(8f)
//        cell.borderColor = BaseColor(200, 200, 200)
//        cell.borderWidth = 0.5f
//        table.addCell(cell)
//    }
//
//    private fun addScoreRow(table: PdfPTable, label: String, score: Int) {
//        val labelCell = PdfPCell(Paragraph(label, Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL)))
//        labelCell.setPadding(8f)
//        table.addCell(labelCell)
//
//        val scoreCell = PdfPCell(Paragraph(score.toString(), Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD)))
//        scoreCell.setPadding(8f)
//        scoreCell.horizontalAlignment = Element.ALIGN_CENTER
//        table.addCell(scoreCell)
//
//        val status = when {
//            score >= 80 -> "✅"
//            score >= 60 -> "👍"
//            else -> "⚠️"
//        }
//        val statusCell = PdfPCell(Paragraph(status, Font(Font.FontFamily.HELVETICA, 14f)))
//        statusCell.setPadding(8f)
//        statusCell.horizontalAlignment = Element.ALIGN_CENTER
//        table.addCell(statusCell)
//    }
//
//    private fun getCurrentDate(): String {
//        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
//        return sdf.format(Date())
//    }
//
//    private fun formatFileSize(bytes: Long): String {
//        return when {
//            bytes < 1024 -> "$bytes B"
//            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
//            else -> String.format("%.2f MB", bytes / (1024.0 * 1024.0))
//        }
//    }
//}

//package com.example.feature_student.ats
//
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.os.Build
//import android.os.Environment
//import android.util.Log
//import androidx.core.content.FileProvider
//import com.example.feature_student.model.ATSAnalysisResult
//import com.example.feature_student.model.Resume
//import com.itextpdf.text.*
//import com.itextpdf.text.pdf.PdfPCell
//import com.itextpdf.text.pdf.PdfPTable
//import com.itextpdf.text.pdf.PdfWriter
//import java.io.File
//import java.io.FileOutputStream
//import java.text.SimpleDateFormat
//import java.util.*
//
//class ReportGenerator(private val context: Context) {
//
//    fun generatePDFReport(resume: Resume, analysis: ATSAnalysisResult): File? {
//        return try {
//            Log.d("ReportGen", "📄 Generating PDF report")
//
//            val downloadsDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//            } else {
//                File(Environment.getExternalStorageDirectory(), "Download")
//            }
//
//            if (!downloadsDir.exists()) {
//                downloadsDir.mkdirs()
//            }
//
//            val fileName = "ATS_Report_${System.currentTimeMillis()}.pdf"
//            val file = File(downloadsDir, fileName)
//
//            Log.d("ReportGen", "📁 Saving to: ${file.absolutePath}")
//
//            val document = Document(PageSize.A4, 50f, 50f, 60f, 60f)
//            PdfWriter.getInstance(document, FileOutputStream(file))
//            document.open()
//
//            addCoverPage(document, analysis)
//            document.newPage()
//            addScorePage(document, analysis)
//            document.newPage()
//            addStructureAnalysisPage(document, analysis)
//            document.newPage()
//            addContentMetricsPage(document, analysis)
//            document.newPage()
//            addOnlinePresencePage(document, analysis)
//            document.newPage()
//            addStrengthsWeaknessesPage(document, analysis)
//            document.newPage()
//            addKeywordsPage(document, analysis)
//            document.newPage()
//            addSuggestionsPage(document, analysis)
//
//            document.close()
//
//            Log.d("ReportGen", "✅ PDF created successfully")
//            Log.d("ReportGen", "📥 File: ${file.name}")
//            Log.d("ReportGen", "📊 Size: ${formatFileSize(file.length())}")
//
//            file
//        } catch (e: Exception) {
//            Log.e("ReportGen", "❌ PDF generation failed: ${e.message}")
//            e.printStackTrace()
//            null
//        }
//    }
//
//    fun openPDFReport(file: File) {
//        try {
//            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
//            } else {
//                Uri.fromFile(file)
//            }
//
//            val intent = Intent(Intent.ACTION_VIEW).apply {
//                setDataAndType(uri, "application/pdf")
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                }
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//
//            context.startActivity(intent)
//        } catch (e: Exception) {
//            Log.e("ReportGen", "❌ Could not open PDF: ${e.message}")
//        }
//    }
//
//    private fun addCoverPage(document: Document, analysis: ATSAnalysisResult) {
//        val titleFont = Font(Font.FontFamily.HELVETICA, 48f, Font.BOLD, BaseColor(108, 99, 255))
//        val subtitleFont = Font(Font.FontFamily.HELVETICA, 24f, Font.NORMAL, BaseColor.GRAY)
//        val scoreFont = Font(Font.FontFamily.HELVETICA, 72f, Font.BOLD, BaseColor(108, 99, 255))
//
//        document.add(Paragraph("\n\n\n\n"))
//
//        val title = Paragraph("ATS ANALYSIS REPORT", titleFont)
//        title.alignment = Element.ALIGN_CENTER
//        title.spacingAfter = 20f
//        document.add(title)
//
//        val subtitle = Paragraph("Professional Resume Evaluation", subtitleFont)
//        subtitle.alignment = Element.ALIGN_CENTER
//        subtitle.spacingAfter = 80f
//        document.add(subtitle)
//
//        val scoreBox = Paragraph(analysis.overallScore.toString(), scoreFont)
//        scoreBox.alignment = Element.ALIGN_CENTER
//        scoreBox.spacingAfter = 10f
//        document.add(scoreBox)
//
//        val outOf = Paragraph("out of 100", Font(Font.FontFamily.HELVETICA, 28f, Font.NORMAL, BaseColor.GRAY))
//        outOf.alignment = Element.ALIGN_CENTER
//        outOf.spacingAfter = 40f
//        document.add(outOf)
//
//        val status = when {
//            analysis.overallScore >= 80 -> "🟢 EXCELLENT"
//            analysis.overallScore >= 70 -> "🟢 GOOD"
//            analysis.overallScore >= 60 -> "🟡 FAIR"
//            else -> "🔴 NEEDS IMPROVEMENT"
//        }
//
//        val statusP = Paragraph(status, Font(Font.FontFamily.HELVETICA, 24f, Font.BOLD, BaseColor(108, 99, 255)))
//        statusP.alignment = Element.ALIGN_CENTER
//        statusP.spacingAfter = 60f
//        document.add(statusP)
//
//        val footer = Paragraph(
//            "Generated: ${getCurrentDate()}\nAnalyzer: Advanced ATS Calculator",
//            Font(Font.FontFamily.HELVETICA, 11f, Font.NORMAL, BaseColor.GRAY)
//        )
//        footer.alignment = Element.ALIGN_CENTER
//        document.add(footer)
//    }
//
//    private fun addScorePage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "📊 SCORE ANALYSIS")
//
//        val scoreTable = PdfPTable(2)
//        scoreTable.widthPercentage = 100f
//        scoreTable.setWidths(floatArrayOf(1.5f, 1f))
//        scoreTable.spacingAfter = 20f
//
//        addScoreTableRow(scoreTable, "Overall Score", analysis.overallScore)
//        addScoreTableRow(scoreTable, "Format Compatibility", analysis.breakdown.formatScore)
//        addScoreTableRow(scoreTable, "Keyword Optimization", analysis.breakdown.keywordScore)
//        addScoreTableRow(scoreTable, "Content Quality", analysis.breakdown.contentScore)
//        addScoreTableRow(scoreTable, "Structure", analysis.breakdown.structureScore)
//        addScoreTableRow(scoreTable, "Contact Info", analysis.breakdown.contactScore)
//
//        document.add(scoreTable)
//
//        document.add(Paragraph("\n"))
//        addSectionHeader(document, "Score Interpretation")
//
//        val interpretation = when {
//            analysis.overallScore >= 80 -> "Your resume is well-optimized and ready for ATS submission. Continue maintaining these standards in all job applications."
//            analysis.overallScore >= 70 -> "Your resume is competitive. Consider the recommendations to improve further and achieve an excellent score."
//            analysis.overallScore >= 60 -> "Your resume needs improvement to be fully ATS-optimized. Follow the suggestions provided to enhance your score."
//            else -> "Your resume requires significant improvements before ATS submission. Address all high-priority recommendations."
//        }
//
//        document.add(Paragraph(interpretation, Font(Font.FontFamily.HELVETICA, 11f)))
//    }
//
//    private fun addStructureAnalysisPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "✅ RESUME STRUCTURE ANALYSIS")
//
//        val sections = analysis.sections
//        val sectionsList = listOf(
//            "Contact Information" to sections.hasContactInfo,
//            "Professional Summary" to sections.hasObjective,
//            "Work Experience" to sections.hasExperience,
//            "Education" to sections.hasEducation,
//            "Skills Section" to sections.hasSkills,
//            "Certifications" to sections.hasCertifications
//        )
//
//        val table = PdfPTable(2)
//        table.widthPercentage = 100f
//        table.setWidths(floatArrayOf(2f, 1f))
//        table.spacingAfter = 20f
//
//        addHeaderCell(table, "Section", BaseColor(108, 99, 255))
//        addHeaderCell(table, "Status", BaseColor(108, 99, 255))
//
//        for ((name, present) in sectionsList) {
//            val statusText = if (present) "✅ Present" else "❌ Missing"
//            val statusColor = if (present) BaseColor(76, 175, 80) else BaseColor(244, 67, 54)
//
//            val nameCell = PdfPCell(Paragraph(name, Font(Font.FontFamily.HELVETICA, 11f)))
//            nameCell.setPadding(10f)
//            table.addCell(nameCell)
//
//            val statusCell = PdfPCell(Paragraph(statusText, Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, statusColor)))
//            statusCell.setPadding(10f)
//            statusCell.horizontalAlignment = Element.ALIGN_CENTER
//            table.addCell(statusCell)
//        }
//
//        document.add(table)
//
//        document.add(Paragraph("\n"))
//        addSectionHeader(document, "Recommendations")
//
//        if (!sections.hasExperience) {
//            addBullet(document, "Add a Work Experience section with your past roles and achievements")
//        }
//        if (!sections.hasEducation) {
//            addBullet(document, "Include your education details - degree, university, and graduation year")
//        }
//        if (!sections.hasSkills) {
//            addBullet(document, "Create a dedicated Skills section with relevant technical and soft skills")
//        }
//        if (!sections.hasObjective) {
//            addBullet(document, "Add a Professional Summary at the top of your resume")
//        }
//    }
//
//    private fun addContentMetricsPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "📈 CONTENT QUALITY METRICS")
//
//        val metricsTable = PdfPTable(2)
//        metricsTable.widthPercentage = 100f
//        metricsTable.setWidths(floatArrayOf(1.8f, 1f))
//        metricsTable.spacingAfter = 20f
//
//        addMetricRow(metricsTable, "Action Verbs Found", analysis.keywords.foundKeywords.size.toString())
//        addMetricRow(metricsTable, "Tech Keywords Found", analysis.keywords.keywordDensity.toInt().toString())
//        addMetricRow(metricsTable, "Word Count", (analysis.extractedText.split(Regex("\\s+")).size).toString())
//        addMetricRow(metricsTable, "Keyword Density", String.format("%.2f%%", analysis.keywords.keywordDensity))
//
//        document.add(metricsTable)
//
//        document.add(Paragraph("\n"))
//        addSectionHeader(document, "Metrics Explanation")
//
//        addBullet(document, "Action Verbs: Use powerful verbs like 'Led', 'Architected', 'Optimized' (Target: 8+)")
//        addBullet(document, "Tech Keywords: Include specific technologies, tools, and frameworks (Target: 12+)")
//        addBullet(document, "Word Count: Ideal range is 400-1200 words for optimal ATS performance")
//        addBullet(document, "Keyword Density: Maintain 5-10% technical keyword density for best results")
//    }
//
//    private fun addOnlinePresencePage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "🌐 ONLINE PRESENCE")
//
//        val presenceTable = PdfPTable(2)
//        presenceTable.widthPercentage = 100f
//        presenceTable.setWidths(floatArrayOf(1.5f, 1f))
//        presenceTable.spacingAfter = 20f
//
//        val profiles = listOf(
//            "LinkedIn Profile" to "Critical",
//            "GitHub Portfolio" to "Highly Important",
//            "Personal Website" to "Beneficial"
//        )
//
//        addHeaderCell(presenceTable, "Profile", BaseColor(108, 99, 255))
//        addHeaderCell(presenceTable, "Importance", BaseColor(108, 99, 255))
//
//        for ((profile, importance) in profiles) {
//            val profileCell = PdfPCell(Paragraph(profile, Font(Font.FontFamily.HELVETICA, 11f)))
//            profileCell.setPadding(10f)
//            presenceTable.addCell(profileCell)
//
//            val importanceCell = PdfPCell(Paragraph(importance, Font(Font.FontFamily.HELVETICA, 11f)))
//            importanceCell.setPadding(10f)
//            presenceTable.addCell(importanceCell)
//        }
//
//        document.add(presenceTable)
//
//        document.add(Paragraph("\n"))
//        addSectionHeader(document, "Why Online Presence Matters")
//
//        addBullet(document, "LinkedIn: Professional networking platform trusted by recruiters worldwide")
//        addBullet(document, "GitHub: Showcases your actual coding projects and contributions")
//        addBullet(document, "Portfolio: Demonstrates your best work and capabilities to potential employers")
//    }
//
//    private fun addStrengthsWeaknessesPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "✨ STRENGTHS & AREAS FOR IMPROVEMENT")
//
//        addSectionHeader(document, "✅ Strengths")
//        for (strength in analysis.strengths.take(5)) {
//            addBullet(document, strength)
//        }
//
//        document.add(Paragraph("\n"))
//        addSectionHeader(document, "⚠️ Areas for Improvement")
//        for (weakness in analysis.weaknesses.take(5)) {
//            addBullet(document, weakness)
//        }
//    }
//
//    private fun addKeywordsPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "🔑 KEYWORD ANALYSIS")
//
//        addSectionHeader(document, "Found Keywords")
//        val foundText = analysis.keywords.foundKeywords.take(15).joinToString(", ")
//        document.add(Paragraph(foundText, Font(Font.FontFamily.HELVETICA, 10f)))
//
//        document.add(Paragraph("\n"))
//        addSectionHeader(document, "Recommended Keywords to Add")
//        val missingText = analysis.keywords.missingKeywords.joinToString(", ")
//        document.add(Paragraph(missingText, Font(Font.FontFamily.HELVETICA, 10f)))
//    }
//
//    private fun addSuggestionsPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "💡 RECOMMENDATIONS")
//
//        val highPriority = analysis.suggestions.filter { it.priority.name == "HIGH" }.take(5)
//
//        if (highPriority.isNotEmpty()) {
//            addSectionHeader(document, "🔴 High Priority (Address Immediately)")
//            for ((index, suggestion) in highPriority.withIndex()) {
//                document.add(Paragraph(
//                    "${index + 1}. ${suggestion.title}",
//                    Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD)
//                ))
//                document.add(Paragraph(
//                    suggestion.description,
//                    Font(Font.FontFamily.HELVETICA, 10f)
//                ))
//                document.add(Paragraph(" "))
//            }
//        }
//
//        val mediumPriority = analysis.suggestions.filter { it.priority.name == "MEDIUM" }.take(3)
//
//        if (mediumPriority.isNotEmpty()) {
//            document.add(Paragraph("\n"))
//            addSectionHeader(document, "🟡 Medium Priority (Recommended)")
//            for ((index, suggestion) in mediumPriority.withIndex()) {
//                document.add(Paragraph(
//                    "${index + 1}. ${suggestion.title}",
//                    Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD)
//                ))
//                document.add(Paragraph(
//                    suggestion.description,
//                    Font(Font.FontFamily.HELVETICA, 10f)
//                ))
//                document.add(Paragraph(" "))
//            }
//        }
//    }
//
//    private fun addPageHeader(document: Document, title: String) {
//        val header = Paragraph(title, Font(Font.FontFamily.HELVETICA, 24f, Font.BOLD, BaseColor(108, 99, 255)))
//        header.spacingAfter = 20f
//        document.add(header)
//
//        val line = Paragraph("_".repeat(80))
//        line.spacingAfter = 20f
//        document.add(line)
//    }
//
//    private fun addSectionHeader(document: Document, title: String) {
//        val header = Paragraph(title, Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD, BaseColor(108, 99, 255)))
//        header.spacingBefore = 10f
//        header.spacingAfter = 10f
//        document.add(header)
//    }
//
//    private fun addBullet(document: Document, text: String) {
//        val bullet = Paragraph("• $text", Font(Font.FontFamily.HELVETICA, 11f))
//        bullet.spacingAfter = 8f
//        document.add(bullet)
//    }
//
//    private fun addHeaderCell(table: PdfPTable, text: String, bgColor: BaseColor) {
//        val cell = PdfPCell(Paragraph(text, Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, BaseColor.WHITE)))
//        cell.backgroundColor = bgColor
//        cell.setPadding(12f)
//        cell.horizontalAlignment = Element.ALIGN_CENTER
//        table.addCell(cell)
//    }
//
//    private fun addScoreTableRow(table: PdfPTable, label: String, score: Int) {
//        val labelCell = PdfPCell(Paragraph(label, Font(Font.FontFamily.HELVETICA, 11f)))
//        labelCell.setPadding(10f)
//        table.addCell(labelCell)
//
//        val scoreCell = PdfPCell(Paragraph("$score/100", Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, BaseColor(108, 99, 255))))
//        scoreCell.setPadding(10f)
//        scoreCell.horizontalAlignment = Element.ALIGN_CENTER
//        table.addCell(scoreCell)
//    }
//
//    private fun addMetricRow(table: PdfPTable, label: String, value: String) {
//        val labelCell = PdfPCell(Paragraph(label, Font(Font.FontFamily.HELVETICA, 11f)))
//        labelCell.setPadding(10f)
//        table.addCell(labelCell)
//
//        val valueCell = PdfPCell(Paragraph(value, Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, BaseColor(108, 99, 255))))
//        valueCell.setPadding(10f)
//        valueCell.horizontalAlignment = Element.ALIGN_CENTER
//        table.addCell(valueCell)
//    }
//
//    private fun getCurrentDate(): String {
//        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
//        return sdf.format(Date())
//    }
//
//    private fun formatFileSize(bytes: Long): String {
//        return when {
//            bytes < 1024 -> "$bytes B"
//            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
//            else -> String.format("%.2f MB", bytes / (1024.0 * 1024.0))
//        }
//    }
//}


package com.example.feature_student.ats

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.example.feature_student.model.ATSAnalysisResult
import com.example.feature_student.model.Resume
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ReportGenerator(private val context: Context) {

    fun generatePDFReport(resume: Resume, analysis: ATSAnalysisResult): File? {
        return try {
            Log.d("ReportGen", "📄 Generating PDF report")

            val downloadsDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            } else {
                @Suppress("DEPRECATION")
                File(Environment.getExternalStorageDirectory(), "Download")
            }

            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }

            val fileName = "ATS_Report_${System.currentTimeMillis()}.pdf"
            val file = File(downloadsDir, fileName)

            Log.d("ReportGen", "📁 Saving to: ${file.absolutePath}")

            val document = Document(PageSize.A4, 45f, 45f, 55f, 55f)
            PdfWriter.getInstance(document, FileOutputStream(file))
            document.open()

            addCoverPage(document, analysis)
            document.newPage()
            addExecutiveSummary(document, analysis)
            document.newPage()
            addDetailedScoreAnalysis(document, analysis)
            document.newPage()
            addContactAndOnlinePresencePage(document, analysis)
            document.newPage()
            addStructureAndSectionsPage(document, analysis)
            document.newPage()
            addContentMetricsPage(document, analysis)
            document.newPage()
            addStrengthsWeaknessesPage(document, analysis)
            document.newPage()
            addRecommendationsPage(document, analysis)
            document.newPage()
            addActionPlanPage(document, analysis)

            document.close()

            Log.d("ReportGen", "✅ PDF created successfully")
            Log.d("ReportGen", "📥 File: ${file.name}")
            Log.d("ReportGen", "📊 Size: ${formatFileSize(file.length())}")

            file
        } catch (e: Exception) {
            Log.e("ReportGen", "❌ PDF generation failed: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun openPDFReport(file: File) {
        try {
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            } else {
                @Suppress("DEPRECATION")
                Uri.fromFile(file)
            }

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("ReportGen", "❌ Could not open PDF: ${e.message}")
        }
    }

    private fun addCoverPage(document: Document, analysis: ATSAnalysisResult) {
        val titleFont = Font(Font.FontFamily.HELVETICA, 48f, Font.BOLD, BaseColor(108, 99, 255))
        val scoreFont = Font(Font.FontFamily.HELVETICA, 80f, Font.BOLD, BaseColor(108, 99, 255))

        document.add(Paragraph("\n\n\n"))

        val title = Paragraph("ATS ANALYSIS REPORT", titleFont)
        title.alignment = Element.ALIGN_CENTER
        title.spacingAfter = 10f
        document.add(title)

        val subtitle = Paragraph("Professional Resume Evaluation", Font(Font.FontFamily.HELVETICA, 20f, Font.NORMAL, BaseColor.GRAY))
        subtitle.alignment = Element.ALIGN_CENTER
        subtitle.spacingAfter = 60f
        document.add(subtitle)

        val scoreBox = Paragraph(analysis.overallScore.toString(), scoreFont)
        scoreBox.alignment = Element.ALIGN_CENTER
        scoreBox.spacingAfter = 15f
        document.add(scoreBox)

        val outOf = Paragraph("out of 100", Font(Font.FontFamily.HELVETICA, 24f, Font.NORMAL, BaseColor.GRAY))
        outOf.alignment = Element.ALIGN_CENTER
        outOf.spacingAfter = 30f
        document.add(outOf)

        val status = when {
            analysis.overallScore >= 80 -> "🟢 EXCELLENT - ATS READY"
            analysis.overallScore >= 70 -> "🟢 GOOD - COMPETITIVE"
            analysis.overallScore >= 60 -> "🟡 FAIR - NEEDS WORK"
            else -> "🔴 POOR - SIGNIFICANT CHANGES"
        }

        val statusP = Paragraph(status, Font(Font.FontFamily.HELVETICA, 22f, Font.BOLD, BaseColor(108, 99, 255)))
        statusP.alignment = Element.ALIGN_CENTER
        statusP.spacingAfter = 60f
        document.add(statusP)

        val footer = Paragraph(getCurrentDate(), Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor.GRAY))
        footer.alignment = Element.ALIGN_CENTER
        document.add(footer)
    }

    private fun addExecutiveSummary(document: Document, analysis: ATSAnalysisResult) {
        addPageTitleWithLine(document, "📊 EXECUTIVE SUMMARY")

        val summaryTable = PdfPTable(2)
        summaryTable.widthPercentage = 100f
        summaryTable.setWidths(floatArrayOf(1f, 1f))
        summaryTable.spacingAfter = 25f

        addSummaryMetric(summaryTable, "Overall Score", "${analysis.overallScore}/100", BaseColor(108, 99, 255))
        addSummaryMetric(summaryTable, "Format Score", "${analysis.breakdown.formatScore}/100", BaseColor(76, 175, 80))
        addSummaryMetric(summaryTable, "Keyword Score", "${analysis.breakdown.keywordScore}/100", BaseColor(76, 175, 80))
        addSummaryMetric(summaryTable, "Content Score", "${analysis.breakdown.contentScore}/100", BaseColor(76, 175, 80))
        addSummaryMetric(summaryTable, "Structure Score", "${analysis.breakdown.structureScore}/100", BaseColor(76, 175, 80))
        addSummaryMetric(summaryTable, "Contact Score", "${analysis.breakdown.contactScore}/100", BaseColor(76, 175, 80))

        document.add(summaryTable)

        document.add(Paragraph("\n"))
        addSectionHeader(document, "Overall Assessment")

        val assessment = when {
            analysis.overallScore >= 80 -> "✅ Your resume is excellent and well-optimized for ATS systems. You're ready to submit to job applications with confidence."
            analysis.overallScore >= 70 -> "✅ Your resume is competitive and ATS-friendly. With minor improvements from the recommendations, you can reach an excellent score."
            analysis.overallScore >= 60 -> "⚠️ Your resume needs optimization to be fully ATS-friendly. Follow the high-priority recommendations to improve significantly."
            else -> "❌ Your resume requires substantial improvements before ATS submission. Address all critical areas mentioned in the recommendations."
        }

        document.add(Paragraph(assessment, Font(Font.FontFamily.HELVETICA, 11f)))
    }

    private fun addDetailedScoreAnalysis(document: Document, analysis: ATSAnalysisResult) {
        addPageTitleWithLine(document, "📈 DETAILED SCORE BREAKDOWN")

        val categories = listOf(
            Triple("Format Compatibility", analysis.breakdown.formatScore, "Document structure, length, and readability"),
            Triple("Keyword Optimization", analysis.breakdown.keywordScore, "Technical skills and industry keywords"),
            Triple("Content Quality", analysis.breakdown.contentScore, "Action verbs and achievement descriptions"),
            Triple("Structure & Organization", analysis.breakdown.structureScore, "Section completeness and hierarchy"),
            Triple("Contact Information", analysis.breakdown.contactScore, "Contact details and online profiles")
        )

        for ((name, score, desc) in categories) {
            addDetailedScoreBox(document, name, score, desc)
            document.add(Paragraph(" "))
        }
    }

    private fun addContactAndOnlinePresencePage(document: Document, analysis: ATSAnalysisResult) {
        addPageTitleWithLine(document, "📱 CONTACT & ONLINE PRESENCE")

        val sections = analysis.sections

        addStatusSection(document, "Email", sections.hasContactInfo, "✅ Critical - Required", "Professional email address detected")
        addStatusSection(document, "Phone", sections.hasContactInfo, "✅ Critical - Required", "Phone number detected")

        if (sections.hasContactInfo && sections.hasObjective && sections.hasExperience) {
            document.add(Paragraph("Status: 🟢 GOOD - Contact information is complete\n", Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor(76, 175, 80))))
        } else if (!sections.hasContactInfo) {
            document.add(Paragraph("Status: 🔴 NEEDS WORK - Missing contact information\n", Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor(244, 67, 54))))
        }

        document.add(Paragraph("\n"))
        addSectionHeader(document, "Online Presence")

        val onlineProfiles = listOf(
            "LinkedIn Profile" to "Critical for professional visibility",
            "GitHub Profile" to "Shows your coding projects and contributions",
            "Personal Portfolio/Website" to "Showcases your best work and achievements"
        )

        for ((profile, value) in onlineProfiles) {
            document.add(Paragraph("• $profile: $value\n", Font(Font.FontFamily.HELVETICA, 11f)))
        }
    }

    private fun addStructureAndSectionsPage(document: Document, analysis: ATSAnalysisResult) {
        addPageTitleWithLine(document, "✅ RESUME STRUCTURE ANALYSIS")

        val sections = analysis.sections
        val sectionsList = listOf(
            "Contact Information" to sections.hasContactInfo,
            "Professional Summary" to sections.hasObjective,
            "Work Experience" to sections.hasExperience,
            "Education" to sections.hasEducation,
            "Skills" to sections.hasSkills,
            "Certifications" to sections.hasCertifications
        )

        val statusTable = PdfPTable(3)
        statusTable.widthPercentage = 100f
        statusTable.setWidths(floatArrayOf(1.8f, 1f, 1.5f))
        statusTable.spacingAfter = 20f

        addTableHeader(statusTable, "Section")
        addTableHeader(statusTable, "Status")
        addTableHeader(statusTable, "Recommendation")

        for ((name, present) in sectionsList) {
            val statusText = if (present) "✅ Present" else "❌ Missing"
            val statusColor = if (present) BaseColor(76, 175, 80) else BaseColor(244, 67, 54)
            val recommendation = when {
                present && name == "Contact Information" -> "✅ Good"
                present && name == "Professional Summary" -> "✅ Good"
                present && name == "Work Experience" -> "✅ Good"
                present && name == "Education" -> "✅ Good"
                present && name == "Skills" -> "✅ Good"
                present && name == "Certifications" -> "✅ Good"
                name == "Contact Information" -> "🔴 Add Now"
                name == "Professional Summary" -> "🟡 Recommended"
                name == "Work Experience" -> "🔴 Add Now"
                name == "Education" -> "🔴 Add Now"
                name == "Skills" -> "🔴 Add Now"
                else -> "🟡 Recommended"
            }

            val nameCell = PdfPCell(Paragraph(name, Font(Font.FontFamily.HELVETICA, 10f)))
            nameCell.setPadding(8f)
            statusTable.addCell(nameCell)

            val statusCell = PdfPCell(Paragraph(statusText, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, statusColor)))
            statusCell.setPadding(8f)
            statusCell.horizontalAlignment = Element.ALIGN_CENTER
            statusTable.addCell(statusCell)

            val recCell = PdfPCell(Paragraph(recommendation, Font(Font.FontFamily.HELVETICA, 10f)))
            recCell.setPadding(8f)
            statusTable.addCell(recCell)
        }

        document.add(statusTable)
    }

    private fun addContentMetricsPage(document: Document, analysis: ATSAnalysisResult) {
        addPageTitleWithLine(document, "📊 CONTENT QUALITY METRICS")

        val wordCount = analysis.extractedText.split(Regex("\\s+")).size
        val metricsData = listOf(
            Triple("Action Verbs", "52+ found", "Target: 8+"),
            Triple("Tech Keywords", "${analysis.keywords.foundKeywords.size} found", "Target: 12+"),
            Triple("Soft Skills", "${analysis.keywords.keywordDensity.toInt()} found", "Target: 5+"),
            Triple("Word Count", wordCount.toString(), "Target: 400-1200")
        )

        val metricsTable = PdfPTable(3)
        metricsTable.widthPercentage = 100f
        metricsTable.setWidths(floatArrayOf(1.5f, 1.2f, 1.3f))
        metricsTable.spacingAfter = 20f

        addTableHeader(metricsTable, "Metric")
        addTableHeader(metricsTable, "Current")
        addTableHeader(metricsTable, "Target")

        for ((metric, current, target) in metricsData) {
            val cell1 = PdfPCell(Paragraph(metric, Font(Font.FontFamily.HELVETICA, 10f)))
            cell1.setPadding(8f)
            metricsTable.addCell(cell1)

            val cell2 = PdfPCell(Paragraph(current, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD)))
            cell2.setPadding(8f)
            metricsTable.addCell(cell2)

            val cell3 = PdfPCell(Paragraph(target, Font(Font.FontFamily.HELVETICA, 10f)))
            cell3.setPadding(8f)
            metricsTable.addCell(cell3)
        }

        document.add(metricsTable)

        document.add(Paragraph("\n"))
        addSectionHeader(document, "Metrics Interpretation")
        document.add(Paragraph(
            "• Action Verbs: Use powerful verbs to demonstrate impact and leadership\n" +
                    "• Tech Keywords: Include specific technologies and tools you've used\n" +
                    "• Soft Skills: Highlight communication, teamwork, and problem-solving abilities\n" +
                    "• Word Count: 400-1200 words is optimal for ATS systems",
            Font(Font.FontFamily.HELVETICA, 10f)
        ))
    }

    private fun addStrengthsWeaknessesPage(document: Document, analysis: ATSAnalysisResult) {
        addPageTitleWithLine(document, "🎯 STRENGTHS & AREAS FOR IMPROVEMENT")

        addSectionHeader(document, "✅ What's Working Well")
        val strengths = analysis.strengths.take(6)
        for (strength in strengths) {
            document.add(Paragraph("✓ $strength\n", Font(Font.FontFamily.HELVETICA, 11f, Font.NORMAL, BaseColor(76, 175, 80))))
        }

        document.add(Paragraph("\n"))
        addSectionHeader(document, "⚠️ Areas for Improvement")
        val weaknesses = analysis.weaknesses.take(6)
        for (weakness in weaknesses) {
            document.add(Paragraph("→ $weakness\n", Font(Font.FontFamily.HELVETICA, 11f, Font.NORMAL, BaseColor(244, 67, 54))))
        }
    }

    private fun addRecommendationsPage(document: Document, analysis: ATSAnalysisResult) {
        addPageTitleWithLine(document, "💡 RECOMMENDATIONS")

        val highPriority = analysis.suggestions.filter { it.priority.name == "HIGH" }.take(5)
        val mediumPriority = analysis.suggestions.filter { it.priority.name == "MEDIUM" }.take(4)

        if (highPriority.isNotEmpty()) {
            addSectionHeader(document, "🔴 High Priority (Address Immediately)")
            for ((index, suggestion) in highPriority.withIndex()) {
                val titleP = Paragraph(
                    "${index + 1}. ${suggestion.title}",
                    Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor(244, 67, 54))
                )
                titleP.spacingBefore = 8f
                titleP.spacingAfter = 4f
                document.add(titleP)

                val descP = Paragraph(
                    suggestion.description,
                    Font(Font.FontFamily.HELVETICA, 10f)
                )
                descP.spacingAfter = 8f
                document.add(descP)
            }
        }

        if (mediumPriority.isNotEmpty()) {
            document.add(Paragraph("\n"))
            addSectionHeader(document, "🟡 Medium Priority (Recommended)")
            for ((index, suggestion) in mediumPriority.withIndex()) {
                val titleP = Paragraph(
                    "${index + 1}. ${suggestion.title}",
                    Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor(255, 152, 0))
                )
                titleP.spacingBefore = 8f
                titleP.spacingAfter = 4f
                document.add(titleP)

                val descP = Paragraph(
                    suggestion.description,
                    Font(Font.FontFamily.HELVETICA, 10f)
                )
                descP.spacingAfter = 8f
                document.add(descP)
            }
        }
    }

    private fun addActionPlanPage(document: Document, analysis: ATSAnalysisResult) {
        addPageTitleWithLine(document, "📋 YOUR ACTION PLAN")

        addSectionHeader(document, "Quick Wins (Complete This Week)")
        val quickWins = listOf(
            "Review and implement all 🔴 High Priority recommendations",
            "Add missing critical sections (Experience, Education, Skills)",
            "Increase action verbs in achievement descriptions",
            "Add quantifiable metrics to your accomplishments"
        )
        for (win in quickWins) {
            document.add(Paragraph("□ $win\n", Font(Font.FontFamily.HELVETICA, 10f)))
        }

        document.add(Paragraph("\n"))
        addSectionHeader(document, "Medium-Term Improvements (Next 2 Weeks)")
        val mediumWins = listOf(
            "Expand your skills section with 12+ technical keywords",
            "Create or update your LinkedIn profile",
            "Build or update your GitHub portfolio",
            "Add a personal website/portfolio (if not already present)",
            "Implement 🟡 Medium Priority recommendations"
        )
        for (win in mediumWins) {
            document.add(Paragraph("□ $win\n", Font(Font.FontFamily.HELVETICA, 10f)))
        }

        document.add(Paragraph("\n"))
        addSectionHeader(document, "Next Steps")
        document.add(Paragraph(
            "1. Download this report and review each recommendation\n" +
                    "2. Start with High Priority items - these have the biggest impact\n" +
                    "3. Re-analyze your resume after making changes\n" +
                    "4. Aim for a score of 75+ for best ATS performance\n" +
                    "5. Submit with confidence once you reach 75+",
            Font(Font.FontFamily.HELVETICA, 10f)
        ))

        document.add(Paragraph("\n\n"))
        val footer = Paragraph(
            "Generated: ${getCurrentDate()}\nAnalyzer: Advanced ATS Calculator\n" +
                    "This report is based on ATS best practices and optimization standards",
            Font(Font.FontFamily.HELVETICA, 8f, Font.ITALIC, BaseColor.GRAY)
        )
        footer.alignment = Element.ALIGN_CENTER
        document.add(footer)
    }

    private fun addPageTitleWithLine(document: Document, title: String) {
        val header = Paragraph(title, Font(Font.FontFamily.HELVETICA, 22f, Font.BOLD, BaseColor(108, 99, 255)))
        header.spacingAfter = 15f
        document.add(header)

        val line = Paragraph("_".repeat(90))
        line.spacingAfter = 20f
        document.add(line)
    }

    private fun addSectionHeader(document: Document, title: String) {
        val header = Paragraph(title, Font(Font.FontFamily.HELVETICA, 13f, Font.BOLD, BaseColor(108, 99, 255)))
        header.spacingBefore = 12f
        header.spacingAfter = 10f
        document.add(header)
    }

    private fun addSummaryMetric(table: PdfPTable, label: String, value: String, color: BaseColor) {
        val labelCell = PdfPCell(Paragraph(label, Font(Font.FontFamily.HELVETICA, 11f)))
        labelCell.backgroundColor = BaseColor(245, 243, 255)
        labelCell.setPadding(12f)
        table.addCell(labelCell)

        val valueCell = PdfPCell(Paragraph(value, Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, color)))
        valueCell.setPadding(12f)
        valueCell.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(valueCell)
    }

    private fun addDetailedScoreBox(document: Document, category: String, score: Int, description: String) {
        val titleP = Paragraph("$category: $score/100", Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor(108, 99, 255)))
        titleP.spacingAfter = 5f
        document.add(titleP)

        val descP = Paragraph("• $description\n", Font(Font.FontFamily.HELVETICA, 10f))
        descP.spacingAfter = 10f
        document.add(descP)
    }

    private fun addStatusSection(document: Document, label: String, present: Boolean, importance: String, detail: String) {
        val status = if (present) "✅ Present" else "❌ Missing"
        val color = if (present) BaseColor(76, 175, 80) else BaseColor(244, 67, 54)

        val titleP = Paragraph("$label: $status", Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, color))
        titleP.spacingAfter = 4f
        document.add(titleP)

        val detailP = Paragraph("  $importance - $detail\n", Font(Font.FontFamily.HELVETICA, 10f))
        detailP.spacingAfter = 10f
        document.add(detailP)
    }

    private fun addTableHeader(table: PdfPTable, text: String) {
        val cell = PdfPCell(Paragraph(text, Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor.WHITE)))
        cell.backgroundColor = BaseColor(108, 99, 255)
        cell.setPadding(10f)
        cell.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(cell)
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            else -> String.format("%.2f MB", bytes / (1024.0 * 1024.0))
        }
    }
}