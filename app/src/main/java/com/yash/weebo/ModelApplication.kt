package com.yash.weebo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import di.myModule
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ModelApplication: Application(),ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        val context = this
        startKoin {
            modules(myModule)
            androidContext(context)
        }
        val channel = NotificationChannel(
            "channel_id",
            "Download complete",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManeger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManeger.createNotificationChannel(channel)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder()
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.03)
                    .directory(cacheDir)
                    .build()
            }
            .memoryCachePolicy(CachePolicy.DISABLED)
            .dispatcher(Dispatchers.IO)
            .logger(DebugLogger())
            .build()
    }

}