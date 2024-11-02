package navBar

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yash.weebo.SETTING


val listitem = listOf(
    BottomNavigationItem.HOME,
    BottomNavigationItem.FAV,
    BottomNavigationItem.SETTING
)

class BottomNavigation {
    @Composable
    fun BottomNavigationBar(navController: NavController) {
        NavigationBar {
            listitem.forEach { item ->
                NavigationBarItem(
                    selected = navController.currentDestination?.route.toString() == item.check,
                    onClick = { navController.navigate(item.route) },
                    icon = {Icon(imageVector = item.icon, contentDescription = item.label)})
            }
        }
    }
}