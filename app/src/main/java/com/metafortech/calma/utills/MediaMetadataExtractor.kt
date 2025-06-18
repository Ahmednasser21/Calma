package com.metafortech.calma.utills

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaMetadataExtractor @Inject constructor(
    @ApplicationContext private val context: Context
) {

    data class MediaMetadata(
        val duration: Long? = null,
        val thumbnailUri: Uri? = null
    )

    suspend fun extractVideoMetadata(videoUri: Uri): MediaMetadata = withContext(Dispatchers.IO) {
        try {
            val duration = extractDuration(videoUri)
            val thumbnailUri = generateThumbnail(videoUri)

            MediaMetadata(
                duration = duration,
                thumbnailUri = thumbnailUri
            )
        } catch (_: Exception) {
            MediaMetadata()
        }
    }

    suspend fun extractAudioDuration(audioUri: Uri): Long? = withContext(Dispatchers.IO) {
        try {
            extractDuration(audioUri)
        } catch (_: Exception) {
            null
        }
    }

    private fun extractDuration(mediaUri: Uri): Long? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, mediaUri)
            val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            durationString?.toLongOrNull()
        } catch (_: Exception) {
            null
        } finally {
            try {
                retriever.release()
            } catch (_: Exception) {

            }
        }
    }

    private suspend fun generateThumbnail(videoUri: Uri): Uri? = withContext(Dispatchers.IO) {
        val retriever = MediaMetadataRetriever()
        return@withContext try {
            retriever.setDataSource(context, videoUri)

            val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val duration = durationString?.toLongOrNull() ?: 0L

            val frameTime = maxOf(1000000L, duration * 1000 / 10)

            val bitmap = retriever.getFrameAtTime(
                frameTime,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            )

            bitmap?.let { saveBitmapToFile(it) }
        } catch (_: Exception) {
            null
        } finally {
            try {
                retriever.release()
            } catch (_: Exception) {
            }
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap): Uri {
        val thumbnailFile = File(
            context.cacheDir,
            "video_thumbnail_${System.currentTimeMillis()}.jpg"
        )

        FileOutputStream(thumbnailFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
        }

        return Uri.fromFile(thumbnailFile)
    }

    fun cleanupThumbnail(thumbnailUri: Uri?) {
        thumbnailUri?.path?.let { path ->
            try {
                val file = File(path)
                if (file.exists() && file.name.startsWith("video_thumbnail_")) {
                    file.delete()
                }
            } catch (e: Exception) {
                Log.e("MediaMetadataExtractor", "Failed to cleanup thumbnail", e)
            }
        }
    }
}
