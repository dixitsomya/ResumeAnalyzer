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
//            Log.d("ReportGen", "📄 Generating Comprehensive PDF Report")
//
//            val downloadsDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//            } else {
//                @Suppress("DEPRECATION")
//                File(Environment.getExternalStorageDirectory(), "Download")
//            }
//
//            if (!downloadsDir.exists()) downloadsDir.mkdirs()
//
//            val fileName = "ATS_Complete_Analysis_${System.currentTimeMillis()}.pdf"
//            val file = File(downloadsDir, fileName)
//
//            val document = Document(PageSize.A4, 50f, 50f, 60f, 60f)
//            PdfWriter.getInstance(document, FileOutputStream(file))
//            document.open()
//
//            // Page 1: Cover Page
//            addCoverPage(document, analysis)
//
//            // Page 2: Executive Summary
//            document.newPage()
//            addExecutiveSummary(document, analysis)
//
//            // Page 3: Overall Score Breakdown
//            document.newPage()
//            addOverallScorePage(document, analysis)
//
//            // Page 4: Resume Structure Analysis
//            document.newPage()
//            addStructureAnalysisPage(document, analysis)
//
//            // Page 5: Contact & Online Presence
//            document.newPage()
//            addContactPresencePage(document, analysis)
//
//            // Page 6: Content Quality Analysis
//            document.newPage()
//            addContentQualityPage(document, analysis)
//
//            // Page 7: Technical Keywords
//            document.newPage()
//            addKeywordsPage(document, analysis)
//
//            // Page 8: Strengths Deep Dive
//            document.newPage()
//            addStrengthsDetailPage(document, analysis)
//
//            // Page 9: Weaknesses & Gaps
//            document.newPage()
//            addWeaknessesDetailPage(document, analysis)
//
//            // Page 10: High Priority Recommendations
//            document.newPage()
//            addHighPriorityPage(document, analysis)
//
//            // Page 11: Medium Priority Recommendations
//            document.newPage()
//            addMediumPriorityPage(document, analysis)
//
//            // Page 12: Action Plan
//            document.newPage()
//            addActionPlanPage(document, analysis)
//
//            document.close()
//
//            Log.d("ReportGen", "✅ Comprehensive PDF created: ${file.name}")
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
//                @Suppress("DEPRECATION")
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
//    // ============= PAGE 1: COVER PAGE =============
//    private fun addCoverPage(document: Document, analysis: ATSAnalysisResult) {
//        document.add(Paragraph("\n\n\n\n"))
//
//        val titleFont = Font(Font.FontFamily.HELVETICA, 52f, Font.BOLD, BaseColor(108, 99, 255))
//        val title = Paragraph("ATS RESUME ANALYSIS", titleFont)
//        title.alignment = Element.ALIGN_CENTER
//        title.spacingAfter = 20f
//        document.add(title)
//
//        val subtitleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.NORMAL, BaseColor.GRAY)
//        val subtitle = Paragraph("Complete Professional Assessment Report", subtitleFont)
//        subtitle.alignment = Element.ALIGN_CENTER
//        subtitle.spacingAfter = 80f
//        document.add(subtitle)
//
//        // Big Score
//        val scoreFont = Font(Font.FontFamily.HELVETICA, 96f, Font.BOLD, BaseColor(108, 99, 255))
//        val score = Paragraph(analysis.overallScore.toString(), scoreFont)
//        score.alignment = Element.ALIGN_CENTER
//        score.spacingAfter = 15f
//        document.add(score)
//
//        val scoreOutOf = Paragraph("/ 100", Font(Font.FontFamily.HELVETICA, 28f, Font.NORMAL, BaseColor.GRAY))
//        scoreOutOf.alignment = Element.ALIGN_CENTER
//        scoreOutOf.spacingAfter = 50f
//        document.add(scoreOutOf)
//
//        // Status Badge
//        val statusText = when {
//            analysis.overallScore >= 80 -> "✅ EXCELLENT - ATS READY"
//            analysis.overallScore >= 70 -> "✅ GOOD - COMPETITIVE"
//            analysis.overallScore >= 60 -> "⚠️ FAIR - NEEDS IMPROVEMENT"
//            else -> "🔴 POOR - MAJOR WORK NEEDED"
//        }
//        val statusColor = when {
//            analysis.overallScore >= 80 -> BaseColor(76, 175, 80)
//            analysis.overallScore >= 70 -> BaseColor(76, 175, 80)
//            analysis.overallScore >= 60 -> BaseColor(255, 152, 0)
//            else -> BaseColor(244, 67, 54)
//        }
//
//        val status = Paragraph(statusText, Font(Font.FontFamily.HELVETICA, 24f, Font.BOLD, statusColor))
//        status.alignment = Element.ALIGN_CENTER
//        status.spacingAfter = 80f
//        document.add(status)
//
//        val dateP = Paragraph(getCurrentDate(), Font(Font.FontFamily.HELVETICA, 10f, Font.ITALIC, BaseColor.GRAY))
//        dateP.alignment = Element.ALIGN_CENTER
//        document.add(dateP)
//    }
//
//    // ============= PAGE 2: EXECUTIVE SUMMARY =============
//    private fun addExecutiveSummary(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "📊 EXECUTIVE SUMMARY")
//
//        // Key Metrics
//        val metricsTable = PdfPTable(4)
//        metricsTable.widthPercentage = 100f
//        metricsTable.setWidths(floatArrayOf(1f, 1f, 1f, 1f))
//        metricsTable.spacingAfter = 20f
//
//        addMetricBox(metricsTable, "Score", "${analysis.overallScore}/100")
//        addMetricBox(metricsTable, "Status", getStatusEmoji(analysis.overallScore))
//        addMetricBox(metricsTable, "Sections", countSections(analysis).toString())
//        addMetricBox(metricsTable, "Keywords", "${analysis.keywords.foundKeywords.size}")
//
//        document.add(metricsTable)
//
//        document.add(Paragraph("\n"))
//        addSubHeader(document, "Quick Assessment")
//
//        val assessment = when {
//            analysis.overallScore >= 80 -> "🟢 Your resume is excellently optimized and ready for ATS submission. You have all critical sections, strong keywords, and good content quality."
//            analysis.overallScore >= 70 -> "🟢 Your resume is competitive with good ATS compatibility. Minor improvements will push it to excellent status."
//            analysis.overallScore >= 60 -> "🟡 Your resume has potential but needs optimization. Follow the recommendations to improve significantly."
//            else -> "🔴 Your resume requires substantial improvements before being ATS-ready. Address high-priority items immediately."
//        }
//
//        document.add(Paragraph(assessment, Font(Font.FontFamily.HELVETICA, 11f, Font.NORMAL)))
//    }
//
//    // ============= PAGE 3: OVERALL SCORE BREAKDOWN =============
//    private fun addOverallScorePage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "📈 SCORE BREAKDOWN ANALYSIS")
//
//        val breakdown = analysis.breakdown
//
//        addScoreDetail(document, "Format Compatibility", breakdown.formatScore,
//            "How well your resume is formatted and structured for ATS parsing")
//        addScoreDetail(document, "Keyword Optimization", breakdown.keywordScore,
//            "Technical skills and industry keywords coverage")
//        addScoreDetail(document, "Content Quality", breakdown.contentScore,
//            "Action verbs, metrics, and achievement descriptions")
//        addScoreDetail(document, "Structure & Organization", breakdown.structureScore,
//            "Presence of all essential sections and hierarchy")
//        addScoreDetail(document, "Contact Information", breakdown.contactScore,
//            "Complete contact details and online profiles")
//    }
//
//    // ============= PAGE 4: STRUCTURE ANALYSIS =============
//    private fun addStructureAnalysisPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "✅ RESUME STRUCTURE ANALYSIS")
//
//        val sections = analysis.sections
//
//        val structureTable = PdfPTable(3)
//        structureTable.widthPercentage = 100f
//        structureTable.setWidths(floatArrayOf(2f, 1f, 1.5f))
//        structureTable.spacingAfter = 20f
//
//        addTableHeader(structureTable, "Section")
//        addTableHeader(structureTable, "Status")
//        addTableHeader(structureTable, "Priority")
//
//        addStructureRow(structureTable, "Contact Information", sections.hasContactInfo, "CRITICAL")
//        addStructureRow(structureTable, "Professional Summary", sections.hasObjective, "HIGH")
//        addStructureRow(structureTable, "Work Experience", sections.hasExperience, "CRITICAL")
//        addStructureRow(structureTable, "Education", sections.hasEducation, "CRITICAL")
//        addStructureRow(structureTable, "Skills", sections.hasSkills, "CRITICAL")
//        addStructureRow(structureTable, "Certifications", sections.hasCertifications, "MEDIUM")
//
//        document.add(structureTable)
//
//        document.add(Paragraph("\n"))
//        addSubHeader(document, "Section Details")
//
//        if (!sections.hasContactInfo) {
//            addWarning(document, "Contact Information", "Email and phone number are missing. This is critical for recruiters to reach you.")
//        }
//        if (!sections.hasExperience) {
//            addWarning(document, "Work Experience", "No work history section found. This is essential to showcase your professional background.")
//        }
//        if (!sections.hasEducation) {
//            addWarning(document, "Education", "Education section is missing. Include your degree, university, and graduation date.")
//        }
//        if (!sections.hasSkills) {
//            addWarning(document, "Skills", "Dedicated skills section not found. Add 25-35 relevant technical and soft skills.")
//        }
//    }
//
//    // ============= PAGE 5: CONTACT & ONLINE PRESENCE =============
//    private fun addContactPresencePage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "📱 CONTACT & ONLINE PRESENCE")
//
//        addSubHeader(document, "Contact Information")
//        addCheckItem(document, "Professional Email", "✅", "Critical for recruiter contact")
//        addCheckItem(document, "Phone Number", "✅", "Essential contact method")
//
//        document.add(Paragraph("\n"))
//        addSubHeader(document, "Online Profiles")
//
//        val profiles = listOf(
//            Triple("LinkedIn Profile", "Critical", "linkedin.com/in/yourprofile"),
//            Triple("GitHub Portfolio", "Highly Important", "github.com/yourprofile"),
//            Triple("Personal Website", "Beneficial", "yourname.com or portfolio site")
//        )
//
//        for ((name, importance, example) in profiles) {
//            val p = Paragraph(
//                "🔗 $name ($importance)\n   Example: $example\n",
//                Font(Font.FontFamily.HELVETICA, 10f)
//            )
//            p.spacingAfter = 8f
//            document.add(p)
//        }
//    }
//
//    // ============= PAGE 6: CONTENT QUALITY =============
//    private fun addContentQualityPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "📝 CONTENT QUALITY ANALYSIS")
//
//        val wordCount = analysis.extractedText.split(Regex("\\s+")).size
//
//        val qualityTable = PdfPTable(3)
//        qualityTable.widthPercentage = 100f
//        qualityTable.setWidths(floatArrayOf(1.8f, 1f, 1.2f))
//        qualityTable.spacingAfter = 20f
//
//        addTableHeader(qualityTable, "Metric")
//        addTableHeader(qualityTable, "Current")
//        addTableHeader(qualityTable, "Target")
//
//        addQualityRow(qualityTable, "Word Count", wordCount.toString(), "400-1200")
//        addQualityRow(qualityTable, "Action Verbs", "Multiple", "8+")
//        addQualityRow(qualityTable, "Quantified Metrics", "Present", "4 types")
//        addQualityRow(qualityTable, "Tech Keywords", "${analysis.keywords.foundKeywords.size}", "12+")
//
//        document.add(qualityTable)
//
//        document.add(Paragraph("\n"))
//        addSubHeader(document, "Content Recommendations")
//
//        val recommendations = listOf(
//            "Use strong action verbs: Led, Architected, Optimized, Transformed, Innovated",
//            "Include quantifiable metrics: percentages, money amounts, team sizes, years",
//            "Maintain ideal word count (400-1200) for ATS optimization",
//            "Add specific technologies and tools you've worked with",
//            "Focus on achievements and impact, not just responsibilities"
//        )
//
//        for ((index, rec) in recommendations.withIndex()) {
//            document.add(Paragraph("${index + 1}. $rec\n", Font(Font.FontFamily.HELVETICA, 10f)))
//        }
//    }
//
//    // ============= PAGE 7: KEYWORDS =============
//    private fun addKeywordsPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "🔑 TECHNICAL KEYWORDS ANALYSIS")
//
//        addSubHeader(document, "Found Keywords (${analysis.keywords.foundKeywords.size})")
//
//        val foundText = analysis.keywords.foundKeywords.take(20).joinToString(", ")
//        val foundP = Paragraph(foundText, Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor(76, 175, 80)))
//        foundP.spacingAfter = 20f
//        document.add(foundP)
//
//        addSubHeader(document, "Missing Keywords (Recommended)")
//
//        val missingText = analysis.keywords.missingKeywords.take(15).joinToString(", ")
//        val missingP = Paragraph(missingText, Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor(244, 67, 54)))
//        missingP.spacingAfter = 20f
//        document.add(missingP)
//
//        document.add(Paragraph("\n"))
//        addSubHeader(document, "Why Keywords Matter")
//
//        val keywordExplain = "ATS systems scan resumes for specific keywords that match job descriptions. Including relevant technical keywords increases the chances of your resume passing through ATS filters and reaching recruiters."
//        document.add(Paragraph(keywordExplain, Font(Font.FontFamily.HELVETICA, 10f)))
//    }
//
//    // ============= PAGE 8: STRENGTHS DETAIL =============
//    private fun addStrengthsDetailPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "⭐ YOUR STRENGTHS")
//
//        for ((index, strength) in analysis.strengths.withIndex()) {
//            val titleP = Paragraph(
//                "${index + 1}. ✅ ${strength}",
//                Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor(76, 175, 80))
//            )
//            titleP.spacingBefore = 8f
//            titleP.spacingAfter = 4f
//            document.add(titleP)
//        }
//
//        document.add(Paragraph("\n"))
//        addSubHeader(document, "How to Maintain Strengths")
//
//        val maintain = listOf(
//            "Keep using strong action verbs and quantifiable metrics",
//            "Maintain all critical sections (Experience, Education, Skills)",
//            "Update your online profiles regularly",
//            "Continuously add new skills and certifications",
//            "Use industry-relevant keywords consistently"
//        )
//
//        for (item in maintain) {
//            document.add(Paragraph("• $item\n", Font(Font.FontFamily.HELVETICA, 10f)))
//        }
//    }
//
//    // ============= PAGE 9: WEAKNESSES DETAIL =============
//    private fun addWeaknessesDetailPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "⚠️ AREAS FOR IMPROVEMENT")
//
//        for ((index, weakness) in analysis.weaknesses.withIndex()) {
//            val titleP = Paragraph(
//                "${index + 1}. 🔴 ${weakness}",
//                Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor(244, 67, 54))
//            )
//            titleP.spacingBefore = 8f
//            titleP.spacingAfter = 4f
//            document.add(titleP)
//        }
//
//        document.add(Paragraph("\n"))
//        addSubHeader(document, "Impact on ATS Score")
//
//        val impact = "Each weakness identified above reduces your ATS compatibility score. " +
//                "Addressing these issues will significantly improve your resume's chances of passing ATS filters " +
//                "and reaching hiring managers."
//        document.add(Paragraph(impact, Font(Font.FontFamily.HELVETICA, 10f)))
//    }
//
//    // ============= PAGE 10: HIGH PRIORITY =============
//    private fun addHighPriorityPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "🔴 HIGH PRIORITY RECOMMENDATIONS")
//
//        val highPriority = analysis.suggestions.filter { it.priority.name == "HIGH" }
//
//        if (highPriority.isEmpty()) {
//            document.add(Paragraph("✅ No high-priority issues found!", Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor(76, 175, 80))))
//            return
//        }
//
//        for ((index, suggestion) in highPriority.take(8).withIndex()) {
//            addRecommendationBox(document, index + 1, suggestion.title, suggestion.description, true)
//        }
//    }
//
//    // ============= PAGE 11: MEDIUM PRIORITY =============
//    private fun addMediumPriorityPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "🟡 MEDIUM PRIORITY RECOMMENDATIONS")
//
//        val mediumPriority = analysis.suggestions.filter { it.priority.name == "MEDIUM" }
//
//        if (mediumPriority.isEmpty()) {
//            document.add(Paragraph("✅ No medium-priority items!", Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor(76, 175, 80))))
//            return
//        }
//
//        for ((index, suggestion) in mediumPriority.take(6).withIndex()) {
//            addRecommendationBox(document, index + 1, suggestion.title, suggestion.description, false)
//        }
//    }
//
//    // ============= PAGE 12: ACTION PLAN =============
//    private fun addActionPlanPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "📋 YOUR ACTION PLAN")
//
//        addSubHeader(document, "This Week (Quick Wins)")
//        val quickWins = listOf(
//            "Review all 🔴 HIGH PRIORITY recommendations",
//            "Fix missing critical sections",
//            "Add strong action verbs to achievements",
//            "Insert quantifiable metrics (%, $, team size, years)"
//        )
//        for ((i, win) in quickWins.withIndex()) {
//            document.add(Paragraph("${i + 1}. ☐ $win\n", Font(Font.FontFamily.HELVETICA, 10f)))
//        }
//
//        document.add(Paragraph("\n"))
//        addSubHeader(document, "Next 2 Weeks (Medium Priority)")
//        val mediumPlan = listOf(
//            "Expand technical keywords (target: 12+)",
//            "Create/update LinkedIn profile",
//            "Build GitHub portfolio",
//            "Implement 🟡 MEDIUM PRIORITY items"
//        )
//        for ((i, plan) in mediumPlan.withIndex()) {
//            document.add(Paragraph("${i + 1}. ☐ $plan\n", Font(Font.FontFamily.HELVETICA, 10f)))
//        }
//
//        document.add(Paragraph("\n"))
//        addSubHeader(document, "Next Steps")
//        val steps = when {
//            analysis.overallScore >= 80 -> "Your resume is already excellent! Keep it updated and start applying confidently."
//            analysis.overallScore >= 70 -> "Make high-priority changes and re-analyze to reach 80+. You're very close!"
//            else -> "Address all high-priority items first, then medium-priority. Target: 75+ for best results."
//        }
//        document.add(Paragraph(steps, Font(Font.FontFamily.HELVETICA, 10f)))
//
//        document.add(Paragraph("\n\n"))
//        val footer = Paragraph(
//            "Report Generated: ${getCurrentDate()}\nAnalyzer: Comprehensive ATS Calculator\n" +
//                    "Based on ATS best practices and industry standards",
//            Font(Font.FontFamily.HELVETICA, 8f, Font.ITALIC, BaseColor.GRAY)
//        )
//        footer.alignment = Element.ALIGN_CENTER
//        document.add(footer)
//    }
//
//    // ============= HELPER FUNCTIONS =============
//
//    private fun addPageHeader(document: Document, title: String) {
//        val header = Paragraph(title, Font(Font.FontFamily.HELVETICA, 24f, Font.BOLD, BaseColor(108, 99, 255)))
//        header.spacingAfter = 15f
//        document.add(header)
//
//        val line = Paragraph("═".repeat(95))
//        line.spacingAfter = 20f
//        document.add(line)
//    }
//
//    private fun addSubHeader(document: Document, title: String) {
//        val header = Paragraph(title, Font(Font.FontFamily.HELVETICA, 13f, Font.BOLD, BaseColor(108, 99, 255)))
//        header.spacingBefore = 12f
//        header.spacingAfter = 10f
//        document.add(header)
//    }
//
//    private fun addMetricBox(table: PdfPTable, label: String, value: String) {
//        val cell = PdfPCell().apply {
//            backgroundColor = BaseColor(245, 243, 255)
//            borderColor = BaseColor(108, 99, 255)
//            borderWidth = 1.5f
//            setPadding(12f)
//
//            val labelP = Paragraph(label, Font(Font.FontFamily.HELVETICA, 10f, Font.ITALIC, BaseColor.GRAY))
//            val valueP = Paragraph(value, Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD, BaseColor(108, 99, 255)))
//
//            addElement(labelP)
//            addElement(valueP)
//        }
//        cell.horizontalAlignment = Element.ALIGN_CENTER
//        table.addCell(cell)
//    }
//
//    private fun addScoreDetail(document: Document, category: String, score: Int, description: String) {
//        val categoryP = Paragraph(category, Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, BaseColor(108, 99, 255)))
//        categoryP.spacingAfter = 4f
//        document.add(categoryP)
//
//        val scoreBar = Paragraph(
//            "Score: $score/100 " + "█".repeat((score / 5)) + "░".repeat((100 - score) / 5),
//            Font(Font.FontFamily.HELVETICA, 10f)
//        )
//        scoreBar.spacingAfter = 4f
//        document.add(scoreBar)
//
//        val descP = Paragraph(description, Font(Font.FontFamily.HELVETICA, 9f, Font.ITALIC, BaseColor.GRAY))
//        descP.spacingAfter = 12f
//        document.add(descP)
//    }
//
//    private fun addStructureRow(table: PdfPTable, section: String, present: Boolean, priority: String) {
//        val status = if (present) "✅ Present" else "❌ Missing"
//        val statusColor = if (present) BaseColor(76, 175, 80) else BaseColor(244, 67, 54)
//
//        val cell1 = PdfPCell(Paragraph(section, Font(Font.FontFamily.HELVETICA, 10f)))
//        cell1.setPadding(10f)
//        table.addCell(cell1)
//
//        val cell2 = PdfPCell(Paragraph(status, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, statusColor)))
//        cell2.setPadding(10f)
//        cell2.horizontalAlignment = Element.ALIGN_CENTER
//        table.addCell(cell2)
//
//        val cell3 = PdfPCell(Paragraph(priority, Font(Font.FontFamily.HELVETICA, 10f)))
//        cell3.setPadding(10f)
//        table.addCell(cell3)
//    }
//
//    private fun addCheckItem(document: Document, item: String, status: String, detail: String) {
//        val p = Paragraph(
//            "$status $item\n   └─ $detail\n",
//            Font(Font.FontFamily.HELVETICA, 10f)
//        )
//        p.spacingAfter = 8f
//        document.add(p)
//    }
//
//    private fun addQualityRow(table: PdfPTable, metric: String, current: String, target: String) {
//        val cell1 = PdfPCell(Paragraph(metric, Font(Font.FontFamily.HELVETICA, 10f)))
//        cell1.setPadding(10f)
//        table.addCell(cell1)
//
//        val cell2 = PdfPCell(Paragraph(current, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD)))
//        cell2.setPadding(10f)
//        cell2.horizontalAlignment = Element.ALIGN_CENTER
//        table.addCell(cell2)
//
//        val cell3 = PdfPCell(Paragraph(target, Font(Font.FontFamily.HELVETICA, 10f, Font.ITALIC)))
//        cell3.setPadding(10f)
//        cell3.horizontalAlignment = Element.ALIGN_CENTER
//        table.addCell(cell3)
//    }
//
//    private fun addRecommendationBox(document: Document, number: Int, title: String, description: String, isHigh: Boolean) {
//        val color = if (isHigh) BaseColor(244, 67, 54) else BaseColor(255, 152, 0)
//
//        val titleP = Paragraph(
//            "$number. $title",
//            Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, color)
//        )
//        titleP.spacingBefore = 8f
//        titleP.spacingAfter = 4f
//        document.add(titleP)
//
//        val descP = Paragraph(
//            "→ $description\n",
//            Font(Font.FontFamily.HELVETICA, 10f)
//        )
//        descP.spacingAfter = 10f
//        document.add(descP)
//    }
//
//    private fun addWarning(document: Document, section: String, message: String) {
//        val p = Paragraph(
//            "⚠️ $section\n   $message\n",
//            Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor(244, 67, 54))
//        )
//        p.spacingAfter = 10f
//        document.add(p)
//    }
//
//    private fun addTableHeader(table: PdfPTable, text: String) {
//        val cell = PdfPCell(Paragraph(text, Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor.WHITE)))
//        cell.backgroundColor = BaseColor(108, 99, 255)
//        cell.setPadding(10f)
//        cell.horizontalAlignment = Element.ALIGN_CENTER
//        table.addCell(cell)
//    }
//
//    private fun getStatusEmoji(score: Int): String = when {
//        score >= 80 -> "🟢 Excellent"
//        score >= 70 -> "🟢 Good"
//        score >= 60 -> "🟡 Fair"
//        else -> "🔴 Poor"
//    }
//
//    private fun countSections(analysis: ATSAnalysisResult): Int {
//        var count = 0
//        if (analysis.sections.hasContactInfo) count++
//        if (analysis.sections.hasObjective) count++
//        if (analysis.sections.hasExperience) count++
//        if (analysis.sections.hasEducation) count++
//        if (analysis.sections.hasSkills) count++
//        if (analysis.sections.hasCertifications) count++
//        return count
//    }
//
//    private fun getCurrentDate(): String {
//        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
//        return sdf.format(Date())
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
//import android.os.Handler
//import android.os.Looper
//
//class ReportGenerator(private val context: Context) {
//
//    private val primaryColor = BaseColor(108, 99, 255)
//    private val successColor = BaseColor(76, 175, 80)
//    private val warningColor = BaseColor(255, 152, 0)
//    private val dangerColor = BaseColor(244, 67, 54)
//    private val lightGray = BaseColor(240, 240, 240)
//
//    fun generatePDFReport(resume: Resume, analysis: ATSAnalysisResult): File? {
//        return try {
//            Log.d("ReportGen", "📄 Starting PDF Report Generation")
//
//            // Step 1: Get Downloads Directory - Try multiple approaches
//            val downloadsDir = getDownloadsDirectory()
//
//            if (downloadsDir == null || !downloadsDir.exists()) {
//                Log.e("ReportGen", "❌ Failed to get or create downloads directory")
//                return null
//            }
//
//            Log.d("ReportGen", "📁 Downloads Directory: ${downloadsDir.absolutePath}")
//            Log.d("ReportGen", "📁 Directory exists: ${downloadsDir.exists()}")
//            Log.d("ReportGen", "📁 Can write: ${downloadsDir.canWrite()}")
//
//            // Step 2: Create unique filename
//            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//            val fileName = "ATS_Report_$timeStamp.pdf"
//            val file = File(downloadsDir, fileName)
//
//            Log.d("ReportGen", "📝 Creating file: ${file.absolutePath}")
//
//            // Step 3: Create the PDF
//            val document = Document(PageSize.A4, 40f, 40f, 50f, 50f)
//            val fileOutputStream = FileOutputStream(file)
//            PdfWriter.getInstance(document, fileOutputStream)
//            document.open()
//
//            // Add all pages
//            addCoverPage(document, analysis)
//            document.newPage()
//            addExecutiveSummary(document, analysis)
//            document.newPage()
//            addScoreBreakdownPage(document, analysis)
//            document.newPage()
//            addResumeStructurePage(document, analysis)
//            document.newPage()
//            addContentQualityPage(document, analysis)
//            document.newPage()
//            addContactPresencePage(document, analysis)
//            document.newPage()
//            addStrengthsPage(document, analysis)
//            document.newPage()
//            addWeaknessesPage(document, analysis)
//            document.newPage()
//            addRecommendationsPage(document, analysis)
//            document.newPage()
//            addActionPlanPage(document, analysis)
//
////            document.close()
////            fileOutputStream.close()
////
////            // Step 4: Verify file was created
////            if (file.exists()) {
////                Log.d("ReportGen", "✅ PDF created successfully")
////                Log.d("ReportGen", "📥 File: ${file.name}")
////                Log.d("ReportGen", "📊 Size: ${formatFileSize(file.length())}")
////                Log.d("ReportGen", "📂 Full Path: ${file.absolutePath}")
////                return file
////            } else {
////                Log.e("ReportGen", "❌ File was not created")
////                return null
////            }
//            document.close()
//            fileOutputStream.close()
//
//            // Step 4: Verify file was created
//            if (file.exists()) {
//                Log.d("ReportGen", "✅ PDF created successfully")
//                Log.d("ReportGen", "📥 File: ${file.name}")
//                Log.d("ReportGen", "📊 Size: ${formatFileSize(file.length())}")
//                Log.d("ReportGen", "📂 Full Path: ${file.absolutePath}")
//
//                // 🔥 AUTO-OPEN REPORT AFTER 500ms DELAY
//                Handler(Looper.getMainLooper()).postDelayed({
//                    openPDFReport(file)
//                }, 500)
//
//                return file
//            } else {
//                Log.e("ReportGen", "❌ File was not created")
//                return null
//            }
//
//        } catch (e: Exception) {
//            Log.e("ReportGen", "❌ PDF generation failed: ${e.message}")
//            Log.e("ReportGen", "❌ Stack trace: ${e.stackTraceToString()}")
//            e.printStackTrace()
//            return null
//        }
//    }
//
//    private fun getDownloadsDirectory(): File? {
//        return try {
//            val downloadsDir = when {
//                // Android 10+ (API 29+)
//                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
//                    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    dir
//                }
//                // Android 6-9
//                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
//                    @Suppress("DEPRECATION")
//                    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    dir
//                }
//                // Below Android 6
//                else -> {
//                    @Suppress("DEPRECATION")
//                    File(Environment.getExternalStorageDirectory(), "Download")
//                }
//            }
//
//            // Ensure directory exists
//            if (!downloadsDir.exists()) {
//                val created = downloadsDir.mkdirs()
//                Log.d("ReportGen", "📁 Directory creation result: $created")
//            }
//
//            downloadsDir
//        } catch (e: Exception) {
//            Log.e("ReportGen", "❌ Error getting downloads directory: ${e.message}")
//            null
//        }
//    }
//
////    fun openPDFReport(file: File) {
////        try {
////            Log.d("ReportGen", "📖 Opening PDF: ${file.absolutePath}")
////
////            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                try {
////                    FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
////                } catch (e: Exception) {
////                    Log.e("ReportGen", "❌ FileProvider error: ${e.message}")
////                    @Suppress("DEPRECATION")
////                    Uri.fromFile(file)
////                }
////            } else {
////                @Suppress("DEPRECATION")
////                Uri.fromFile(file)
////            }
////
////            Log.d("ReportGen", "📱 URI: $uri")
////
////            val intent = Intent(Intent.ACTION_VIEW).apply {
////                setDataAndType(uri, "application/pdf")
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
////                }
////                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////            }
////
////            // Verify intent can be resolved
////            val resolveInfo = context.packageManager.resolveActivity(intent, 0)
////            if (resolveInfo != null) {
////                context.startActivity(intent)
////                Log.d("ReportGen", "✅ PDF opened successfully")
////            } else {
////                Log.e("ReportGen", "❌ No PDF viewer app found")
////            }
////        } catch (e: Exception) {
////            Log.e("ReportGen", "❌ Could not open PDF: ${e.message}")
////            e.printStackTrace()
////        }
////    }
//fun openPDFReport(file: File) {
//    try {
//        Log.d("ReportGen", "📖 Opening PDF: ${file.absolutePath}")
//        Log.d("ReportGen", "📁 File exists: ${file.exists()}")
//        Log.d("ReportGen", "📊 File size: ${file.length()} bytes")
//
//        // Step 1: Get URI using FileProvider (Android 7+)
//        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            try {
//                val authority = "${context.packageName}.fileprovider"
//                Log.d("ReportGen", "🔑 Using FileProvider authority: $authority")
//                FileProvider.getUriForFile(context, authority, file)
//            } catch (e: IllegalArgumentException) {
//                Log.e("ReportGen", "❌ FileProvider path error: ${e.message}")
//                Log.e("ReportGen", "❌ This usually means file_paths.xml config is wrong")
//                return
//            } catch (e: Exception) {
//                Log.e("ReportGen", "❌ FileProvider error: ${e.message}")
//                return
//            }
//        } else {
//            @Suppress("DEPRECATION")
//            Uri.fromFile(file)
//        }
//
//        Log.d("ReportGen", "📱 Generated URI: $uri")
//
//        // Step 2: Create Intent
//        val intent = Intent(Intent.ACTION_VIEW).apply {
//            setDataAndType(uri, "application/pdf")
//
//            // Add permissions for Android 7+
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            }
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        }
//
//        // Step 3: Check if any app can handle PDF
//        val resolveInfo = context.packageManager.resolveActivity(intent, 0)
//        if (resolveInfo != null) {
//            Log.d("ReportGen", "✅ PDF viewer found: ${resolveInfo.activityInfo.packageName}")
//            context.startActivity(intent)
//            Log.d("ReportGen", "✅ PDF opened successfully")
//        } else {
//            Log.e("ReportGen", "❌ No PDF viewer app found")
//            Log.d("ReportGen", "💡 Trying alternative approach...")
//
//            // Fallback: Try Chrome
//            tryOpenWithChrome(uri)
//        }
//
//    } catch (e: Exception) {
//        Log.e("ReportGen", "❌ Error opening PDF: ${e.message}")
//        Log.e("ReportGen", "❌ Stack: ${e.stackTraceToString()}")
//        e.printStackTrace()
//    }
//}
//
//    // Fallback function - Chrome se kholo
//    private fun tryOpenWithChrome(uri: Uri) {
//        try {
//            val chromeIntent = Intent(Intent.ACTION_VIEW).apply {
//                setData(uri)
//                setPackage("com.android.chrome")
//                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//
//            val chromeInfo = context.packageManager.resolveActivity(chromeIntent, 0)
//            if (chromeInfo != null) {
//                Log.d("ReportGen", "✅ Opening with Chrome")
//                context.startActivity(chromeIntent)
//            } else {
//                Log.d("ReportGen", "⚠️ Chrome not available either")
//                showFileLocation(uri)
//            }
//        } catch (e: Exception) {
//            Log.e("ReportGen", "❌ Chrome fallback failed: ${e.message}")
//            showFileLocation(uri)
//        }
//    }
//
//    // Last resort - File explorer kholo
//    private fun showFileLocation(uri: Uri) {
//        try {
//            Log.d("ReportGen", "📂 Opening file explorer...")
//            val intent = Intent(Intent.ACTION_VIEW).apply {
//                setData(Uri.parse("content://com.android.externalstorage.documents/document/primary%3ADownload"))
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//            context.startActivity(intent)
//        } catch (e: Exception) {
//            Log.e("ReportGen", "❌ Couldn't open file manager: ${e.message}")
//        }
//    }
//
//    private fun addCoverPage(document: Document, analysis: ATSAnalysisResult) {
//        document.add(Paragraph("\n\n\n"))
//
//        val titleFont = Font(Font.FontFamily.HELVETICA, 48f, Font.BOLD, primaryColor)
//        val title = Paragraph("ATS ANALYSIS REPORT", titleFont)
//        title.alignment = Element.ALIGN_CENTER
//        title.spacingAfter = 10f
//        document.add(title)
//
//        val subtitleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.NORMAL, BaseColor.GRAY)
//        val subtitle = Paragraph("Professional Resume Evaluation & Optimization", subtitleFont)
//        subtitle.alignment = Element.ALIGN_CENTER
//        subtitle.spacingAfter = 80f
//        document.add(subtitle)
//
//        val scoreFont = Font(Font.FontFamily.HELVETICA, 96f, Font.BOLD, primaryColor)
//        val scoreP = Paragraph(analysis.overallScore.toString(), scoreFont)
//        scoreP.alignment = Element.ALIGN_CENTER
//        scoreP.spacingAfter = 5f
//        document.add(scoreP)
//
//        val outOfFont = Font(Font.FontFamily.HELVETICA, 24f, Font.NORMAL, BaseColor.GRAY)
//        val outOfP = Paragraph("out of 100", outOfFont)
//        outOfP.alignment = Element.ALIGN_CENTER
//        outOfP.spacingAfter = 40f
//        document.add(outOfP)
//
//        val (statusEmoji, statusText, statusColor) = getStatusInfo(analysis.overallScore)
//        val statusFont = Font(Font.FontFamily.HELVETICA, 28f, Font.BOLD, statusColor)
//        val statusP = Paragraph("$statusEmoji $statusText", statusFont)
//        statusP.alignment = Element.ALIGN_CENTER
//        statusP.spacingAfter = 100f
//        document.add(statusP)
//
//        val footerFont = Font(Font.FontFamily.HELVETICA, 10f, Font.ITALIC, BaseColor.GRAY)
//        val footerP = Paragraph(
//            "Generated: ${getCurrentDate()}\nAnalysis by: Advanced ATS Calculator",
//            footerFont
//        )
//        footerP.alignment = Element.ALIGN_CENTER
//        document.add(footerP)
//    }
//
//    private fun addExecutiveSummary(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "📊 EXECUTIVE SUMMARY")
//
//        val wordCount = analysis.extractedText.split(Regex("\\s+")).size
//        val (_, statusText, statusColor) = getStatusInfo(analysis.overallScore)
//
//        addInfoBox(document, statusText, analysis.overallScore, statusColor)
//
//        document.add(Paragraph("\n"))
//        addSectionTitle(document, "📈 Overall Assessment")
//
//        val assessment = when {
//            analysis.overallScore >= 85 -> "🟢 EXCELLENT: Your resume is ATS-optimized and ready for submission. You have all critical components with strong keyword coverage and metrics."
//            analysis.overallScore >= 75 -> "🟢 GOOD: Your resume is competitive. Minor enhancements in keywords and metrics will push you to an excellent score."
//            analysis.overallScore >= 65 -> "🟡 FAIR: Your resume needs optimization. Focus on adding metrics, keywords, and strengthening action verbs."
//            analysis.overallScore >= 55 -> "🟡 NEEDS WORK: Several sections need improvement. Prioritize adding missing sections and quantifiable metrics."
//            else -> "🔴 CRITICAL: Your resume requires significant changes before ATS submission. Start with high-priority recommendations."
//        }
//
//        document.add(Paragraph(assessment, Font(Font.FontFamily.HELVETICA, 11f, Font.NORMAL)))
//
//        document.add(Paragraph("\n"))
//        addSectionTitle(document, "📋 Quick Stats")
//
//        val statsTable = PdfPTable(4)
//        statsTable.widthPercentage = 100f
//        statsTable.setWidths(floatArrayOf(1f, 1f, 1f, 1f))
//        statsTable.spacingAfter = 15f
//
//        addStatCell(statsTable, "Score", "${analysis.overallScore}/100", primaryColor)
//        addStatCell(statsTable, "Word Count", wordCount.toString(), if (wordCount in 400..1200) successColor else warningColor)
//        addStatCell(statsTable, "Keywords", "${analysis.keywords.foundKeywords.size}", primaryColor)
//        addStatCell(statsTable, "Density", String.format("%.1f%%", analysis.keywords.keywordDensity), primaryColor)
//
//        document.add(statsTable)
//    }
//
//    private fun addScoreBreakdownPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "🎯 DETAILED SCORE BREAKDOWN")
//
//        val breakdown = analysis.breakdown
//
//        val categories = listOf(
//            "Format Compatibility" to breakdown.formatScore,
//            "Keyword Optimization" to breakdown.keywordScore,
//            "Content Quality" to breakdown.contentScore,
//            "Structure Organization" to breakdown.structureScore,
//            "Contact Information" to breakdown.contactScore
//        )
//
//        for ((name, score) in categories) {
//            addScoreBar(document, name, score)
//        }
//
//        document.add(Paragraph("\n"))
//        addSectionTitle(document, "💡 What These Scores Mean")
//
//        val meanings = mapOf(
//            "Format" to "How well your resume is structured for ATS parsing (fonts, layout, structure)",
//            "Keywords" to "Number and relevance of technical skills and industry terms",
//            "Content" to "Quality of achievement descriptions and use of action verbs",
//            "Structure" to "Presence and organization of essential resume sections",
//            "Contact" to "Availability of contact info and professional online profiles"
//        )
//
//        for ((key, value) in meanings) {
//            document.add(Paragraph("• $key: $value\n", Font(Font.FontFamily.HELVETICA, 10f)))
//        }
//    }
//
//    private fun addResumeStructurePage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "✅ RESUME STRUCTURE ANALYSIS")
//
//        val sections = analysis.sections
//
//        val sectionsList = listOf(
//            Triple("Contact Information", sections.hasContactInfo, "Email, phone, LinkedIn, GitHub"),
//            Triple("Professional Summary", sections.hasObjective, "2-3 line overview of your career"),
//            Triple("Work Experience", sections.hasExperience, "Job titles, companies, achievements"),
//            Triple("Education", sections.hasEducation, "Degree, university, graduation date"),
//            Triple("Skills", sections.hasSkills, "Technical and soft skills (25-35 recommended)"),
//            Triple("Certifications", sections.hasCertifications, "Relevant certifications and licenses")
//        )
//
//        val table = PdfPTable(4)
//        table.widthPercentage = 100f
//        table.setWidths(floatArrayOf(1.5f, 0.8f, 2f, 1f))
//        table.spacingAfter = 20f
//
//        arrayOf("Section", "Status", "Details", "Priority").forEach {
//            addTableHeader(table, it)
//        }
//
//        for ((name, present, details) in sectionsList) {
//            val statusText = if (present) "✅ Present" else "❌ Missing"
//            val statusColor = if (present) successColor else dangerColor
//            val priority = when {
//                !present && (name.contains("Contact") || name.contains("Experience") || name.contains("Education") || name.contains("Skills")) -> "🔴 HIGH"
//                !present -> "🟡 MEDIUM"
//                else -> "✅ OK"
//            }
//
//            val nameCell = PdfPCell(Paragraph(name, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD)))
//            nameCell.setPadding(8f)
//            table.addCell(nameCell)
//
//            val statusCell = PdfPCell(Paragraph(statusText, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, statusColor)))
//            statusCell.setPadding(8f)
//            statusCell.horizontalAlignment = Element.ALIGN_CENTER
//            table.addCell(statusCell)
//
//            val detailsCell = PdfPCell(Paragraph(details, Font(Font.FontFamily.HELVETICA, 9f)))
//            detailsCell.setPadding(8f)
//            table.addCell(detailsCell)
//
//            val priorityCell = PdfPCell(Paragraph(priority, Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD)))
//            priorityCell.setPadding(8f)
//            priorityCell.horizontalAlignment = Element.ALIGN_CENTER
//            table.addCell(priorityCell)
//        }
//
//        document.add(table)
//
//        document.add(Paragraph("\n"))
//        addSectionTitle(document, "🎯 Missing Sections Impact")
//
//        val missingSections = sectionsList.filter { !it.second }
//        if (missingSections.isEmpty()) {
//            document.add(Paragraph(
//                "✅ All essential sections are present! This is excellent for ATS compatibility.",
//                Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, successColor)
//            ))
//        } else {
//            for ((name, _, _) in missingSections) {
//                document.add(Paragraph("❌ Missing $name - Add this section to improve your score\n", Font(Font.FontFamily.HELVETICA, 10f)))
//            }
//        }
//    }
//
//    private fun addContentQualityPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "📝 CONTENT QUALITY ANALYSIS")
//
//        addSectionTitle(document, "Action Verbs & Keywords Found")
//
//        val keywordsPreview = analysis.keywords.foundKeywords.take(12).joinToString(", ")
//        document.add(Paragraph(
//            keywordsPreview,
//            Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, successColor)
//        ))
//
//        document.add(Paragraph("\n"))
//        addSectionTitle(document, "📊 Content Metrics")
//
//        val wordCount = analysis.extractedText.split(Regex("\\s+")).size
//        val metricsData = listOf(
//            Triple("Action Verbs Count", analysis.keywords.foundKeywords.size, 12),
//            Triple("Tech Keywords", analysis.keywords.foundKeywords.size, 15),
//            Triple("Word Count", wordCount, 800),
//            Triple("Keyword Density", analysis.keywords.keywordDensity.toInt(), 8)
//        )
//
//        for ((metric, current, target) in metricsData) {
//            val percentage = (current.toFloat() / target * 100).coerceAtMost(100f)
//            val status = when {
//                percentage >= 100 -> "✅ EXCELLENT"
//                percentage >= 80 -> "🟢 GOOD"
//                percentage >= 60 -> "🟡 FAIR"
//                else -> "🔴 LOW"
//            }
//
//            val row = "${metric.padEnd(25)} │ Current: $current / Target: $target │ $status"
//            document.add(Paragraph(row, Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL)))
//
//            val progressBar = createProgressBar(percentage.toInt())
//            document.add(Paragraph(progressBar + "\n", Font(Font.FontFamily.HELVETICA, 8f)))
//        }
//
//        document.add(Paragraph("\n"))
//        addSectionTitle(document, "💡 Content Recommendations")
//
//        if (analysis.keywords.foundKeywords.size < 12) {
//            document.add(Paragraph("• Add more action verbs (Led, Architected, Optimized, Transformed)\n", Font(Font.FontFamily.HELVETICA, 10f)))
//        }
//        if (analysis.keywords.keywordDensity < 5) {
//            document.add(Paragraph("• Increase technical keyword density to 5-10%\n", Font(Font.FontFamily.HELVETICA, 10f)))
//        }
//        if (wordCount < 400) {
//            document.add(Paragraph("• Expand your resume - currently too brief for optimal ATS performance\n", Font(Font.FontFamily.HELVETICA, 10f)))
//        }
//        if (wordCount > 1200) {
//            document.add(Paragraph("• Trim unnecessary content - maintain focus on impactful achievements\n", Font(Font.FontFamily.HELVETICA, 10f)))
//        }
//    }
//
////    private fun addContactPresencePage(document: Document, analysis: ATSAnalysisResult) {
////        addPageHeader(document, "📱 CONTACT & ONLINE PRESENCE")
////
////        addSectionTitle(document, "✅ Contact Information Status")
////
////        val contactItems = listOf(
////            "Email Address" to "✅ CRITICAL",
////            "Phone Number" to "✅ CRITICAL",
////            "LinkedIn Profile" to "🟡 HIGHLY IMPORTANT",
////            "GitHub Profile" to "🟡 IMPORTANT FOR TECH",
////            "Personal Portfolio" to "🟢 BENEFICIAL"
////        )
////
////        val contactTable = PdfPTable(2)
////        contactTable.widthPercentage = 100f
////        contactTable.setWidths(floatArrayOf(1.5f, 1.5f))
////        contactTable.spacingAfter = 20f
////
////        for ((item, importance) in contactItems) {
////            val cell1 = PdfPCell(Paragraph(item, Font(Font.FontFamily.HELVETICA, 11f)))
////            cell1.setPadding(10f)
////            contactTable.addCell(cell1)
////
////            val cell2 = PdfPCell(Paragraph(importance, Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD)))
////            cell2.setPadding(10f)
////            contactTable.addCell(cell2)
////        }
////
////        document.add(contactTable)
////
////        document.add(Paragraph("\n"))
////        addSectionTitle(document, "🌐 Why Online Presence Matters")
////
////        val reasons = listOf(
////            "LinkedIn: Recruiters verify experience and endorsements here (adds credibility)",
////            "GitHub: Showcases real projects and coding ability (for tech roles)",
////            "Portfolio: Demonstrates your best work and tangible outcomes",
////            "Professional Domains: Better than generic emails for first impressions"
////        )
////
////        for (reason in reasons) {
////            document.add(Paragraph("• $reason\n", Font(Font.FontFamily.HELVETICA, 10f)))
////        }
////    }
//
//    private fun addContactPresencePage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "📱 CONTACT & ONLINE PRESENCE")
//
//        addSectionTitle(document, "✅ Contact Information Status")
//
//        // Detect LinkedIn, GitHub, Portfolio from analysis
//        val lower = analysis.extractedText.lowercase()
//        val hasLinkedIn = listOf("linkedin.com", "linkedin", "in/").any { lower.contains(it) }
//        val hasGitHub = listOf("github.com", "github", "gh").any { lower.contains(it) }
//        val hasPortfolio = listOf("portfolio", "personal website", "portfolio site").any { lower.contains(it) }
//
//        val contactItems = listOf(
//            Triple("Email Address", if (analysis.sections.hasContactInfo) "✅ PRESENT" else "❌ MISSING", "✅ CRITICAL"),
//            Triple("Phone Number", "✅ PRESENT", "✅ CRITICAL"), // Assuming present from analysis
//            Triple("LinkedIn Profile", if (hasLinkedIn) "✅ PRESENT" else "❌ MISSING", "🟡 HIGHLY IMPORTANT"),
//            Triple("GitHub Profile", if (hasGitHub) "✅ PRESENT" else "❌ MISSING", "🟡 IMPORTANT FOR TECH"),
//            Triple("Personal Portfolio", if (hasPortfolio) "✅ PRESENT" else "❌ MISSING", "🟢 BENEFICIAL")
//        )
//
//        val contactTable = PdfPTable(3)
//        contactTable.widthPercentage = 100f
//        contactTable.setWidths(floatArrayOf(1.5f, 1.2f, 1.3f))
//        contactTable.spacingAfter = 20f
//
//        arrayOf("Item", "Status", "Importance").forEach {
//            addTableHeader(contactTable, it)
//        }
//
//        for ((item, status, importance) in contactItems) {
//            // Item Cell
//            val itemCell = PdfPCell(Paragraph(item, Font(Font.FontFamily.HELVETICA, 10f)))
//            itemCell.setPadding(10f)
//            contactTable.addCell(itemCell)
//
//            // Status Cell
//            val statusColor = if (status.contains("PRESENT")) BaseColor(76, 175, 80) else BaseColor(244, 67, 54)
//            val statusCell = PdfPCell(Paragraph(status, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, statusColor)))
//            statusCell.setPadding(10f)
//            statusCell.horizontalAlignment = Element.ALIGN_CENTER
//            contactTable.addCell(statusCell)
//
//            // Importance Cell
//            val importanceCell = PdfPCell(Paragraph(importance, Font(Font.FontFamily.HELVETICA, 9f)))
//            importanceCell.setPadding(10f)
//            importanceCell.horizontalAlignment = Element.ALIGN_CENTER
//            contactTable.addCell(importanceCell)
//        }
//
//        document.add(contactTable)
//
//        document.add(Paragraph("\n"))
//        addSectionTitle(document, "🌐 Why Online Presence Matters")
//
//        val reasons = listOf(
//            "LinkedIn: Recruiters verify experience and endorsements here (adds credibility)",
//            "GitHub: Showcases real projects and coding ability (for tech roles)",
//            "Portfolio: Demonstrates your best work and tangible outcomes",
//            "Professional Domains: Better than generic emails for first impressions"
//        )
//
//        for (reason in reasons) {
//            document.add(Paragraph("• $reason\n", Font(Font.FontFamily.HELVETICA, 10f)))
//        }
//
//        document.add(Paragraph("\n"))
//        addSectionTitle(document, "📋 What's Missing")
//
//        val missing = mutableListOf<String>()
//        if (!hasLinkedIn) missing.add("LinkedIn Profile")
//        if (!hasGitHub) missing.add("GitHub Profile")
//        if (!hasPortfolio) missing.add("Personal Portfolio")
//
//        if (missing.isEmpty()) {
//            document.add(Paragraph(
//                "✅ Excellent! You have all important online profiles.",
//                Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor(76, 175, 80))
//            ))
//        } else {
//            document.add(Paragraph(
//                "Add these to strengthen your application: ${missing.joinToString(", ")}",
//                Font(Font.FontFamily.HELVETICA, 10f)
//            ))
//        }
//    }
//
//    private fun addStrengthsPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "⭐ YOUR RESUME STRENGTHS")
//
//        if (analysis.strengths.isEmpty()) {
//            document.add(Paragraph("No major strengths identified - focus on improvements", Font(Font.FontFamily.HELVETICA, 11f)))
//            return
//        }
//
//        for ((index, strength) in analysis.strengths.take(8).withIndex()) {
//            val titleP = Paragraph(
//                "${index + 1}. ${strength}",
//                Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, successColor)
//            )
//            titleP.spacingAfter = 8f
//            document.add(titleP)
//        }
//
//        document.add(Paragraph("\n"))
//        addSectionTitle(document, "✅ Keep Doing")
//
//        document.add(Paragraph(
//            "Maintain and expand these strong areas. They are key differentiators in ATS systems.",
//            Font(Font.FontFamily.HELVETICA, 10f)
//        ))
//    }
//
//    private fun addWeaknessesPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "⚠️ AREAS FOR IMPROVEMENT")
//
//        if (analysis.weaknesses.isEmpty()) {
//            document.add(Paragraph("Your resume is well-optimized!", Font(Font.FontFamily.HELVETICA, 11f, Font.NORMAL, successColor)))
//            return
//        }
//
//        for ((index, weakness) in analysis.weaknesses.take(8).withIndex()) {
//            val titleP = Paragraph(
//                "${index + 1}. ${weakness}",
//                Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, dangerColor)
//            )
//            titleP.spacingAfter = 8f
//            document.add(titleP)
//        }
//
//        document.add(Paragraph("\n"))
//        addSectionTitle(document, "🎯 Priority Fixes")
//
//        val highPriority = analysis.suggestions.filter { it.priority.name == "HIGH" }.take(3)
//        for (suggestion in highPriority) {
//            document.add(Paragraph(
//                "🔴 ${suggestion.title}: ${suggestion.description}\n",
//                Font(Font.FontFamily.HELVETICA, 10f)
//            ))
//        }
//    }
//
//    private fun addRecommendationsPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "💡 DETAILED RECOMMENDATIONS")
//
//        val highPriority = analysis.suggestions.filter { it.priority.name == "HIGH" }
//        val mediumPriority = analysis.suggestions.filter { it.priority.name == "MEDIUM" }
//        val lowPriority = analysis.suggestions.filter { it.priority.name == "LOW" }
//
//        if (highPriority.isNotEmpty()) {
//            addRecommendationSection(document, "🔴 HIGH PRIORITY (Implement First)", highPriority.take(5), dangerColor)
//            document.add(Paragraph("\n"))
//        }
//
//        if (mediumPriority.isNotEmpty()) {
//            addRecommendationSection(document, "🟡 MEDIUM PRIORITY (Highly Recommended)", mediumPriority.take(4), warningColor)
//            document.add(Paragraph("\n"))
//        }
//
//        if (lowPriority.isNotEmpty()) {
//            addRecommendationSection(document, "🟢 LOW PRIORITY (Nice to Have)", lowPriority.take(3), successColor)
//        }
//    }
//
//    private fun addActionPlanPage(document: Document, analysis: ATSAnalysisResult) {
//        addPageHeader(document, "🚀 YOUR ACTION PLAN")
//
//        addSectionTitle(document, "This Week (Immediate Actions)")
//
//        val immediateActions = listOf(
//            "Review all 🔴 HIGH priority recommendations",
//            "Fix critical contact information and missing sections",
//            "Add quantifiable metrics to your achievements",
//            "Increase action verbs in your job descriptions"
//        )
//
//        for ((index, action) in immediateActions.withIndex()) {
//            document.add(Paragraph("${"□"}  ${index + 1}. $action\n", Font(Font.FontFamily.HELVETICA, 10f)))
//        }
//
//        document.add(Paragraph("\n"))
//        addSectionTitle(document, "Next 2 Weeks (Medium-term)")
//
//        val mediumActions = listOf(
//            "Expand skills section with 12+ technical keywords",
//            "Create/update LinkedIn and GitHub profiles",
//            "Add quantifiable business metrics to accomplishments",
//            "Implement 🟡 MEDIUM priority recommendations"
//        )
//
//        for ((index, action) in mediumActions.withIndex()) {
//            document.add(Paragraph("${"□"}  ${index + 1}. $action\n", Font(Font.FontFamily.HELVETICA, 10f)))
//        }
//
//        document.add(Paragraph("\n"))
//        addSectionTitle(document, "Success Criteria")
//
//        val successCriteria = when {
//            analysis.overallScore >= 80 -> listOf(
//                "✅ Maintain current excellent score",
//                "✅ Re-verify after 6 months of job changes",
//                "✅ Ready for top-tier company applications"
//            )
//            else -> listOf(
//                "✅ Target: Reach 75+ score",
//                "✅ Complete all HIGH priority recommendations",
//                "✅ Add minimum 12 technical keywords",
//                "✅ Include at least 2-3 quantifiable metrics"
//            )
//        }
//
//        for (criteria in successCriteria) {
//            document.add(Paragraph(criteria + "\n", Font(Font.FontFamily.HELVETICA, 10f)))
//        }
//
//        document.add(Paragraph("\n\n"))
//
//        val footer = Paragraph(
//            "Generated: ${getCurrentDate()}\n" +
//                    "Analyzer: Advanced ATS Calculator\n" +
//                    "Your resume was analyzed against current ATS systems best practices",
//            Font(Font.FontFamily.HELVETICA, 8f, Font.ITALIC, BaseColor.GRAY)
//        )
//        footer.alignment = Element.ALIGN_CENTER
//        document.add(footer)
//    }
//
//    // ==================== HELPER FUNCTIONS ====================
//
//    private fun addPageHeader(document: Document, title: String) {
//        val header = Paragraph(title, Font(Font.FontFamily.HELVETICA, 24f, Font.BOLD, primaryColor))
//        header.spacingAfter = 15f
//        document.add(header)
//
//        val line = Paragraph("_".repeat(95))
//        line.spacingAfter = 20f
//        document.add(line)
//    }
//
//    private fun addSectionTitle(document: Document, title: String) {
//        val header = Paragraph(title, Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD, primaryColor))
//        header.spacingBefore = 12f
//        header.spacingAfter = 10f
//        document.add(header)
//    }
//
//    private fun addInfoBox(document: Document, status: String, score: Int, color: BaseColor) {
//        val cell = PdfPCell().apply {
//            borderColor = color
//            borderWidth = 3f
//            backgroundColor = lightGray
//            setPadding(15f)
//            horizontalAlignment = Element.ALIGN_CENTER
//
//            val text = "$status - Score: $score/100"
//            addElement(Paragraph(text, Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD, color)))
//        }
//
//        val table = PdfPTable(1)
//        table.widthPercentage = 100f
//        table.spacingAfter = 20f
//        table.addCell(cell)
//        document.add(table)
//    }
//
//    private fun addStatCell(table: PdfPTable, label: String, value: String, color: BaseColor) {
//        val labelP = Paragraph(label, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD))
//        val valueP = Paragraph(value, Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, color))
//
//        val cell = PdfPCell()
//        cell.backgroundColor = lightGray
//        cell.setPadding(10f)
//        cell.horizontalAlignment = Element.ALIGN_CENTER
//        cell.addElement(labelP)
//        cell.addElement(Paragraph(" "))
//        cell.addElement(valueP)
//
//        table.addCell(cell)
//    }
//
//    private fun addScoreBar(document: Document, label: String, score: Int) {
//        val statusColor = when {
//            score >= 85 -> successColor
//            score >= 70 -> warningColor
//            else -> dangerColor
//        }
//
//        val labelP = Paragraph(
//            "$label: $score/100",
//            Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD)
//        )
//        labelP.spacingAfter = 5f
//        document.add(labelP)
//
//        val barLength = (score / 5).coerceIn(1, 20)
//        val bar = "█".repeat(barLength) + "░".repeat(20 - barLength)
//        val barP = Paragraph(bar, Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL, statusColor))
//        barP.spacingAfter = 12f
//        document.add(barP)
//    }
//
//    private fun addTableHeader(table: PdfPTable, text: String) {
//        val cell = PdfPCell(Paragraph(text, Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor.WHITE)))
//        cell.backgroundColor = primaryColor
//        cell.setPadding(10f)
//        cell.horizontalAlignment = Element.ALIGN_CENTER
//        table.addCell(cell)
//    }
//
//    private fun addRecommendationSection(document: Document, title: String, suggestions: List<com.example.feature_student.model.Suggestion>, color: BaseColor) {
//        val header = Paragraph(title, Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, color))
//        header.spacingAfter = 10f
//        document.add(header)
//
//        for (suggestion in suggestions) {
//            val titleP = Paragraph(suggestion.title, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, color))
//            titleP.spacingAfter = 3f
//            document.add(titleP)
//
//            val descP = Paragraph("→ ${suggestion.description}\n", Font(Font.FontFamily.HELVETICA, 9f))
//            descP.spacingAfter = 8f
//            document.add(descP)
//        }
//    }
//
//    private fun getStatusInfo(score: Int): Triple<String, String, BaseColor> {
//        return when {
//            score >= 85 -> Triple("🟢", "EXCELLENT - ATS READY", successColor)
//            score >= 75 -> Triple("🟢", "GOOD - COMPETITIVE", successColor)
//            score >= 65 -> Triple("🟡", "FAIR - NEEDS OPTIMIZATION", warningColor)
//            score >= 55 -> Triple("🟡", "POOR - SIGNIFICANT WORK NEEDED", warningColor)
//            else -> Triple("🔴", "CRITICAL - MAJOR CHANGES REQUIRED", dangerColor)
//        }
//    }
//
//    private fun createProgressBar(percentage: Int): String {
//        val filled = (percentage / 5).coerceIn(0, 20)
//        val empty = 20 - filled
//        return "▮".repeat(filled) + "▯".repeat(empty) + " $percentage%"
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
//
//




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
import android.os.Handler
import android.os.Looper

