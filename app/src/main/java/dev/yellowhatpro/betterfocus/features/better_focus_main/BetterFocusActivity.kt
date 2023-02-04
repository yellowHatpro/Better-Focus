package dev.yellowhatpro.betterfocus.features.better_focus_main

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.yellowhatpro.betterfocus.ui.components.AppCard
import dev.yellowhatpro.betterfocus.ui.theme.BetterFocusTheme


class BetterFocusActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val runningAppProcessInfo = packageManager.getInstalledPackages(0).filter {
            !(isSystemApp(it.packageName))
        }
        setContent {
            BetterFocusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val isSelected = rememberSaveable {
                        mutableListOf<String>()
                    }
                    LazyVerticalGrid(columns = GridCells.Adaptive(170.dp) ){
                        items(runningAppProcessInfo){
                            AppCard(name = it.applicationInfo.loadLabel(packageManager).toString() ,
                                icon = it.applicationInfo.loadIcon(packageManager),
                                modifier = Modifier.pointerInput(Unit){
                                    detectTapGestures(
                                        onLongPress = {
                                            Toast.makeText(
                                                this@BetterFocusActivity,
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
        }
    }

    @SuppressLint("PackageManagerGetSignatures")
    fun isSystemApp(packageName: String): Boolean {
        return try {
            // Get packageinfo for target application
            val targetPkgInfo: PackageInfo = packageManager.getPackageInfo(
                packageName, PackageManager.GET_SIGNATURES
            )
            // Get packageinfo for system package
            val sys: PackageInfo = packageManager.getPackageInfo(
                "android", PackageManager.GET_SIGNATURES
            )
            // Match both packageinfo for there signatures
            targetPkgInfo.signatures != null && (sys.signatures[0]
                    == targetPkgInfo.signatures[0])
        } catch (e: NameNotFoundException) {
            false
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BetterFocusTheme {
        Greeting("Android")
    }
}