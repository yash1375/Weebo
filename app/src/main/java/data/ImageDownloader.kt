package data

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class ImageDownloader(
    context: Context
) : Downloader{
    private val  downloadManager = context.getSystemService(DownloadManager::class.java)
    override fun downloadfile(url: String,name : String,fileType: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/$fileType")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(name)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "/Weebo/$name.$fileType")
        return downloadManager.enqueue(request)
    }

}