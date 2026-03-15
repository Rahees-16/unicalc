package com.rahees.unicalc.ui.navigation

import android.content.Context
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
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
import com.rahees.unicalc.ui.screens.onboarding.OnboardingScreen
import com.rahees.unicalc.ui.screens.profession.ProfessionScreen
import com.rahees.unicalc.ui.screens.settings.SettingsScreen
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding")

sealed class Route(val route: String) {
    data object Onboarding : Route("onboarding")
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val onboardingCompleted by context.onboardingDataStore.data.map { prefs ->
        prefs[booleanPreferencesKey("onboarding_completed")] ?: false
    }.collectAsState(initial = true)

    val showBottomBar = currentDestination?.route != Route.Onboarding.route &&
        currentDestination?.route in bottomNavItems.map { it.route }

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
            startDestination = if (onboardingCompleted) Route.Home.route else Route.Onboarding.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.Onboarding.route) {
                OnboardingScreen(
                    onComplete = {
                        scope.launch {
                            context.onboardingDataStore.edit { prefs ->
                                prefs[booleanPreferencesKey("onboarding_completed")] = true
                            }
                        }
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

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
