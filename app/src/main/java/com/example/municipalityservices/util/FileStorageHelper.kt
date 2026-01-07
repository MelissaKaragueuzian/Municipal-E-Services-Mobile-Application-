package com.example.municipalityservices.util

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FileStorageHelper(private val context: Context) {
    
    private val attachmentsDir: File
        get() = File(context.getExternalFilesDir(null), "attachments").apply {
            if (!exists()) mkdirs()
        }
    
    suspend fun saveAttachment(uri: Uri, requestId: Long): String = withContext(Dispatchers.IO) {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        inputStream?.use { input ->
            val fileName = getFileName(uri) ?: "attachment_${System.currentTimeMillis()}"
            val file = File(attachmentsDir, "${requestId}_$fileName")
            
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
            
            file.absolutePath
        } ?: throw Exception("Failed to read file")
    }
    
    fun getAttachmentFile(filePath: String): File? {
        return try {
            File(filePath).takeIf { it.exists() }
        } catch (e: Exception) {
            null
        }
    }
    
    fun getAttachmentUri(filePath: String): Uri? {
        val file = getAttachmentFile(filePath) ?: return null
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
    
    fun deleteAttachment(filePath: String) {
        try {
            File(filePath).delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path?.let {
                val cut = it.lastIndexOf('/')
                if (cut != -1) {
                    it.substring(cut + 1)
                } else {
                    it
                }
            }
        }
        return result
    }
}


