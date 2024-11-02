package views

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import com.yash.weebo.R
import data.FavEntity
import data.ImageEntity
import data.toImageEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import navBar.BottomNavigation
import viewmodel.ImageViewmodel

class FavImage {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun Fav(modifier: Modifier = Modifier, navHostController: NavHostController, imageViewmodel: ImageViewmodel, unblur: State<Boolean?>, nsfw: State<Boolean?>,
            context: Context,
            gridNumber: State<Int?>,
            gridType: State<Boolean?>
    ) {
        val imageraw = imageViewmodel.Favdata().map {
            it.map {
                FavEntity.toImageEntity(it)
            }
        }.collectAsLazyPagingItems()
        imageViewmodel.resetLazy()
        Scaffold(
            content = {
                ImagesGrid(
                    modifier = Modifier.padding(it),
                    navController = navHostController,
                    unblur = unblur,
                    nsfw = nsfw,
                    context = context,
                    image = imageraw,
                    gridNumber = gridNumber,
                    gridType = gridType
                )
            },
            bottomBar = { BottomNavigation().BottomNavigationBar(navController = navHostController) },
        )
    }
    }