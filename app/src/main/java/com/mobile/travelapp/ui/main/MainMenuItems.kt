package com.mobile.travelapp.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainMenuItems(
    val icon: ImageVector,
    val iconSelected: ImageVector
) {
    Home(
        icon = Icons.Outlined.Home,
        iconSelected = Icons.Filled.Home
    ),
    Search(
        icon = Icons.Outlined.Search,
        iconSelected = Icons.Filled.Search
    ),
    Wishlist(
        icon = Icons.Outlined.FavoriteBorder,
        iconSelected = Icons.Filled.Favorite
    ),
    List(
        icon = Icons.Outlined.FavoriteBorder,
        iconSelected = Icons.Filled.Favorite
    ),
    DetailPage(
        icon = Icons.Outlined.FavoriteBorder,
        iconSelected = Icons.Filled.Favorite
    );

    enum class Items {
        ListScreen()
    }

    companion object {
        fun fromRoute(route: String?): MainMenuItems =
            when (route?.substringBefore("/")) {
                Home.name -> Home
                Search.name -> Search
                Wishlist.name -> Wishlist
                "list?categoryName={categoryName},isPopular={isPopular},isTrending={isTrending}" -> List
                "detailsPage?placeID={placeID}" -> DetailPage
                null -> Home
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}
