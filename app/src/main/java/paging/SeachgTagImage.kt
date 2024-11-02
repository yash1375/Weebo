package paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import data.ImageEntity
import data.ImageNetwork
import data.ImageRepo
import data.toImageEntity

class SeachgTagImage(
    val search : String,
    val imageRepo: ImageRepo,
    val nsfw : Boolean
) : PagingSource<Int, ImageEntity>() {
    override fun getRefreshKey(state: PagingState<Int, ImageEntity>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageEntity> {
        val currentPage = params.key ?: 1
        return try {
            val response =
                if (nsfw) {
                    imageRepo.getPost(currentPage, 60, search).filter { it.rating == 'g' }
                }
            else {
                    imageRepo.getPost(currentPage, 60, search)
                }
            val endOfPage = response.size
            if (response.isNotEmpty()){
                LoadResult.Page(
                    data = response.map {
                        ImageNetwork.toImageEntity(it)
                    },
                    prevKey = if (currentPage == 1) null else currentPage -1,
                    nextKey = if (endOfPage == 0) null else currentPage + 1
                )
            }
            else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e : Exception) {
            LoadResult.Error(e)
        }
    }
}