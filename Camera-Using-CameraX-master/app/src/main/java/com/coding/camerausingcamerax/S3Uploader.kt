package com.coding.camerausingcamerax

import android.content.Context
import android.net.Uri
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class S3Uploader(
    private val context: Context,
    accessKey: String,
    secretKey: String,
    private val bucketName: String
) {
    private val s3Client: AmazonS3Client

    init {
        val awsCredentials = BasicAWSCredentials(accessKey, secretKey)
        s3Client = AmazonS3Client(awsCredentials)
    }

    suspend fun uploadVideo(videoUri: Uri, fileName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(videoUri)
            val metadata = ObjectMetadata().apply {
                contentType = "video/mp4"
            }

            val putObjectRequest = PutObjectRequest(bucketName, fileName, inputStream, metadata)
            s3Client.putObject(putObjectRequest)

            println("Video upload completed")
            true
        } catch (e: Exception) {
            println("Error during upload: ${e.message}")
            false
        }
    }
}