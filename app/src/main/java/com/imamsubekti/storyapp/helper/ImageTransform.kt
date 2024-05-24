package com.imamsubekti.storyapp.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageTransform(private val context: Context, private val imageUri: Uri) {
    private val filenameFormat = "yyyyMMdd_HHmmss"
    private val timeStamp: String = SimpleDateFormat(filenameFormat, Locale.US).format(Date())
    private val maxSize = 1000000

    private fun toFile(): File {
        val myFile = createCustomTempFile()
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    fun toMultipart(): MultipartBody.Part {
        val imageFile = toFile().reduceFileImage()
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        return MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
    }

    private fun createCustomTempFile(): File {
        val filesDir = context.externalCacheDir
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }

    private fun File.reduceFileImage(): File {
        val file = this
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > maxSize)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return file
    }
}