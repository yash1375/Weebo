package views
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.transformation.blur.BlurTransformationPlugin
import com.yash.weebo.ImageDetailScreen
import data.ImageEntity
import data.Variants
import org.koin.androidx.compose.koinViewModel
import viewmodel.ImageViewmodel


    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ImagesGrid(
        modifier: Modifier = Modifier,
        columnNumber: Int = 3,
        navController: NavHostController,
        imageViewmodel: ImageViewmodel = koinViewModel(),
        unblur: State<Boolean?>,
        nsfw: State<Boolean?>,
        context : Context,
        image: LazyPagingItems<ImageEntity>,
        gridNumber: State<Int?>,
        gridType: State<Boolean?>
    ) {
        val state = rememberLazyStaggeredGridState(
            initialFirstVisibleItemIndex = imageViewmodel.scrollPosition,
            initialFirstVisibleItemScrollOffset = imageViewmodel.scrollOffset,
        )
        LaunchedEffect(state) {
            snapshotFlow { state.firstVisibleItemIndex to state.firstVisibleItemScrollOffset }.collect{
                (index,offset) -> imageViewmodel.scrollPosition = index
                imageViewmodel.scrollOffset = offset
            }
        }
        LazyVerticalStaggeredGrid(
            state = state,
            columns = if(gridType.value!! == false) {
                StaggeredGridCells.Fixed(gridNumber.value!!)
            } else {
                StaggeredGridCells.Adaptive(gridNumber.value!!.dp)
            },
            modifier = modifier.background(MaterialTheme.colorScheme.background),
            content = {
                items(image.itemCount,
                    key = image.itemKey { it.id.hashCode()
                    }
                ){ index ->
                    if (!image[index]?.mediaAsset?.variants.isNullOrEmpty()) {
                        val painter = loadImagePainter(image[index]!!,context)
                        imageList(
                            imageparserData =  ImageparserData(
                                variants = image[index]!!.mediaAsset?.variants,
                                copyright = image[index]!!.tagStringCopyright.toString(),
                                character = image[index]!!.tagStringCharacter.toString(),
                                tagString = image[index]!!.tagStringGeneral.toString(),
                                artist = image[index]!!.tagStringArtist.toString(),
                                meta = image[index]!!.tagStringMeta.toString(),
                                rating = image[index]!!.rating,
                            ),
                            painter = painter.request,
                            nsfw = nsfw.value!!,
                            unblur = unblur.value!!,
                            onClick = { data ->
                                navController.navigate(
                                    ImageDetailScreen(
                                        painter = data.painter,
                                        url = data.url,
                                        copyright = data.copyright,
                                        character = data.character,
                                        tagString = data.tagString,
                                        artist = data.artist,
                                        meta = data.meta,
                                        fileType = data.fileType
                                    )
                                )
                            },
                            aspectRatio = image[index]!!.mediaAsset?.variants!![1]!!.width!!.toFloat() / image[index]!!.mediaAsset?.variants!![1]!!.height!!.toFloat()
                        )
                    }
                }
            }
        )

    }


    @Composable
    fun imageList(
        imageparserData: ImageparserData,
        painter: ImageRequest,
        modifier: Modifier = Modifier,
        unblur: Boolean,
        nsfw: Boolean,
        onClick: (ImageDetailScreen) -> Unit,
        aspectRatio: Float
    ) {
        val blurImage =
            rememberImageComponent {
                if (imageparserData.rating == 'e' || imageparserData.rating == 'q') {
                    if (!unblur) {
                        add(BlurTransformationPlugin(200))
                    }
                }
        }

        val onclick = {
            if (imageparserData.variants?.get(imageparserData.variants.size - 1)?.url != null) {
                onClick(
                    ImageDetailScreen(
                        painter = painter.diskCacheKey,
                        url = imageparserData.variants.get(imageparserData.variants.size - 2).url.toString(),
                        artist = imageparserData.artist,
                        meta = imageparserData.meta,
                        fileType = imageparserData.variants!!.get(
                            imageparserData.variants.size - 1
                        ).type,
                        character = imageparserData.character,
                        copyright = imageparserData.copyright,
                        tagString = imageparserData.tagString
                    )
                )
            }
        }
        LaunchedEffect(unblur) {
            if (imageparserData.rating == 'e' || imageparserData.rating == 'q') {
                if (!unblur) {
                    blurImage.add(BlurTransformationPlugin(200))
                } else {
                    blurImage.remove(BlurTransformationPlugin())
                }
            }
        }
        Card(onClick = onclick, modifier = Modifier.fillMaxWidth().padding(5.dp), shape = RoundedCornerShape(30.dp)) {
                CoilImage(
                    imageRequest = { painter },
                    modifier
                        .aspectRatio(
                            (aspectRatio)
                        ),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.FillHeight,
                    ),
                    component = blurImage
                )
            }
        }
@Stable
data class ImageparserData(
    val variants: ArrayList<Variants>?,
    val copyright: String,
    val character: String,
    val tagString: String,
    val artist: String,
    val meta: String,
    val rating: Char,
)
@Composable
fun loadImagePainter(item: ImageEntity, context: Context) =
    rememberAsyncImagePainter(
        ImageRequest.Builder(context)
            .data(item.mediaAsset?.variants?.get(2)?.url)
            .crossfade(true)
            .size(Size.ORIGINAL)
            .diskCacheKey(item.mediaAsset?.pixelHash.toString())
            .build()
    )