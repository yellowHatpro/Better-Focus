package dev.yellowhatpro.betterfocus.features.dashboard

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.yellowhatpro.betterfocus.R
import dev.yellowhatpro.betterfocus.ui.components.AppCard

@Composable
fun DashboardScreen(modifier : Modifier = Modifier,
    usageStatsList: List<Pair<String, Long>>,
    packageManager: PackageManager) {
    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val context = LocalContext.current
        LazyVerticalGrid(columns = GridCells.Adaptive(170.dp)) {
            items(usageStatsList.filter{
                val name = try {
                    packageManager.getApplicationLabel(
                        packageManager.getApplicationInfo(
                            it.first,
                            0
                        )
                    ).toString()
                } catch (E: Exception){
                    "error"
                }
                name!="error"
            }) {
                val name = try {
                    packageManager.getApplicationLabel(
                        packageManager.getApplicationInfo(
                            it.first,
                            0
                        )
                    ).toString()
                } catch (E: Exception){
                    "error"
                }
                val icon = try{
                    packageManager.getApplicationIcon(
                        packageManager.getApplicationInfo(
                            it.first,
                            0
                        )
                    )
                } catch (e: Exception){
                    context.getDrawable(R.drawable.ic_better_focus_background)!!
                }
                AppCard(name = name  + it.second.toString(),
                    icon = icon,
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
                    })
            }
        }
    }
}