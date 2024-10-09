package com.project.tubocare.profile.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalTime

suspend fun downloadAndSaveImageProfile(context: Context, imageUrl: String, userId: String): String {
    return withContext(Dispatchers.IO) {
        val localFile = File(context.filesDir, "profile_images/$userId.jpg")

        Log.d("ProfileImageDownload", "Checking if local file exists: ${localFile.exists()}")

        if (localFile.exists()) {
            localFile.delete()
            Log.d("ProfileImageDownload", "Deleted existing local file.")
        }

        val bitmap = Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .submit()
            .get()

        localFile.parentFile?.mkdirs()
        val outputStream = FileOutputStream(localFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        Log.d("ProfileImageDownload", "Saved new image to: ${localFile.absolutePath}")
        return@withContext localFile.absolutePath
    }
}