class ReportGenerator(private val context: Context) {

    private val primaryColor = BaseColor(108, 99, 255)
    private val successColor = BaseColor(76, 175, 80)
    private val warningColor = BaseColor(255, 152, 0)
    private val dangerColor = BaseColor(244, 67, 54)
    private val lightGray = BaseColor(240, 240, 240)

    fun generatePDFReport(resume: Resume, analysis: ATSAnalysisResult): File? {
        return try {
            Log.d("ReportGen", "📄 Starting PDF Report Generation")

            // Step 1: Get Downloads Directory - Try multiple approaches
            val downloadsDir = getDownloadsDirectory()

            if (downloadsDir == null || !downloadsDir.exists()) {
                Log.e("ReportGen", "❌ Failed to get or create downloads directory")
                return null
            }

            Log.d("ReportGen", "📁 Downloads Directory: ${downloadsDir.absolutePath}")
            Log.d("ReportGen", "📁 Directory exists: ${downloadsDir.exists()}")
            Log.d("ReportGen", "📁 Can write: ${downloadsDir.canWrite()}")

            // Step 2: Create unique filename
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "ATS_Report_$timeStamp.pdf"
            val file = File(downloadsDir, fileName)

            Log.d("ReportGen", "📝 Creating file: ${file.absolutePath}")

            // Step 3: Create the PDF
            val document = Document(PageSize.A4, 40f, 40f, 50f, 50f)
            val fileOutputStream = FileOutputStream(file)
            PdfWriter.getInstance(document, fileOutputStream)
            document.open()

            // Add all pages
            addCoverPage(document, analysis)
            document.newPage()
            addExecutiveSummary(document, analysis)
            document.newPage()
            addScoreBreakdownPage(document, analysis)
            document.newPage()
            addResumeStructurePage(document, analysis)
            document.newPage()
            addContentQualityPage(document, analysis)
            document.newPage()
            addContactPresencePage(document, analysis)
            document.newPage()
            addStrengthsPage(document, analysis)
            document.newPage()
            addWeaknessesPage(document, analysis)
            document.newPage()
            addRecommendationsPage(document, analysis)
            document.newPage()
            addActionPlanPage(document, analysis)

            document.close()
            fileOutputStream.close()

            // Step 4: Verify file was created
            if (file.exists()) {
                Log.d("ReportGen", "✅ PDF created successfully")
                Log.d("ReportGen", "📥 File: ${file.name}")
                Log.d("ReportGen", "📊 Size: ${formatFileSize(file.length())}")
                Log.d("ReportGen", "📂 Full Path: ${file.absolutePath}")

                // 🔥 AUTO-OPEN REPORT AFTER 500ms DELAY
                Handler(Looper.getMainLooper()).postDelayed({
                    openPDFReport(file)
                }, 500)

                return file
            } else {
                Log.e("ReportGen", "❌ File was not created")
                return null
            }

        } catch (e: Exception) {
            Log.e("ReportGen", "❌ PDF generation failed: ${e.message}")
            Log.e("ReportGen", "❌ Stack trace: ${e.stackTraceToString()}")
            e.printStackTrace()
            return null
        }
    }

