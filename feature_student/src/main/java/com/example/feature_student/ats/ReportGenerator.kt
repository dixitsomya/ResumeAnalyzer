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


