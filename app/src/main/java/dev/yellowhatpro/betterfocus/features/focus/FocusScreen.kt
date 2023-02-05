package dev.yellowhatpro.betterfocus.features.focus

import android.content.pm.PackageManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import dev.yellowhatpro.betterfocus.features.BetterFocusViewModel
import dev.yellowhatpro.betterfocus.utils.SharedPrefManager

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun FocusScreen(modifier : Modifier = Modifier, packageManager: PackageManager, viewModel: BetterFocusViewModel = hiltViewModel()
) {
    viewModel.provideFocusApps()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    Column {
        val focusApps = SharedPrefManager.focusList ?: listOf()
        LazyColumn(state = lazyListState) {
            items(focusApps) { app ->
                val currentItem by rememberUpdatedState(app)
                val dismissState = rememberDismissState()
                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                    viewModel.removeAppFromFocusList(app.packageName)
                }
                SwipeToDismiss(
                    state = dismissState, background = {
                        MaterialTheme.colorScheme.onSurface
                    },
                    directions = setOf(DismissDirection.EndToStart),
                    modifier = Modifier.animateItemPlacement()
                ) {
                    Column {
                        ListItem(
                            headlineText = {
                                Text(
                                    text = packageManager.getApplicationLabel(
                                        packageManager.getApplicationInfo(
                                            currentItem.packageName,
                                            0
                                        )
                                    ).toString()
                                )
                            },
                            supportingText = {
                                Text(text = "Time Limit: ${app.hours} Hours ${app.minutes} minutes")
                            },
                            leadingContent = {
                                Image(
                                    painter = rememberDrawablePainter(
                                        drawable = packageManager.getApplicationIcon(
                                            packageManager.getApplicationInfo(
                                                currentItem.packageName,
                                                0
                                            )
                                        )
                                    ), contentDescription = ""
                                )
                            },
                            trailingContent = {

                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}