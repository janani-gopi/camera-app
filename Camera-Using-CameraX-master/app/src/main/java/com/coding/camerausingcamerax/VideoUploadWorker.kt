package com.coding.camerausingcamerax
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException

class VideoUploadWorker (
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams){
    override fun doWork(): Result {
        // Retrieve file details from inputData
        val videoPath = inputData.getString("video_path") ?: return Result.failure()
        val fileName = inputData.getString("file_name") ?: return Result.failure()

        // Create or get the video file from the path
        val videoFile = File(videoPath, fileName)
        if (!videoFile.exists()) {
            // Create the file if it doesn't exist (this step is for demonstration)
            // In a real-world scenario, the video file is usually created somewhere else
            try {
                if (!videoFile.createNewFile()) {
                    return Result.failure()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return Result.failure()
            }
        }

        // Upload the video and handle the response
        val response = uploadVideo(videoFile)

        return if (response.isSuccessful) {
            Result.success()
        } else {
            Result.retry() // Retry if the upload fails
        }
    }

    private fun uploadVideo(videoFile: File): Response {


        // Prepare the multipart body for file upload
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FOval client = OkHttpClient()RM)
            .addFormDataPart(
                "file", videoFile.name,
                RequestBody.create("video/mp4".toMediaTypeOrNull(), videoFile)
            )
            .build()

        // Create the POST request to the API endpoint
        val request = Request.Builder()
            .url("https://api.cloudinary.com/v1_1/dvwtwyunm/video/upload") // Replace with your API endpoint
            .post(requestBody)
            .build()

        // Execute the request
        return client.newCall(request).execute()
    }
}