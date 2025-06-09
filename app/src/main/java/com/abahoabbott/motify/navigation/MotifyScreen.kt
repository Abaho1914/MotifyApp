package com.abahoabbott.motify.navigation

import com.abahoabbott.motify.R

sealed class MotifyScreen(val route: String, val icon: Int, val label: String) {
    object Home : MotifyScreen("home", R.drawable.home_outlined_24px, "Home")
    object Explore : MotifyScreen("explore", R.drawable.search_filled_24px, "Explore")
    object Bookmarks : MotifyScreen("bookmarks", R.drawable.bookmarks_outlined_24px, "Bookmarks")
    object Profile : MotifyScreen("profile", R.drawable.person_filled_24px, "Profile")
} 