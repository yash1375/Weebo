package com.yash.weebo

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.yash.weebo.ui.theme.WeeboTheme
import data.ImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import navBar.BottomNavigation
import org.koin.androidx.compose.koinViewModel
import org.koin.core.scope.Scope
import viewmodel.ImageViewmodel
import views.FavImage
import views.ImageDetail
import views.ImagesGrid
import views.Setting

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeeboTheme {
                val context = this
                val imageViewmodel : ImageViewmodel = koinViewModel()
                val nsfw = imageViewmodel.nsfw.collectAsStateWithLifecycle(false)
                val unblur = imageViewmodel.unblur.collectAsStateWithLifecycle(false)
                val gridtype = imageViewmodel.gridType.collectAsStateWithLifecycle(false)
                val gridNumber = imageViewmodel.gridNumber.collectAsStateWithLifecycle(2)
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                val firstrun = imageViewmodel.firstRun.collectAsStateWithLifecycle(true)
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) {
                    if (it){
                        scope.launch {
                            imageViewmodel.FirstrunComplete()
                        }
                    }
                    else {
                    }
                }
                var pagingdata by remember { mutableStateOf(imageViewmodel.getALl(scope)) }
                var searchTrigger = imageViewmodel.searchTrigger.collectAsStateWithLifecycle()
                LaunchedEffect(
                    nsfw.value!!
                ) {
                    pagingdata =  if (nsfw.value!!){
                        imageViewmodel.getSfwALl(scope)
                    }
                    else{
                        imageViewmodel.getALl(scope)
                    }
                }
                if(searchTrigger.value!! == true){
                    LaunchedEffect(searchTrigger) {
                        pagingdata = imageViewmodel.searchImage(scope,nsfw.value!!)
                    }
                }
                var image = pagingdata.collectAsLazyPagingItems(Dispatchers.IO)
                if (firstrun.value!!) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        LaunchedEffect(Unit) {
                            permissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
                        }
                    }
                    else{
                        LaunchedEffect(Unit) {
                            imageViewmodel.FirstrunComplete()
                        }
                    }
                }
                NavHost(navController = navController, startDestination = HOME){
                    composable<HOME>{
                        ImageScreen(
                            navHostController = navController, imageViewmodel = imageViewmodel, nsfw = nsfw, unblur = unblur,
                            context = context, image = image, gridNumber = gridNumber, gridType = gridtype)
                    }
                    composable<ImageDetailScreen>(
                    ){
                        val agrs = it.toRoute<ImageDetailScreen>()
                        ImageDetail(painter = agrs.painter,url = agrs.url, copyright = agrs.copyright, tagString = agrs.tagString, meta = agrs.meta, artist = agrs.artist, character = agrs.character, fileType = agrs.fileType, navHostController = navController)
                    }
                    composable<SETTING>{
                        Setting().setting(navController = navController, nsfw = nsfw, unblur = unblur, imageViewmodel = imageViewmodel,gridNumber = gridNumber, gridType = gridtype)
                    }
                    composable<FAV>{
                        val agrs = it.toRoute<FAV>()
                        FavImage().Fav(navHostController = navController, imageViewmodel = imageViewmodel, nsfw = nsfw, unblur = unblur,
                            context = context, gridNumber = gridNumber, gridType = gridtype)
                    }
                }
                }
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScreen(
    modifier: Modifier = Modifier, navHostController: NavHostController, imageViewmodel: ImageViewmodel, unblur: State<Boolean?>, nsfw: State<Boolean?>,
    context: Context,
    image: LazyPagingItems<ImageEntity>,
    gridNumber: State<Int?>,
    gridType: State<Boolean?>
) {
    var bottomSheetScaffoldState = rememberModalBottomSheetState()
    var sheetshow by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var serachTag by remember { mutableStateOf("") }
    if(sheetshow) {
        ModalBottomSheet(
            sheetState = bottomSheetScaffoldState,
            onDismissRequest = {sheetshow = false},
            content = {
                Box(Modifier.fillMaxHeight()) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            Modifier.fillMaxWidth(0.88f).padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Search Filter", fontSize = 30.sp)
                                Text("DanBooru")
                            }
                            Button(onClick = {imageViewmodel.searchTrigger(serachTag)
                            sheetshow = false
                            }
                            ) {
                                Text("Save")
                            }
                        }
                        Spacer(
                            Modifier.fillMaxWidth().height(1.dp)
                                .border(1.dp, MaterialTheme.colorScheme.onTertiary)
                        )
                        Spacer(Modifier.fillMaxWidth().heightIn(20.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.Center) {
                            OutlinedTextField(
                                value = serachTag,
                                onValueChange = { serachTag = it },
                                Modifier.fillMaxWidth(0.9f),
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions (
                                    onSearch = {imageViewmodel.searchTrigger(serachTag)
                                    sheetshow = false
                                    }
                                ),
                                placeholder = { Text("Type Tag to Search") },
                                label = { Text("Search by tag") })
                        }
                    }
                }
            }
        )
    }
    Scaffold (
        content = {
                ImagesGrid(
                    modifier = Modifier.padding(it),
                    navController = navHostController,
                    unblur = unblur,
                    nsfw = nsfw,
                    context = context,
                    image = image,
                    gridNumber = gridNumber,
                    gridType = gridType
                )
        },
        floatingActionButton = {
                ExtendedFloatingActionButton(onClick = {
                    scope.launch{
                        sheetshow = true
                    }
                })
                {
                    Icon(
                        painter = painterResource(R.drawable.baseline_filter_list_alt_24),
                        contentDescription = "filter"
                    )
                    Text("Filter Image")
                }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = { BottomNavigation().BottomNavigationBar(navController = navHostController)}
    )
}

@Serializable
object HOME

@Serializable
object SETTING

@Serializable
data class FAV(
   val tag: String? = null
)

@Stable
@Serializable
data class ImageDetailScreen(
    val url: String,
    val copyright: String? = "",
    val character: String? = "",
    val tagString: String? = "",
    val artist: String? = "",
    val meta: String? = "",
    val fileType: String?,
    val painter: String?,
)

