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
            items(usageStatsList) {
                AppCard(name = packageManager.getApplicationLabel(
                    packageManager.getApplicationInfo(
                        it.first,
                        0
                    )
                )
                    .toString() + it.second.toString(),
                    icon = packageManager.getApplicationIcon(
                        packageManager.getApplicationInfo(
                            it.first,
                            0
                        )
                    ),
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