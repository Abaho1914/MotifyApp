package com.abahoabbott.motify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abahoabbott.motify.motivate.MotifyScreen
import com.abahoabbott.motify.motivate.MotifyViewModel
import com.abahoabbott.motify.ui.theme.MotifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val quoteFromNotification = intent?.getStringExtra("QUOTE_FROM_NOTIFICATION")

        enableEdgeToEdge()
        setContent {
            MotifyTheme {
                MotifyApp(quoteFromNotification = quoteFromNotification)
            }
        }
    }
}

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
)

@Composable
fun MotifyApp(quoteFromNotification: String?) {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem("Home", "home", Icons.Default.Home),
        BottomNavItem("Explore", "explore", Icons.Default.Search),
        BottomNavItem("Bookmarks", "bookmarks", Icons.Default.FavoriteBorder),
        BottomNavItem("Profile", "profile", Icons.Default.AccountCircle)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.name) },
                        label = { Text(item.name) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                val viewModel: MotifyViewModel = hiltViewModel()
                LaunchedEffect(quoteFromNotification) {
                    quoteFromNotification?.let {
                        viewModel.overrideQuoteFromNotification(it)
                    }
                }
                MotifyScreen()
            }
            composable("explore") {
                // Placeholder for Explore Screen
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Text(text = "Explore Screen")
                }
            }
            composable("bookmarks") {
                // Placeholder for Bookmarks Screen
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Text(text = "Bookmarks Screen")
                }
            }
            composable("profile") {
                // Placeholder for Profile Screen
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Text(text = "Profile Screen")
                }
            }
        }
    }
}

