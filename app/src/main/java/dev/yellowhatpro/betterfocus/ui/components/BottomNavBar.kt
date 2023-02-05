package dev.yellowhatpro.betterfocus.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.yellowhatpro.betterfocus.ui.navigation.NavigationItem.*

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val items = listOf(
        Dashboard,
        Focus,
        About
    )
    val backStackEntry by navController.currentBackStackEntryAsState()
    NavigationBar {
        items.forEach { item ->
            val selected = item.route == backStackEntry?.destination?.route
            NavigationBarItem(
                selected = selected,
                icon = { Icon(item.icon, contentDescription = null) },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(text = item.title) },
            )
        }
    }
}