    private fun getDownloadsDirectory(): File? {
        return try {
            val downloadsDir = when {
                // Android 10+ (API 29+)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    dir
                }
                // Android 6-9
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                    @Suppress("DEPRECATION")
                    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    dir
                }
                // Below Android 6
                else -> {
                    @Suppress("DEPRECATION")
                    File(Environment.getExternalStorageDirectory(), "Download")
                }
            }

            // Ensure directory exists
            if (!downloadsDir.exists()) {
                val created = downloadsDir.mkdirs()
                Log.d("ReportGen", "📁 Directory creation result: $created")
            }

            downloadsDir
        } catch (e: Exception) {
            Log.e("ReportGen", "❌ Error getting downloads directory: ${e.message}")
            null
        }
    }

        fun openPDFReport(file: File) {
        try {
            Log.d("ReportGen", "📖 Opening PDF: ${file.absolutePath}")

            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                } catch (e: Exception) {
                    Log.e("ReportGen", "❌ FileProvider error: ${e.message}")
                    @Suppress("DEPRECATION")
                    Uri.fromFile(file)
                }
            } else {
                @Suppress("DEPRECATION")
                Uri.fromFile(file)
            }

            Log.d("ReportGen", "📱 URI: $uri")

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            // Verify intent can be resolved
            val resolveInfo = context.packageManager.resolveActivity(intent, 0)
            if (resolveInfo != null) {
                context.startActivity(intent)
                Log.d("ReportGen", "✅ PDF opened successfully")
            } else {
                Log.e("ReportGen", "❌ No PDF viewer app found")
            }
        } catch (e: Exception) {
            Log.e("ReportGen", "❌ Could not open PDF: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun addCoverPage(document: Document, analysis: ATSAnalysisResult) {
        document.add(Paragraph("\n\n\n"))

        val titleFont = Font(Font.FontFamily.HELVETICA, 48f, Font.BOLD, primaryColor)
        val title = Paragraph("ATS ANALYSIS REPORT", titleFont)
        title.alignment = Element.ALIGN_CENTER
        title.spacingAfter = 10f
        document.add(title)

        val subtitleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.NORMAL, BaseColor.GRAY)
        val subtitle = Paragraph("Professional Resume Evaluation & Optimization", subtitleFont)
        subtitle.alignment = Element.ALIGN_CENTER
        subtitle.spacingAfter = 80f
        document.add(subtitle)

        val scoreFont = Font(Font.FontFamily.HELVETICA, 96f, Font.BOLD, primaryColor)
        val scoreP = Paragraph(analysis.overallScore.toString(), scoreFont)
        scoreP.alignment = Element.ALIGN_CENTER
        scoreP.spacingAfter = 5f
        document.add(scoreP)

        val outOfFont = Font(Font.FontFamily.HELVETICA, 24f, Font.NORMAL, BaseColor.GRAY)
        val outOfP = Paragraph("out of 100", outOfFont)
        outOfP.alignment = Element.ALIGN_CENTER
        outOfP.spacingAfter = 40f
        document.add(outOfP)

        val (statusEmoji, statusText, statusColor) = getStatusInfo(analysis.overallScore)
        val statusFont = Font(Font.FontFamily.HELVETICA, 28f, Font.BOLD, statusColor)
        val statusP = Paragraph("$statusEmoji $statusText", statusFont)
        statusP.alignment = Element.ALIGN_CENTER
        statusP.spacingAfter = 100f
        document.add(statusP)

        val footerFont = Font(Font.FontFamily.HELVETICA, 10f, Font.ITALIC, BaseColor.GRAY)
        val footerP = Paragraph(
            "Generated: ${getCurrentDate()}\nAnalysis by: Advanced ATS Calculator",
            footerFont
        )
        footerP.alignment = Element.ALIGN_CENTER
        document.add(footerP)
    }

    private fun addExecutiveSummary(document: Document, analysis: ATSAnalysisResult) {
        addPageHeader(document, "📊 EXECUTIVE SUMMARY")

        val wordCount = analysis.extractedText.split(Regex("\\s+")).size
        val (_, statusText, statusColor) = getStatusInfo(analysis.overallScore)

        addInfoBox(document, statusText, analysis.overallScore, statusColor)

        document.add(Paragraph("\n"))
        addSectionTitle(document, "📈 Overall Assessment")

        val assessment = when {
            analysis.overallScore >= 85 -> "🟢 EXCELLENT: Your resume is ATS-optimized and ready for submission. You have all critical components with strong keyword coverage and metrics."
            analysis.overallScore >= 75 -> "🟢 GOOD: Your resume is competitive. Minor enhancements in keywords and metrics will push you to an excellent score."
            analysis.overallScore >= 65 -> "🟡 FAIR: Your resume needs optimization. Focus on adding metrics, keywords, and strengthening action verbs."
            analysis.overallScore >= 55 -> "🟡 NEEDS WORK: Several sections need improvement. Prioritize adding missing sections and quantifiable metrics."
            else -> "🔴 CRITICAL: Your resume requires significant changes before ATS submission. Start with high-priority recommendations."
        }

        document.add(Paragraph(assessment, Font(Font.FontFamily.HELVETICA, 11f, Font.NORMAL)))

        document.add(Paragraph("\n"))
        addSectionTitle(document, "📋 Quick Stats")

        val statsTable = PdfPTable(4)
        statsTable.widthPercentage = 100f
        statsTable.setWidths(floatArrayOf(1f, 1f, 1f, 1f))
        statsTable.spacingAfter = 15f

        addStatCell(statsTable, "Score", "${analysis.overallScore}/100", primaryColor)
        addStatCell(statsTable, "Word Count", wordCount.toString(), if (wordCount in 400..1200) successColor else warningColor)
        addStatCell(statsTable, "Keywords", "${analysis.keywords.foundKeywords.size}", primaryColor)
        addStatCell(statsTable, "Density", String.format("%.1f%%", analysis.keywords.keywordDensity), primaryColor)

        document.add(statsTable)
    }

    private fun addScoreBreakdownPage(document: Document, analysis: ATSAnalysisResult) {
        addPageHeader(document, "🎯 DETAILED SCORE BREAKDOWN")

        val breakdown = analysis.breakdown

        val categories = listOf(
            "Format Compatibility" to breakdown.formatScore,
            "Keyword Optimization" to breakdown.keywordScore,
            "Content Quality" to breakdown.contentScore,
            "Structure Organization" to breakdown.structureScore,
            "Contact Information" to breakdown.contactScore
        )

        for ((name, score) in categories) {
            addScoreBar(document, name, score)
        }

        document.add(Paragraph("\n"))
        addSectionTitle(document, "💡 What These Scores Mean")

        val meanings = mapOf(
            "Format" to "How well your resume is structured for ATS parsing (fonts, layout, structure)",
            "Keywords" to "Number and relevance of technical skills and industry terms",
            "Content" to "Quality of achievement descriptions and use of action verbs",
            "Structure" to "Presence and organization of essential resume sections",
            "Contact" to "Availability of contact info and professional online profiles"
        )

        for ((key, value) in meanings) {
            document.add(Paragraph("• $key: $value\n", Font(Font.FontFamily.HELVETICA, 10f)))
        }
    }

    private fun addResumeStructurePage(document: Document, analysis: ATSAnalysisResult) {
        addPageHeader(document, "✅ RESUME STRUCTURE ANALYSIS")

        val sections = analysis.sections

        val sectionsList = listOf(
            Triple("Contact Information", sections.hasContactInfo, "Email, phone, LinkedIn, GitHub"),
            Triple("Professional Summary", sections.hasObjective, "2-3 line overview of your career"),
            Triple("Work Experience", sections.hasExperience, "Job titles, companies, achievements"),
            Triple("Education", sections.hasEducation, "Degree, university, graduation date"),
            Triple("Skills", sections.hasSkills, "Technical and soft skills (25-35 recommended)"),
            Triple("Certifications", sections.hasCertifications, "Relevant certifications and licenses")
        )

        val table = PdfPTable(4)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(1.5f, 0.8f, 2f, 1f))
        table.spacingAfter = 20f

        arrayOf("Section", "Status", "Details", "Priority").forEach {
            addTableHeader(table, it)
        }

        for ((name, present, details) in sectionsList) {
            val statusText = if (present) "✅ Present" else "❌ Missing"
            val statusColor = if (present) successColor else dangerColor
            val priority = when {
                !present && (name.contains("Contact") || name.contains("Experience") || name.contains("Education") || name.contains("Skills")) -> "🔴 HIGH"
                !present -> "🟡 MEDIUM"
                else -> "✅ OK"
            }

            val nameCell = PdfPCell(Paragraph(name, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD)))
            nameCell.setPadding(8f)
            table.addCell(nameCell)

            val statusCell = PdfPCell(Paragraph(statusText, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, statusColor)))
            statusCell.setPadding(8f)
            statusCell.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(statusCell)

            val detailsCell = PdfPCell(Paragraph(details, Font(Font.FontFamily.HELVETICA, 9f)))
            detailsCell.setPadding(8f)
            table.addCell(detailsCell)

            val priorityCell = PdfPCell(Paragraph(priority, Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD)))
            priorityCell.setPadding(8f)
            priorityCell.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(priorityCell)
        }

        document.add(table)

        document.add(Paragraph("\n"))
        addSectionTitle(document, "🎯 Missing Sections Impact")

        val missingSections = sectionsList.filter { !it.second }
        if (missingSections.isEmpty()) {
            document.add(Paragraph(
                "✅ All essential sections are present! This is excellent for ATS compatibility.",
                Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, successColor)
            ))
        } else {
            for ((name, _, _) in missingSections) {
                document.add(Paragraph("❌ Missing $name - Add this section to improve your score\n", Font(Font.FontFamily.HELVETICA, 10f)))
            }
        }
    }

    private fun addContentQualityPage(document: Document, analysis: ATSAnalysisResult) {
        addPageHeader(document, "📝 CONTENT QUALITY ANALYSIS")

        addSectionTitle(document, "✅ Action Verbs & Keywords Found")

        val keywordsPreview = analysis.keywords.foundKeywords.take(12).joinToString(", ")
        val keywordsParagraph = Paragraph(keywordsPreview, Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, successColor))
        keywordsParagraph.spacingAfter = 15f
        document.add(keywordsParagraph)

        addSectionTitle(document, "📊 Content Metrics Table")

        // ============ METRICS TABLE (Tabular Form) ============
        val metricsTable = PdfPTable(5)
        metricsTable.widthPercentage = 100f
        metricsTable.setWidths(floatArrayOf(2f, 1.2f, 1.2f, 1.2f, 1.2f))
        metricsTable.spacingAfter = 20f

        // Table Header
        arrayOf("Metric", "Current", "Target", "Progress", "Status").forEach {
            addTableHeader(metricsTable, it)
        }

        val wordCount = analysis.extractedText.split(Regex("\\s+")).size
        val metricsData = listOf(
            Triple("Action Verbs Count", analysis.keywords.foundKeywords.size, 12),
            Triple("Tech Keywords", analysis.keywords.foundKeywords.size, 15),
            Triple("Word Count", wordCount, 800),
            Triple("Keyword Density", analysis.keywords.keywordDensity.toInt(), 8)
        )

        for ((metric, current, target) in metricsData) {
            val percentage = (current.toFloat() / target * 100).coerceAtMost(100f)

            val (statusEmoji, statusText, statusColor) = when {
                percentage >= 100 -> Triple("✅", "EXCELLENT", successColor)
                percentage >= 80 -> Triple("🟢", "GOOD", BaseColor(76, 175, 80))
                percentage >= 60 -> Triple("🟡", "FAIR", warningColor)
                else -> Triple("🔴", "LOW", dangerColor)
            }

            // Metric Name Cell
            val metricCell = PdfPCell(Paragraph(metric, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD)))
            metricCell.setPadding(10f)
            metricCell.backgroundColor = lightGray
            metricsTable.addCell(metricCell)

            // Current Value Cell
            val currentCell = PdfPCell(Paragraph(current.toString(), Font(Font.FontFamily.HELVETICA, 10f)))
            currentCell.setPadding(10f)
            currentCell.horizontalAlignment = Element.ALIGN_CENTER
            metricsTable.addCell(currentCell)

            // Target Value Cell
            val targetCell = PdfPCell(Paragraph(target.toString(), Font(Font.FontFamily.HELVETICA, 10f)))
            targetCell.setPadding(10f)
            targetCell.horizontalAlignment = Element.ALIGN_CENTER
            metricsTable.addCell(targetCell)

            // Progress Bar Cell
            val progressPercentInt = percentage.toInt()
            val progressBar = createProgressBar(progressPercentInt)
            val progressCell = PdfPCell(Paragraph(progressBar, Font(Font.FontFamily.HELVETICA, 8f)))
            progressCell.setPadding(8f)
            progressCell.horizontalAlignment = Element.ALIGN_CENTER
            metricsTable.addCell(progressCell)

            // Status Cell
            val statusCell = PdfPCell(Paragraph("$statusEmoji $statusText", Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, statusColor)))
            statusCell.setPadding(10f)
            statusCell.horizontalAlignment = Element.ALIGN_CENTER
            metricsTable.addCell(statusCell)
        }

        document.add(metricsTable)

        document.add(Paragraph("\n"))
        addSectionTitle(document, "📝 What Each Metric Means")

        val metricsMeaning = listOf(
            "Action Verbs: Impactful words like Led, Architected, Optimized (Target: 12+)",
            "Tech Keywords: Technology skills and tools mentioned in resume (Target: 15+)",
            "Word Count: Total words in your resume (Ideal: 400-1200 words)",
            "Keyword Density: Percentage of technical keywords (Target: 5-10%)"
        )

        for (meaning in metricsMeaning) {
            document.add(Paragraph("• $meaning\n", Font(Font.FontFamily.HELVETICA, 9f)))
        }

        document.add(Paragraph("\n"))
        addSectionTitle(document, "💡 Recommendations")

        if (analysis.keywords.foundKeywords.size < 12) {
            document.add(Paragraph(
                "🔴 Action Verbs: Add more impactful action verbs (Led, Architected, Optimized, Transformed, Spearheaded)\n",
                Font(Font.FontFamily.HELVETICA, 10f)
            ))
        }
        if (analysis.keywords.foundKeywords.size < 15) {
            document.add(Paragraph(
                "🟡 Tech Keywords: Expand technical skills (Docker, Kubernetes, AWS, CI/CD, Jenkins, REST APIs, Git)\n",
                Font(Font.FontFamily.HELVETICA, 10f)
            ))
        }
        if (analysis.keywords.keywordDensity < 5) {
            document.add(Paragraph(
                "🟡 Keyword Density: Increase technical keyword percentage to 5-10%\n",
                Font(Font.FontFamily.HELVETICA, 10f)
            ))
        }
        if (wordCount < 400) {
            document.add(Paragraph(
                "🔴 Word Count: Resume is too brief. Expand to minimum 400 words for better ATS performance\n",
                Font(Font.FontFamily.HELVETICA, 10f)
            ))
        } else if (wordCount > 1200) {
            document.add(Paragraph(
                "🟡 Word Count: Resume is too long. Trim to maximum 1200 words, maintaining focus on achievements\n",
                Font(Font.FontFamily.HELVETICA, 10f)
            ))
        } else {
            document.add(Paragraph(
                "✅ Word Count: Your resume length is optimal\n",
                Font(Font.FontFamily.HELVETICA, 10f)
            ))
        }
    }

    private fun addContactPresencePage(document: Document, analysis: ATSAnalysisResult) {
        addPageHeader(document, "📱 CONTACT & ONLINE PRESENCE")

        addSectionTitle(document, "✅ Contact Information Status")

        // Detect LinkedIn, GitHub, Portfolio from analysis
        val lower = analysis.extractedText.lowercase()
        val hasLinkedIn = listOf("linkedin.com", "linkedin", "in/").any { lower.contains(it) }
        val hasGitHub = listOf("github.com", "github", "gh").any { lower.contains(it) }
        val hasPortfolio = listOf("portfolio", "personal website", "portfolio site").any { lower.contains(it) }

        val contactItems = listOf(
            Triple("Email Address", if (analysis.sections.hasContactInfo) "✅ PRESENT" else "❌ MISSING", "✅ CRITICAL"),
            Triple("Phone Number", "✅ PRESENT", "✅ CRITICAL"), // Assuming present from analysis
            Triple("LinkedIn Profile", if (hasLinkedIn) "✅ PRESENT" else "❌ MISSING", "🟡 HIGHLY IMPORTANT"),
            Triple("GitHub Profile", if (hasGitHub) "✅ PRESENT" else "❌ MISSING", "🟡 IMPORTANT FOR TECH"),
            Triple("Personal Portfolio", if (hasPortfolio) "✅ PRESENT" else "❌ MISSING", "🟢 BENEFICIAL")
        )

        val contactTable = PdfPTable(3)
        contactTable.widthPercentage = 100f
        contactTable.setWidths(floatArrayOf(1.5f, 1.2f, 1.3f))
        contactTable.spacingAfter = 20f

        arrayOf("Item", "Status", "Importance").forEach {
            addTableHeader(contactTable, it)
        }

        for ((item, status, importance) in contactItems) {
            // Item Cell
            val itemCell = PdfPCell(Paragraph(item, Font(Font.FontFamily.HELVETICA, 10f)))
            itemCell.setPadding(10f)
            contactTable.addCell(itemCell)

            // Status Cell
            val statusColor = if (status.contains("PRESENT")) BaseColor(76, 175, 80) else BaseColor(244, 67, 54)
            val statusCell = PdfPCell(Paragraph(status, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, statusColor)))
            statusCell.setPadding(10f)
            statusCell.horizontalAlignment = Element.ALIGN_CENTER
            contactTable.addCell(statusCell)

            // Importance Cell
            val importanceCell = PdfPCell(Paragraph(importance, Font(Font.FontFamily.HELVETICA, 9f)))
            importanceCell.setPadding(10f)
            importanceCell.horizontalAlignment = Element.ALIGN_CENTER
            contactTable.addCell(importanceCell)
        }

        document.add(contactTable)

        document.add(Paragraph("\n"))
        addSectionTitle(document, "🌐 Why Online Presence Matters")

        val reasons = listOf(
            "LinkedIn: Recruiters verify experience and endorsements here (adds credibility)",
            "GitHub: Showcases real projects and coding ability (for tech roles)",
            "Portfolio: Demonstrates your best work and tangible outcomes",
            "Professional Domains: Better than generic emails for first impressions"
        )

        for (reason in reasons) {
            document.add(Paragraph("• $reason\n", Font(Font.FontFamily.HELVETICA, 10f)))
        }

        document.add(Paragraph("\n"))
        addSectionTitle(document, "📋 What's Missing")

        val missing = mutableListOf<String>()
        if (!hasLinkedIn) missing.add("LinkedIn Profile")
        if (!hasGitHub) missing.add("GitHub Profile")
        if (!hasPortfolio) missing.add("Personal Portfolio")

        if (missing.isEmpty()) {
            document.add(Paragraph(
                "✅ Excellent! You have all important online profiles.",
                Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor(76, 175, 80))
            ))
        } else {
            document.add(Paragraph(
                "Add these to strengthen your application: ${missing.joinToString(", ")}",
                Font(Font.FontFamily.HELVETICA, 10f)
            ))
        }
    }

    private fun addStrengthsPage(document: Document, analysis: ATSAnalysisResult) {
        addPageHeader(document, "⭐ YOUR RESUME STRENGTHS")

        if (analysis.strengths.isEmpty()) {
            document.add(Paragraph("No major strengths identified - focus on improvements", Font(Font.FontFamily.HELVETICA, 11f)))
            return
        }

        for ((index, strength) in analysis.strengths.take(8).withIndex()) {
            val titleP = Paragraph(
                "${index + 1}. ${strength}",
                Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, successColor)
            )
            titleP.spacingAfter = 8f
            document.add(titleP)
        }

        document.add(Paragraph("\n"))
        addSectionTitle(document, "✅ Keep Doing")

        document.add(Paragraph(
            "Maintain and expand these strong areas. They are key differentiators in ATS systems.",
            Font(Font.FontFamily.HELVETICA, 10f)
        ))
    }

    private fun addWeaknessesPage(document: Document, analysis: ATSAnalysisResult) {
        addPageHeader(document, "⚠️ AREAS FOR IMPROVEMENT")

        if (analysis.weaknesses.isEmpty()) {
            document.add(Paragraph("Your resume is well-optimized!", Font(Font.FontFamily.HELVETICA, 11f, Font.NORMAL, successColor)))
            return
        }

        for ((index, weakness) in analysis.weaknesses.take(8).withIndex()) {
            val titleP = Paragraph(
                "${index + 1}. ${weakness}",
                Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, dangerColor)
            )
            titleP.spacingAfter = 8f
            document.add(titleP)
        }

        document.add(Paragraph("\n"))
        addSectionTitle(document, "🎯 Priority Fixes")

        val highPriority = analysis.suggestions.filter { it.priority.name == "HIGH" }.take(3)
        for (suggestion in highPriority) {
            document.add(Paragraph(
                "🔴 ${suggestion.title}: ${suggestion.description}\n",
                Font(Font.FontFamily.HELVETICA, 10f)
            ))
        }
    }

    private fun addRecommendationsPage(document: Document, analysis: ATSAnalysisResult) {
        addPageHeader(document, "💡 DETAILED RECOMMENDATIONS")

        val highPriority = analysis.suggestions.filter { it.priority.name == "HIGH" }
        val mediumPriority = analysis.suggestions.filter { it.priority.name == "MEDIUM" }
        val lowPriority = analysis.suggestions.filter { it.priority.name == "LOW" }

        if (highPriority.isNotEmpty()) {
            addRecommendationSection(document, "🔴 HIGH PRIORITY (Implement First)", highPriority.take(5), dangerColor)
            document.add(Paragraph("\n"))
        }

        if (mediumPriority.isNotEmpty()) {
            addRecommendationSection(document, "🟡 MEDIUM PRIORITY (Highly Recommended)", mediumPriority.take(4), warningColor)
            document.add(Paragraph("\n"))
        }

        if (lowPriority.isNotEmpty()) {
            addRecommendationSection(document, "🟢 LOW PRIORITY (Nice to Have)", lowPriority.take(3), successColor)
        }
    }

    private fun addActionPlanPage(document: Document, analysis: ATSAnalysisResult) {
        addPageHeader(document, "🚀 YOUR ACTION PLAN")

        addSectionTitle(document, "This Week (Immediate Actions)")

        val immediateActions = listOf(
            "Review all 🔴 HIGH priority recommendations",
            "Fix critical contact information and missing sections",
            "Add quantifiable metrics to your achievements",
            "Increase action verbs in your job descriptions"
        )

        for ((index, action) in immediateActions.withIndex()) {
            document.add(Paragraph("${"□"}  ${index + 1}. $action\n", Font(Font.FontFamily.HELVETICA, 10f)))
        }

        document.add(Paragraph("\n"))
        addSectionTitle(document, "Next 2 Weeks (Medium-term)")

        val mediumActions = listOf(
            "Expand skills section with 12+ technical keywords",
            "Create/update LinkedIn and GitHub profiles",
            "Add quantifiable business metrics to accomplishments",
            "Implement 🟡 MEDIUM priority recommendations"
        )

        for ((index, action) in mediumActions.withIndex()) {
            document.add(Paragraph("${"□"}  ${index + 1}. $action\n", Font(Font.FontFamily.HELVETICA, 10f)))
        }

        document.add(Paragraph("\n"))
        addSectionTitle(document, "Success Criteria")

        val successCriteria = when {
            analysis.overallScore >= 80 -> listOf(
                "✅ Maintain current excellent score",
                "✅ Re-verify after 6 months of job changes",
                "✅ Ready for top-tier company applications"
            )
            else -> listOf(
                "✅ Target: Reach 75+ score",
                "✅ Complete all HIGH priority recommendations",
                "✅ Add minimum 12 technical keywords",
                "✅ Include at least 2-3 quantifiable metrics"
            )
        }

        for (criteria in successCriteria) {
            document.add(Paragraph(criteria + "\n", Font(Font.FontFamily.HELVETICA, 10f)))
        }

        document.add(Paragraph("\n\n"))

        val footer = Paragraph(
            "Generated: ${getCurrentDate()}\n" +
                    "Analyzer: Advanced ATS Calculator\n" +
                    "Your resume was analyzed against current ATS systems best practices",
            Font(Font.FontFamily.HELVETICA, 8f, Font.ITALIC, BaseColor.GRAY)
        )
        footer.alignment = Element.ALIGN_CENTER
        document.add(footer)
    }

    // ==================== HELPER FUNCTIONS ====================

    private fun addPageHeader(document: Document, title: String) {
        val header = Paragraph(title, Font(Font.FontFamily.HELVETICA, 24f, Font.BOLD, primaryColor))
        header.spacingAfter = 15f
        document.add(header)

        val line = Paragraph("_".repeat(95))
        line.spacingAfter = 20f
        document.add(line)
    }

    private fun addSectionTitle(document: Document, title: String) {
        val header = Paragraph(title, Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD, primaryColor))
        header.spacingBefore = 12f
        header.spacingAfter = 10f
        document.add(header)
    }

    private fun addInfoBox(document: Document, status: String, score: Int, color: BaseColor) {
        val cell = PdfPCell().apply {
            borderColor = color
            borderWidth = 3f
            backgroundColor = lightGray
            setPadding(15f)
            horizontalAlignment = Element.ALIGN_CENTER

            val text = "$status - Score: $score/100"
            addElement(Paragraph(text, Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD, color)))
        }

        val table = PdfPTable(1)
        table.widthPercentage = 100f
        table.spacingAfter = 20f
        table.addCell(cell)
        document.add(table)
    }

    private fun addStatCell(table: PdfPTable, label: String, value: String, color: BaseColor) {
        val labelP = Paragraph(label, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD))
        val valueP = Paragraph(value, Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, color))

        val cell = PdfPCell()
        cell.backgroundColor = lightGray
        cell.setPadding(10f)
        cell.horizontalAlignment = Element.ALIGN_CENTER
        cell.addElement(labelP)
        cell.addElement(Paragraph(" "))
        cell.addElement(valueP)

        table.addCell(cell)
    }

    private fun addScoreBar(document: Document, label: String, score: Int) {
        val statusColor = when {
            score >= 85 -> successColor
            score >= 70 -> warningColor
            else -> dangerColor
        }

        val labelP = Paragraph(
            "$label: $score/100",
            Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD)
        )
        labelP.spacingAfter = 5f
        document.add(labelP)

        val barLength = (score / 5).coerceIn(1, 20)
        val bar = "█".repeat(barLength) + "░".repeat(20 - barLength)
        val barP = Paragraph(bar, Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL, statusColor))
        barP.spacingAfter = 12f
        document.add(barP)
    }

    private fun addTableHeader(table: PdfPTable, text: String) {
        val cell = PdfPCell(Paragraph(text, Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor.WHITE)))
        cell.backgroundColor = primaryColor
        cell.setPadding(10f)
        cell.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(cell)
    }

    private fun addRecommendationSection(document: Document, title: String, suggestions: List<com.example.feature_student.model.Suggestion>, color: BaseColor) {
        val header = Paragraph(title, Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, color))
        header.spacingAfter = 10f
        document.add(header)

        for (suggestion in suggestions) {
            val titleP = Paragraph(suggestion.title, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, color))
            titleP.spacingAfter = 3f
            document.add(titleP)

            val descP = Paragraph("→ ${suggestion.description}\n", Font(Font.FontFamily.HELVETICA, 9f))
            descP.spacingAfter = 8f
            document.add(descP)
        }
    }

    private fun getStatusInfo(score: Int): Triple<String, String, BaseColor> {
        return when {
            score >= 85 -> Triple("🟢", "EXCELLENT - ATS READY", successColor)
            score >= 75 -> Triple("🟢", "GOOD - COMPETITIVE", successColor)
            score >= 65 -> Triple("🟡", "FAIR - NEEDS OPTIMIZATION", warningColor)
            score >= 55 -> Triple("🟡", "POOR - SIGNIFICANT WORK NEEDED", warningColor)
            else -> Triple("🔴", "CRITICAL - MAJOR CHANGES REQUIRED", dangerColor)
        }
    }

    private fun createProgressBar(percentage: Int): String {
        val filled = (percentage / 5).coerceIn(0, 20)
        val empty = 20 - filled
        return "▮".repeat(filled) + "▯".repeat(empty) + " $percentage%"
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


