package com.yash.weebo

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.yash.weebo.ui.theme.WeeboTheme
import kotlinx.serialization.Serializable
import navBar.BottomNavigation
import okhttp3.internal.notify
import views.ImageDetail
import views.ImagesGrid
import views.SearchImage
import views.Setting

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeeboTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = HOME){
                    composable<HOME>{
                        ImageScreen(navHostController = navController)
                    }
                    composable<ImageDetailScreen>(
                    ){
                        val agrs = it.toRoute<ImageDetailScreen>()
                        ImageDetail(url = agrs.url, copyright = agrs.copyright, tagString = agrs.tagString, meta = agrs.meta, artist = agrs.artist, character = agrs.character, fileType = agrs.fileType, navHostController = navController)
                    }
                    composable<SETTING>{
                        Setting().setting(navController = navController)
                    }
                    composable<SEARCH>{
                        val agrs = it.toRoute<SEARCH>()
                        SearchImage().Search(navController = navController, pretag = agrs.tag)
                    }
                }
                }
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScreen(modifier: Modifier = Modifier,navHostController: NavHostController) {
    Scaffold (
        topBar =  { CenterAlignedTopAppBar(title = {
            Text(text = "Weebo")
        },
            modifier = Modifier.background(MaterialTheme.colorScheme.primary))},
        content = {
            ImagesGrid(modifier = Modifier.padding(it), navController = navHostController)
        },
        bottomBar = { BottomNavigation().BottomNavigationBar(navController = navHostController)}
    )
}



@Serializable
object HOME

@Serializable
object SETTING

@Serializable
data class SEARCH(
   val tag: String? = null
)

@Serializable
data class ImageDetailScreen(
    val url: String,
    val copyright: String? = "",
    val character: String? = "",
    val tagString: String? = "",
    val artist: String? = "",
    val meta: String? = "",
    val fileType: String?,
)

