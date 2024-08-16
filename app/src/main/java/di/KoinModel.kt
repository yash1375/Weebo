package di

import androidx.room.Room
import data.DownloadCompleteReceiver
import data.ImageDao
import data.ImageDataScore
import data.ImageDatabase
import data.ImageDownloader
import data.ImageRepo
import data.ImageRepoImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import paging.ImagePager
import paging.ImageRemoteMediator
import viewmodel.ImageViewmodel
import kotlin.math.sin

val myModule = module {
        single { ImageViewmodel(get(),get(),get()) }
        single { ImagePager(get(),get(),get()) }
        single { ImageRemoteMediator(get(),get()) }
    single { get<ImageDatabase>().ImageDao() }
        single { Room.databaseBuilder(
            androidApplication(),
            ImageDatabase::class.java, "db"
        ).build() }
        single<ImageRepo> {ImageRepoImpl(get(),get(),androidApplication())}
        single { Dispatchers.IO }
    single { get<ImageDatabase>().ImageDao() }
    single {ImageDataScore(androidApplication())}
    single { ImageDownloader(androidApplication()) }
    }