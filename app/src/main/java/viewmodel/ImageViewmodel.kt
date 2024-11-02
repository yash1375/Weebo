package viewmodel

import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import data.ImageDataScore
import data.ImageDownloader
import data.ImageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import paging.ImagePager
import androidx.paging.cachedIn
import androidx.paging.map
import data.FavEntity
import data.ImageDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive

class ImageViewmodel(
    private val pager: ImagePager,
    private val imageDataScore: ImageDataScore,
    private val imageDownloader: ImageDownloader
) : ViewModel() {
    val firstRun : Flow<Boolean?> = imageDataScore.getFirstRun
    val gridNumber : Flow<Int?> = imageDataScore.gridNumber
    val gridType : Flow<Boolean?> = imageDataScore.gridType
    var scrollPosition = 0
    var scrollOffset = 0
    private val _searchTrigger = MutableStateFlow(false)
    val searchTrigger = _searchTrigger.asStateFlow()
    private val _searchTag = MutableStateFlow("")
    val searchTag = _searchTag.asStateFlow()
    val nsfw :Flow<Boolean?> = imageDataScore.getNsfw
    val unblur : Flow<Boolean?> = imageDataScore.getUnblur
    private val _getfav = MutableStateFlow<List<FavEntity>>(emptyList())
    val getfav: StateFlow<List<FavEntity>> = _getfav
    fun downloadFile(url:String, name:String, fileType:String){
        imageDownloader.downloadfile(url,name,fileType)
    }
    fun getALl(scope: CoroutineScope) :Flow<PagingData<ImageEntity>>{
        return  pager.getPost().cachedIn(scope)
    }
    fun getSfwALl(scope: CoroutineScope) :Flow<PagingData<ImageEntity>> {
        return pager.getSfwPost().cachedIn(scope)
    }
     fun updateNsfwToggle(boolean: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            imageDataScore.NsfwToggle(boolean)
        }
    }
    fun gridTypeChange(boolean: Boolean){
        viewModelScope.launch {
            imageDataScore.gridTypeChange(boolean)
        }
    }
    fun gridNumberChange(int: Int){
        viewModelScope.launch {
            imageDataScore.gridNumberChange(int)
        }
    }
    fun updateUnblurToggle(boolean: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            imageDataScore.UnblurToggle(boolean)
        }
    }
    fun searchTrigger(tag: String){
        _searchTrigger.value = true
        _searchTag.value = tag
    }
    fun searchImage(scope: CoroutineScope,nsfw:Boolean) : Flow<PagingData<ImageEntity>> {
        resetLazy()
        _searchTrigger.value = false
        return pager.getSarchImage(_searchTag.value,nsfw).cachedIn(scope)
    }
    fun Favdata() : Flow<PagingData<FavEntity>>{
        return  pager.getFavImage().cachedIn(viewModelScope)
        }

    suspend fun FirstrunComplete(){
        imageDataScore.FirstRunComplete(true)
    }
    fun resetLazy(){
        scrollPosition = 0
        scrollOffset = 0
    }
}