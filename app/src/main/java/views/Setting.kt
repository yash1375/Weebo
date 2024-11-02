package views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventStart
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import navBar.BottomNavigation
import viewmodel.ImageViewmodel

@OptIn(ExperimentalMaterial3Api::class)
class Setting {
    @Composable
    fun setting(
        navController: NavController,
        imageViewmodel: ImageViewmodel,
        unblur: State<Boolean?>,
        nsfw: State<Boolean?>,
        gridNumber : State<Int?>,
        gridType : State<Boolean?>
    ) {
        var dropDown by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        Scaffold(
            topBar = { TopAppBar(title = { Text(text = "Setting") }) },
            content = {
                Column(Modifier.padding(it)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Unblur nsfw(does not blur eachi walls)",
                            Modifier.weight(4f),
                            fontSize = TextUnit.Unspecified
                        )
                        Spacer(Modifier.weight(1f))
                        Switch(checked = unblur.value!!, onCheckedChange = {
                            imageViewmodel.updateUnblurToggle(it)
                        }, Modifier.weight(1f))
                    }
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    )
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Hide NSFW",
                            Modifier.weight(4f),
                            fontSize = TextUnit.Unspecified
                        )
                        Spacer(Modifier.weight(1f))
                        Switch(checked = nsfw.value!!, onCheckedChange = {
                            imageViewmodel.updateNsfwToggle(it)
                        }, Modifier.weight(1f))
                    }
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    )
                    Row (
                        Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                    ){
                        Text("Column type",Modifier.fillMaxWidth(0.6f))
                        ExposedDropdownMenuBox(
                            expanded = dropDown,
                            onExpandedChange = {dropDown = !dropDown},
                        ) {
                            TextField(
                                value = if (gridType.value == false){
                                    "fixed"
                                }
                                else{
                                    "Additive"
                                },
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.menuAnchor(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDown) },
                            )
                            ExposedDropdownMenu(
                                expanded = dropDown,
                                onDismissRequest = {dropDown = !dropDown}
                            ) {
                                DropdownMenuItem(
                                    text = { Text("fixed") },
                                    onClick = {
                                        scope.launch {
                                            imageViewmodel.gridTypeChange(false)
                                        }
                                        dropDown = false}
                                )
                                DropdownMenuItem(
                                    text = { Text("Additive") },
                                    onClick = {
                                        scope.launch {
                                            imageViewmodel.gridTypeChange(true)
                                        }
                                        dropDown = false}
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(30.dp))
                    Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        if (!gridType.value!!) {
                            Slider(
                                value = gridNumber.value!!.toFloat(),
                                onValueChange = {
                                    scope.launch {
                                        imageViewmodel.gridNumberChange(it.toInt())
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(0.6f),
                                valueRange = 1f..4f,
                                steps = 4,
                            )
                        }
                        else{
                            Slider(
                                value = gridNumber.value!!.toFloat(),
                                onValueChange = {
                                    scope.launch {
                                        imageViewmodel.gridNumberChange(it.toInt())
                                    }
                                },
                                valueRange = 100f..600f,
                                steps = 30
                            )
                        }
                    }
                    Text(gridNumber.value.toString())
                }
            },
            bottomBar = { BottomNavigation().BottomNavigationBar(navController = navController) }
        )
    }
}