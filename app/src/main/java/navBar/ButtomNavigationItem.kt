package navBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.yash.weebo.HOME
import com.yash.weebo.SETTING


sealed class BottomNavigationItem(val route: Any, val icon: ImageVector, val label: String,val check:String){
    data object HOME : BottomNavigationItem(com.yash.weebo.HOME, Icons.Default.Home,"Home","com.yash.weebo.HOME")
    data object FAV : BottomNavigationItem(com.yash.weebo.FAV(null),Icons.Default.Favorite,"favorite","com.yash.weebo.FAV")
    data object SETTING : BottomNavigationItem(com.yash.weebo.SETTING, Icons.Default.Settings,"setting","com.yash.weebo.SETTING")
}