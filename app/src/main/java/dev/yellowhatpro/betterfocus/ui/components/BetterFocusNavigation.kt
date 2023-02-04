package dev.yellowhatpro.betterfocus.ui.components

import android.content.pm.PackageManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.DoNotDisturb
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.yellowhatpro.betterfocus.features.dashboard.DashboardScreen
import dev.yellowhatpro.betterfocus.features.focus.FocusScreen
import dev.yellowhatpro.betterfocus.features.setting.SettingScreen

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
            FocusScreen()
        }
        composable(route = NavigationItem.Setting.route) {
            SettingScreen()
        }
    }
}

sealed class NavigationItem( var route: String,
    var icon: ImageVector,
    var title : String) {
    object Dashboard : NavigationItem("dashboard", Icons.Rounded.Dashboard, "Dashboard")
    object Focus : NavigationItem("focus", Icons.Rounded.DoNotDisturb, "Focus")
    object Setting : NavigationItem("setting", Icons.Rounded.Settings, "Setting")
}