package views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import navBar.BottomNavigation
import org.koin.androidx.compose.koinViewModel
import viewmodel.ImageViewmodel

@OptIn(ExperimentalMaterial3Api::class)
class Setting {
    @Composable
    fun setting(navController: NavController) {
        val imageViewmodel : ImageViewmodel = koinViewModel()
        val check by imageViewmodel.nsfw.collectAsStateWithLifecycle(initialValue = false)
        val scope = rememberCoroutineScope()
        Scaffold(
            topBar = { TopAppBar(title = { Text(text = "Setting") }) },
            content = {
                Column(Modifier.padding(it)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Enable nsfw",
                            Modifier.weight(2f),
                            fontSize = TextUnit.Unspecified
                        )
                        Spacer(Modifier.weight(1f))
                        Switch(checked = check!!, onCheckedChange = {
                            imageViewmodel.updateNsfwToggle(it)
                        }, Modifier.weight(1f))
                    }
                }
            },
            bottomBar = { BottomNavigation().BottomNavigationBar(navController = navController) }
        )
    }

}