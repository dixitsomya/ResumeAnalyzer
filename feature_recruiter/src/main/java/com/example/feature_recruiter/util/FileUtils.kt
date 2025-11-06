package com.example.feature_recruiter.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileUtils {

    @SuppressLint("Range")
    fun getFileNameFromUri(context: Context, uri: Uri): String {
        return when (uri.scheme) {
            ContentResolver.SCHEME_CONTENT -> {
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val displayName = it.getString(
                            it.getColumnIndex("_display_name")
                        )
                        displayName ?: "document"
                    } else {
                        "document"
                    }
                } ?: "document"
            }
            ContentResolver.SCHEME_FILE -> {
                File(uri.path ?: "").name
            }
            else -> "document"
        }
    }

    fun copyUriToFile(context: Context, uri: Uri, destFile: File): Boolean {
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getFileExtension(fileName: String): String {
        return fileName.substringAfterLast(".", "")
    }

    fun isPDFFile(fileName: String): Boolean {
        return getFileExtension(fileName).lowercase() == "pdf"
    }

    fun isDocFile(fileName: String): Boolean {
        val ext = getFileExtension(fileName).lowercase()
        return ext in listOf("doc", "docx", "txt")
    }
}
