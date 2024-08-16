package paging

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import data.ImageDao
import data.ImageDatabase
import data.ImageEntity
import data.ImageRepo
import kotlinx.coroutines.flow.Flow
@OptIn(ExperimentalPagingApi::class)
class ImagePager(
   private val imageRemoteMediator: ImageRemoteMediator,
    private val database: ImageDao,
    private val imageRepo: ImageRepo
) {
    fun getPost(): Flow<PagingData<ImageEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 60, enablePlaceholders = true),
            remoteMediator = imageRemoteMediator,
            pagingSourceFactory = {database.getAll()}
        ).flow
    }

    fun getSarchImage(string: String): Flow<PagingData<ImageEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 60, enablePlaceholders = true),
            pagingSourceFactory = {
                SeachgTagImage(search = string, imageRepo = imageRepo)
            }
        ).flow
    }
}