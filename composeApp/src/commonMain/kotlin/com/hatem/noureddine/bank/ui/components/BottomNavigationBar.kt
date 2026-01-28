package com.hatem.noureddine.bank.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hatem.noureddine.bank.ui.navigation.Route

/**
 * Main Bottom Navigation Bar for the application.
 * Handles navigation between Accounts and Settings screens.
 *
 * @param navController The [NavController] to handle navigation actions.
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 16.dp, // Higher elevation for floating effect
        modifier =
            Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .clip(RoundedCornerShape(32.dp)), // Floating pill shape
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        NavigationBarItem(
            icon = { Icon(Icons.Rounded.AccountBalance, contentDescription = "Accounts") },
            label = { Text("Accounts") },
            selected =
                currentDestination?.hierarchy?.any {
                    it.route?.contains("Accounts") == true || it.route?.contains("Operations") == true
                } == true,
            onClick = {
                navController.navigate(Route.Accounts) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
        )

        NavigationBarItem(
            icon = { Icon(Icons.Rounded.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected =
                currentDestination?.hierarchy?.any {
                    it.route?.contains("Settings") == true
                } == true,
            onClick = {
                navController.navigate(Route.Settings) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
        )
    }
}
