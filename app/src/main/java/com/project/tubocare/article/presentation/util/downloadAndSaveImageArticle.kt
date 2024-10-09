package com.project.tubocare.article.presentation.util

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

suspend fun downloadAndSaveImageArticle(context: Context, imageUrl: String, userId: String): String {
    return withContext(Dispatchers.IO) {
        val localFile = File(context.filesDir, "article_images/$userId.png")
        if (localFile.exists()) {
            return@withContext localFile.absolutePath
        }

        val bitmap = Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .submit()
            .get()

        localFile.parentFile?.mkdirs()
        val outputStream = FileOutputStream(localFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return@withContext localFile.absolutePath
    }
}