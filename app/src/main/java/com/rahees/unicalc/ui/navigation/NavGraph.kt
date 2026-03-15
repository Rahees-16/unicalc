package com.rahees.unicalc.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rahees.unicalc.ui.screens.calculator.CalculatorScreen
import com.rahees.unicalc.ui.screens.converter.ConverterScreen
import com.rahees.unicalc.ui.screens.currency.CurrencyScreen
import com.rahees.unicalc.ui.screens.history.HistoryScreen
import com.rahees.unicalc.ui.screens.home.HomeScreen
import com.rahees.unicalc.ui.screens.profession.ProfessionScreen
import com.rahees.unicalc.ui.screens.settings.SettingsScreen

sealed class Route(val route: String) {
    data object Home : Route("home")
    data object Converter : Route("converter/{category}") {
        fun createRoute(category: String) = "converter/$category"
    }
    data object Calculator : Route("calculator")
    data object Currency : Route("currency")
    data object Profession : Route("profession")
    data object History : Route("history")
    data object Settings : Route("settings")
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("Convert", Icons.Default.SwapHoriz, Route.Home.route),
    BottomNavItem("Calculator", Icons.Default.Calculate, Route.Calculator.route),
    BottomNavItem("Currency", Icons.Default.CurrencyExchange, Route.Currency.route),
    BottomNavItem("Settings", Icons.Default.Settings, Route.Settings.route)
)

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.Home.route) {
                HomeScreen(
                    onCategoryClick = { category ->
                        navController.navigate(Route.Converter.createRoute(category.name))
                    },
                    onHistoryClick = {
                        navController.navigate(Route.History.route)
                    },
                    onProfessionClick = {
                        navController.navigate(Route.Profession.route)
                    }
                )
            }

            composable(
                route = Route.Converter.route,
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("category") ?: ""
                ConverterScreen(
                    categoryName = categoryName,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Route.Calculator.route) {
                CalculatorScreen()
            }

            composable(Route.Currency.route) {
                CurrencyScreen()
            }

            composable(Route.Profession.route) {
                ProfessionScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Route.History.route) {
                HistoryScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Route.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
