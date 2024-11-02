package views

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.yash.weebo.R
import org.koin.androidx.compose.koinViewModel
import viewmodel.ImageViewmodel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    @Composable
    fun ImageDetail(
        modifier: Modifier = Modifier,
        painter: String?,
        url: String,
        copyright: String?,
        character: String?,
        tagString: String?,
        artist: String?,
        meta: String?,
        fileType: String?,
        navHostController: NavHostController
    ) {
        val tagList = tagString?.split(" ")
        val viewmodel: ImageViewmodel = koinViewModel()
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(url)
            .size(Size.ORIGINAL)
            .build()
    )
        var hideUI by remember {
            mutableStateOf(false)
        }
        Scaffold(
            content = {
                Column(Modifier.padding(it).fillMaxSize().scrollable(enabled = true, orientation = Orientation.Horizontal, state = rememberScrollState())) {
                    Row(Modifier.fillMaxSize()) {
                        CoilImage(
                            imageRequest = { painter.request },
                            Modifier.fillMaxSize(),
                            imageOptions = ImageOptions(
                                contentScale = ContentScale.FillWidth,
                            ),
                        )
                        Icon(painter = painterResource(R.drawable.baseline_arrow_downward_24),"Downloads", modifier = Modifier.clickable(
                            onClick = {
                                viewmodel.downloadFile(url, character.toString()+copyright.toString(),
                                    fileType.toString()
                                )
                            }
                        ))
                    }
                }
            },
            bottomBar = {
                Icon(painter = painterResource(R.drawable.baseline_arrow_downward_24),"Downloads", modifier = Modifier.clickable(
                    onClick = {
                        viewmodel.downloadFile(url, character.toString()+copyright.toString(),
                            fileType.toString()
                        )
                    }
                ))
            }
        )
    }