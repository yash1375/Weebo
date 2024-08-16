package views

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash.weebo.R
import com.yash.weebo.SEARCH
import org.koin.androidx.compose.koinViewModel
import viewmodel.ImageViewmodel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    @Composable
    fun ImageDetail(
        modifier: Modifier = Modifier,
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
        var hideUI by remember {
            mutableStateOf(false)
        }
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Weebo")
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back arrow"
                        )
                        Modifier.clickable(onClick = { navHostController.popBackStack() })
                    }
                )
            },
            content = {
                Column(
                    modifier
                        .padding(it)
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState()
                        )
                        .clickable(
                            onClick = {
                                hideUI != hideUI
                            }
                        )
                ) {
//                AsyncImage(model = url, contentDescription = null, Modifier.weight(9f))
//                AnimatedVisibility(visible = hideUI) {
//                    Text(text = "Anime name : $copyright",Modifier.weight(1f))
                    AsyncImage(
                        model = url, contentDescription = null, modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                    Spacer(
                        modifier = Modifier
                            .height(15.dp)
                            .fillMaxWidth()
                    )
                    FlowRow {
                        Text(
                            text = "Anime name : $copyright",
                            Modifier.padding(10.dp)
                        )
                        Button(
                            onClick = {
                                viewmodel.downloadFile(
                                    url,
                                    character.toString(),
                                    fileType.toString()
                                )
                            }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_arrow_downward_24),
                                contentDescription = "Downloads"
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(15.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = "Character : $character",
                        Modifier.padding(10.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(15.dp)
                            .fillMaxWidth()
                    )
                    FlowRow(
                        Modifier
                            .padding(10.dp)
                            .padding(5.dp)
                    ) {
                        if (tagList != null) {
                            for (tag in tagList) {
                                SuggestionChip(onClick = {
                                    navHostController.navigate(SEARCH(tag))
                                }, label = { Text(text = tag) })
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(15.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = "artist : $artist",
                        Modifier.padding(15.dp)
                    )
//                }
                }
            }
        )
    }