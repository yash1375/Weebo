package paging

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import data.ImageDao
import data.ImageDatabase
import data.ImageEntity
import data.ImageNetwork
import data.ImageRepo
import data.toImageEntity
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import viewmodel.ImageViewmodel

@OptIn(ExperimentalPagingApi::class)
class ImageRemoteMediator(
    private val networkService : ImageRepo,
    private val imageDatabase: ImageDatabase
) : RemoteMediator<Int, ImageEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>
    ): MediatorResult {
        return try {
            val page = when(loadType){
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    val lastitem = state.lastItemOrNull()
                    if (lastitem == null){
                        MediatorResult.Success(true)
                    }
                    (imageDatabase.ImageDao().getCount()/state.config.pageSize) + 1
                }
            }
            val response = networkService.getPost(page = page, limit = state.config.pageSize)

            imageDatabase.withTransaction {
                if (LoadType.REFRESH == loadType) {
                    networkService.clear()
                }
                imageDatabase.ImageDao().insert(
                    response.map { images ->
                        ImageNetwork.toImageEntity(images)
                    }
                )
            }
            MediatorResult.Success(response.isEmpty())
        }
        catch (e: Exception){
            MediatorResult.Error(e)

        }

    }
}