package data

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.yash.weebo.R

class DownloadCompleteReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if(p1?.action == "android.intent.action.DOWNLOAD_COMPLETE"){
            val id = p1.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            val notificationManager = p0?.getSystemService(NotificationManager::class.java) as NotificationManager
            if(id != -1L){
                val notification = NotificationCompat.Builder(
                    p0,
                    "channel_id"
                ).setContentTitle("Download Done")
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setContentText("$id").build()
                notificationManager.notify(
                    (1..1000).random(),
                    notification
                )
            }
        }
    }

}