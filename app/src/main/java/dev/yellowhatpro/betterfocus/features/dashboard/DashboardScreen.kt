package dev.yellowhatpro.betterfocus.features.dashboard

import android.content.pm.PackageManager
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import dev.yellowhatpro.betterfocus.R
import dev.yellowhatpro.betterfocus.ui.components.AppCard
import dev.yellowhatpro.betterfocus.ui.components.LottieAnim

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(modifier : Modifier = Modifier,
    usageStatsList: List<Pair<String, String>>,
    packageManager: PackageManager) {
    var shouldShowPopUp by remember {
        mutableStateOf(-1 )
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(20.dp)
        ) {
            LottieAnim(rawRes = R.raw.dashboard_apps_anim)
        }

        Text(
            text = "These are your frequently used apps",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        val sortedApps = sortApps(usageStatsList).filter {
            val name = try {
                packageManager.getApplicationLabel(
                    packageManager.getApplicationInfo(
                        it.first,
                        0
                    )
                ).toString()
            } catch (E: Exception) {
                "error"
            }
            name != "error"
        }
        LazyVerticalGrid(columns = GridCells.Adaptive(170.dp)) {
            items(sortedApps) { app->
                val name = try {
                    packageManager.getApplicationLabel(
                        packageManager.getApplicationInfo(
                            app.first,
                            0
                        )
                    ).toString()
                } catch (E: Exception) {
                    "error"
                }
                val icon = try {
                    packageManager.getApplicationIcon(
                        packageManager.getApplicationInfo(
                            app.first,
                            0
                        )
                    )
                } catch (e: Exception) {
                    context.getDrawable(R.drawable.ic_better_focus_background)!!
                }
                AppCard(name = name,
                    icon = icon,
                    time = app.second,
                    modifier = Modifier
                        .width(160.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(width = if (shouldShowPopUp !=-1 && sortedApps.indexOf(app)==shouldShowPopUp) 2.dp else 0.dp,  if (shouldShowPopUp !=-1 && sortedApps.indexOf(app)==shouldShowPopUp) MaterialTheme.colorScheme.onPrimaryContainer else Color.Unspecified, RoundedCornerShape(20.dp))
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    shouldShowPopUp = sortedApps.indexOf(app)
                                }
                            )
                        }
                )
                if (shouldShowPopUp !=-1) {
                    Popup(
                        onDismissRequest = {
                            shouldShowPopUp = -1
                        }, alignment = Alignment.CenterStart,
                        offset = IntOffset(0, 700)
                    ) {
                        Box {
                            Column {
                                ListItem(
                                    headlineText = { Text("Set the time limit") },
                                    supportingText = { Text("Secondary text") },
                                    trailingContent = { Text("meta") },
                                    leadingContent = {
                                        Icon(
                                            Icons.Filled.Favorite,
                                            contentDescription = "Localized description",
                                        )
                                    }
                                )
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}

fun sortApps(apps: List<Pair<String,String>>) : List<Pair<String, String>> {
    val pattern = "(\\d+)".toRegex()
    val sorted = apps.sortedWith(compareBy {
        val hours = pattern.find(it.second)?.value?.toInt() ?: 0
        val min = pattern.findAll(it.second).lastOrNull()?.value?.toInt() ?: 0
        hours * 60 + min
    }).reversed()
    return sorted
}