package dev.yellowhatpro.betterfocus.features.dashboard

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.yellowhatpro.betterfocus.R
import dev.yellowhatpro.betterfocus.ui.components.AppCard
import dev.yellowhatpro.betterfocus.ui.components.LottieAnim

@Composable
fun DashboardScreen(modifier : Modifier = Modifier,
    usageStatsList: List<Pair<String, String>>,
    packageManager: PackageManager) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current
        Column(
            modifier = Modifier.fillMaxWidth()
                .height(250.dp)
                .padding(20.dp)
        ) {
            LottieAnim(rawRes = R.raw.dashboard_apps_anim)
        }

        Text(text = "These are your frequently used apps", modifier = Modifier.fillMaxWidth() , textAlign = TextAlign.Center)
        val sortedApps = sortApps(usageStatsList)
        LazyVerticalGrid(columns = GridCells.Adaptive(170.dp)) {
            items(sortedApps.filter {
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
            }) {
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
                val icon = try {
                    packageManager.getApplicationIcon(
                        packageManager.getApplicationInfo(
                            it.first,
                            0
                        )
                    )
                } catch (e: Exception) {
                    context.getDrawable(R.drawable.ic_better_focus_background)!!
                }
                AppCard(name = name,
                    icon = icon,
                    time = it.second,
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                Toast.makeText(
                                    context,
                                    "Selected",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                )
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