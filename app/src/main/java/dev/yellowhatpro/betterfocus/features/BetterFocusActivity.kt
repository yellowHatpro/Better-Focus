package dev.yellowhatpro.betterfocus.features

import android.annotation.SuppressLint
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.yellowhatpro.betterfocus.ui.components.BottomNavBar
import dev.yellowhatpro.betterfocus.ui.navigation.BetterFocusNavigation
import dev.yellowhatpro.betterfocus.ui.theme.BetterFocusTheme
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class BetterFocusActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val usageStatsManager =
            this.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - TimeUnit.DAYS.toMillis(1)
        val usageStatsList =
            usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
                .map {
                    val hourmin = String.format(
                        "%02d hrs %02d min",
                        TimeUnit.MILLISECONDS.toHours(it.totalTimeInForeground),
                        TimeUnit.MILLISECONDS.toMinutes(it.totalTimeInForeground) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(
                                it.totalTimeInForeground
                            )
                        )
                    )
                    (it.packageName to hourmin)
                }.filter {
                    !(isSystemApp(it.first)) && !it.first.contains(
                        "com.android",
                        ignoreCase = true
                    ) && !(it.second.contains("00 hrs 00 min"))
                }
                .reversed()
                .distinctBy { it.first }
        setContent {
            val navController = rememberNavController()
            BetterFocusTheme {
                Scaffold(modifier = Modifier,
                    bottomBar = { BottomNavBar(navController = navController) },
                    content = { paddingValues ->
                        BetterFocusNavigation(
                            modifier = Modifier.padding(paddingValues),
                            navHostController = navController,
                            usageStatsList = usageStatsList,
                            packageManager = packageManager
                        )
                    })
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