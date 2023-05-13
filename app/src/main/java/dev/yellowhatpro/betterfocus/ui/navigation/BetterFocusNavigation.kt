package dev.yellowhatpro.betterfocus.ui.navigation

import android.content.pm.PackageManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.SettingsApplications
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.yellowhatpro.betterfocus.features.tools.ToolScreen
import dev.yellowhatpro.betterfocus.features.dashboard.DashboardScreen
import dev.yellowhatpro.betterfocus.features.focus.FocusScreen

@Composable
fun BetterFocusNavigation(
    modifier : Modifier = Modifier,
    navHostController: NavHostController,
    usageStatsList: List<Pair<String, String>>,
    packageManager: PackageManager
) {
    NavHost(navController = navHostController, startDestination = NavigationItem.Dashboard.route) {
        composable(route = NavigationItem.Dashboard.route) {
            DashboardScreen(modifier = modifier, usageStatsList, packageManager)
        }
        composable(route = NavigationItem.Focus.route) {
            FocusScreen(modifier = modifier, packageManager)
        }
        composable(route = NavigationItem.Tools.route) {
            ToolScreen(modifier = modifier, packageManager)
        }
    }
}

sealed class NavigationItem(var route: String,
     var icon: ImageVector,
     var title : String) {
    object Dashboard : NavigationItem("dashboard", Icons.Rounded.Dashboard, "Dashboard")
    object Focus : NavigationItem("focus", Icons.Rounded.Timelapse, "Focus")
    object Tools : NavigationItem("tools", Icons.Rounded.SettingsApplications, "Tools")
}