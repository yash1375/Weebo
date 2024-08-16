package views

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.room.Index
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.yash.weebo.HOME
import com.yash.weebo.ImageDetailScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.definition.indexKey
import viewmodel.ImageViewmodel
    @Composable
    public fun ImagesGrid(
        modifier: Modifier = Modifier,
        columnNumber: Int = 3,
        verticalPading: Int = 0,
        navController: NavHostController,
        imageViewmodel: ImageViewmodel = koinViewModel()
    ) {
        val image = imageViewmodel.posts.collectAsLazyPagingItems()
        val state = rememberLazyStaggeredGridState()
        val nsfwHide by imageViewmodel.nsfw.collectAsStateWithLifecycle(initialValue = false)
        LazyVerticalStaggeredGrid(
            state = state,
            columns = StaggeredGridCells.Fixed(columnNumber),
            verticalItemSpacing = verticalPading.dp,
            modifier = modifier
        ) {
            items(
                count = image.itemCount,
            ) { index ->
                if (image[index]!!.mediaAsset!!.variants.size > 0) {
                    imageList(
                        width = image[index]?.mediaAsset?.variants?.get(2)?.width?.toFloat(),
                        height = image[index]?.mediaAsset?.variants?.get(2)?.height?.toFloat(),
                        url = image[index]?.mediaAsset?.variants?.get(2)?.url,
                        copyright = image[index]?.tagStringCopyright,
                        character = image[index]?.tagStringCharacter,
                        tagString = image[index]?.tagStringGeneral,
                        artist = image[index]?.tagStringArtist,
                        meta = image[index]?.tagStringMeta,
                        rating = image[index]!!.rating,
                        fileType = image[index]?.mediaAsset?.variants?.get(2)?.fileExt,
                        nsfw = nsfwHide!!,
                        onClick = { url, copyright, character, tagString, artist, meta, fileName ->
                            navController.navigate(
                                ImageDetailScreen(
                                    url,
                                    copyright,
                                    character,
                                    tagString,
                                    artist,
                                    meta,
                                    fileName
                                )
                            )
                        },
                    )
                }
            }
        }

    }


    @Composable
    fun imageList(
        modifier: Modifier = Modifier,
        width: Float? = 600f,
        height: Float? = 400f,
        url: String? = "",
        copyright: String? = "",
        character: String? = "",
        tagString: String? = "",
        artist: String? = "",
        meta: String? = "",
        rating: Char,
        fileType: String?,
        nsfw: Boolean,
        onClick: (String, String?, String?, String?, String?, String?, String?) -> Unit,
    ) {
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context = LocalContext.current)
                .data(url)
                .size(Size.ORIGINAL)
                .crossfade(true).build()
        )
        var blurImage by remember {
            mutableIntStateOf(0)
        }
        LaunchedEffect(nsfw) {
            if ((rating == 'e' || rating == 'q') && !nsfw) {
                blurImage = 30
            }
        }
        Column(
            Modifier
                .border(1.dp, color = MaterialTheme.colorScheme.outline)
        ) {
            AsyncImage(
                model = painter.request, contentDescription = null,
                modifier
                    .fillMaxWidth()
                    .aspectRatio(
                        (width!! / height!!)
                    )
                    .clickable(onClick = {
                        if (url != null) {
                            onClick(url, copyright, character, tagString, artist, meta, fileType)
                        }
                    })
                    .blur(blurImage.dp),
                contentScale = ContentScale.FillHeight,
            )
        }
    }