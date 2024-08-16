package views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.yash.weebo.ImageDetailScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import viewmodel.ImageViewmodel

class SearchImage {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun Search(navController: NavController,pretag:String? = null){
        val imageViewmodel : ImageViewmodel = koinViewModel()
        var tag by remember {
            mutableStateOf(if (pretag == null) "" else pretag.toString())
        }
        val search by mutableStateOf(imageViewmodel.seachpage.collectAsLazyPagingItems())
        val post by mutableIntStateOf(search.itemCount)
        val nsfw = imageViewmodel.nsfw.collectAsStateWithLifecycle(initialValue = false)
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            imageViewmodel.searchImage(pretag)
            search.refresh()
        }
        Scaffold (
            topBar = {
                TopAppBar(title = {
                TextField(value = tag, onValueChange = {
                    tag = it
                },
                    placeholder = { Text(text = "search") },
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "search",
                            Modifier.clickable(onClick = {
                                scope.launch{
                                    search.refresh()
                                    imageViewmodel.searchImage(tag)
                                }
                            })
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            scope.launch{
                                search.refresh()
                                imageViewmodel.searchImage(tag)
                            }
                        }
                    )
                )
                })
            },
            content = {
                Column(modifier = Modifier.padding(it)) {
                    Row(
                        Modifier
                            .weight(0.5f)
                            .padding(0.dp, 10.dp)) {
                        Text(text = "POSTS $post")
                    }
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(3),
                        verticalItemSpacing = 10.dp,
                        modifier = Modifier.weight(9f),
                    ) {
                        items(search.itemCount) { index ->
                            if ((search[index]!!.mediaAsset?.variants?.size ?: -1) > 0) {
                                imageList(
                                    width = search[index]?.mediaAsset?.variants?.get(2)?.width?.toFloat(),
                                    height = search[index]?.mediaAsset?.variants?.get(2)?.height?.toFloat(),
                                    url = search[index]?.mediaAsset?.variants?.get(2)?.url,
                                    copyright = search[index]?.tagStringCopyright,
                                    character = search[index]?.tagStringCharacter,
                                    tagString = search[index]?.tagStringGeneral,
                                    artist = search[index]?.tagStringArtist,
                                    meta = search[index]?.tagStringMeta,
                                    rating = search[index]!!.rating,
                                    fileType = search[index]?.mediaAsset?.variants?.get(2)?.fileExt,
                                    nsfw = nsfw.value!!,
                                    onClick = { url, copyright, character, tagString, artist, meta, fileName ->
                                        navController.navigate(
                                            ImageDetailScreen(
                                                url,
                                                copyright,
                                                character,
                                                tagString,
                                                artist,
                                                meta,
                                                fileName,
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}