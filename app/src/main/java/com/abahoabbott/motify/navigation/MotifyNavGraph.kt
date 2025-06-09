package com.abahoabbott.motify.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abahoabbott.motify.motivate.MotifyScreen
import com.abahoabbott.motify.motivate.MotifyViewModel
import com.abahoabbott.motify.navigation.MotifyScreen as NavMotifyScreen

@Composable
fun MotifyApp(quoteFromNotification: String?) {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        NavMotifyScreen.Home,
        NavMotifyScreen.Explore,
        NavMotifyScreen.Bookmarks,
        NavMotifyScreen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(painterResource(screen.icon), contentDescription = screen.label) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavMotifyScreen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavMotifyScreen.Home.route) {
                val viewModel: MotifyViewModel = hiltViewModel()
                LaunchedEffect(quoteFromNotification) {
                    quoteFromNotification?.let {
                        viewModel.overrideQuoteFromNotification(it)
                    }
                }
                MotifyScreen()
            }
            composable(NavMotifyScreen.Explore.route) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Text(text = "Explore Screen")
                }
            }
            composable(NavMotifyScreen.Bookmarks.route) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Text(text = "Bookmarks Screen")
                }
            }
            composable(NavMotifyScreen.Profile.route) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Text(text = "Profile Screen")
                }
            }
        }
    }
} 