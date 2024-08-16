package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import data.ImageDataScore
import data.ImageDownloader
import data.ImageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import paging.ImagePager
import android.util.Log
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems

class ImageViewmodel(
    private val pager: ImagePager,
    private val imageDataScore: ImageDataScore,
    private val imageDownloader: ImageDownloader
) : ViewModel() {
    val posts: Flow<PagingData<ImageEntity>> = pager.getPost().cachedIn(viewModelScope)
    var seachpage: Flow<PagingData<ImageEntity>> = posts
    val nsfw :Flow<Boolean?> = imageDataScore.getNsfw
    fun downloadFile(url:String, name:String, fileType:String){
        imageDownloader.downloadfile(url,name,fileType)
    }
     fun updateNsfwToggle(boolean: Boolean) {
        viewModelScope.launch {
            imageDataScore.NsfwToggle(boolean)
        }
    }
    fun searchImage(tag:String?) {
        if (tag != null) {
            seachpage = pager.getSarchImage(tag)
        }
    }
}