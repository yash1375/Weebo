package paging

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingConfig.Companion.MAX_SIZE_UNBOUNDED
import androidx.paging.PagingData
import androidx.paging.liveData
import data.FavEntity
import data.ImageDao
import data.ImageDatabase
import data.ImageEntity
import data.ImageRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class ImagePager(
   private val imageRemoteMediator: ImageRemoteMediator,
    private val database: ImageDao,
    private val imageRepo: ImageRepo
) {
    fun getPost(): Flow<PagingData<ImageEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 60, enablePlaceholders = true, initialLoadSize = 20),
            remoteMediator = imageRemoteMediator,
            pagingSourceFactory = {database.getAll()}
        ).flow
    }

    fun getSfwPost(): Flow<PagingData<ImageEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 60, enablePlaceholders = true, initialLoadSize = 20),
            remoteMediator = imageRemoteMediator,
            pagingSourceFactory = {database.getSfw()}
        ).flow
    }
    fun getSarchImage(string: String,nsfw:Boolean): Flow<PagingData<ImageEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 60, enablePlaceholders = true),
            pagingSourceFactory = {
                SeachgTagImage(search = string, imageRepo = imageRepo, nsfw = nsfw)
            }
        ).flow
    }

        fun getFavImage(): Flow<PagingData<FavEntity>>{
            return Pager(
                config = PagingConfig(pageSize = 60, enablePlaceholders = true, initialLoadSize = 20),
                pagingSourceFactory = {database.getFavAll()}
            ).flow
        }
    suspend fun getList(): List<Int>{
        return database.getAllfav()
    }
}