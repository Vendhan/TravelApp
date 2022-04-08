package com.mobile.travelapp.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mobile.travelapp.ui.details.DetailsScreen
import com.mobile.travelapp.ui.home.HomeScreenBody
import com.mobile.travelapp.ui.list.ListScreen
import com.mobile.travelapp.ui.search.SearchScreen
import com.mobile.travelapp.ui.wishlist.WishlistScreen
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalComposeUiApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun TravelNavHost(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = MainMenuItems.Home.name,
    ) {
        composable(MainMenuItems.Home.name) {
            HomeScreenBody(
                mainViewModel = mainViewModel,
                navigateToPopularList = {
                    navController.navigate(
                        route = "list?categoryName= ,isPopular=true,isTrending=false"
                    )
                },
                navigateToTrendingList = {
                    navController.navigate(
                        route = "list?categoryName= ,isPopular=false,isTrending=true"
                    )
                },
                navigateToCategoryList = { categoryName ->
                    navController.navigate(
                        route = "list?categoryName=$categoryName,isPopular=false,isTrending=false"
                    )
                },
                navigateToDetailsPage = { placeID ->
                    navController.navigate(
                        route = "detailsPage?placeID=$placeID"
                    )
                }
            )
        }
        composable(MainMenuItems.Search.name) {
            SearchScreen(
                mainViewModel = mainViewModel,
                navigateToDetailsPage = { placeID ->
                    navController.navigate(
                        route = "detailsPage?placeID=$placeID"
                    )
                }
            )
        }
        composable(MainMenuItems.Wishlist.name) {
            WishlistScreen(
                mainViewModel = mainViewModel,
                navigateToDetailsPage = { placeID ->
                    navController.navigate(
                        route = "detailsPage?placeID=$placeID"
                    )
                }
            )
        }
        composable(
            route = "list?categoryName={categoryName},isPopular={isPopular},isTrending={isTrending}",
            arguments = listOf(
                navArgument("categoryName") {
                    type = NavType.StringType
                },
                navArgument("isTrending") {
                    type = NavType.BoolType
                },
                navArgument("isPopular") {
                    type = NavType.BoolType
                }
            )
        ) {
            val categoryName = it.arguments?.getString("categoryName") ?: ""
            val isTrending = it.arguments?.getBoolean("isTrending") ?: false
            val isPopular = it.arguments?.getBoolean("isPopular") ?: false
            ListScreen(
                mainViewModel = mainViewModel,
                categoryType = categoryName,
                isTrending = isTrending,
                isPopular = isPopular,
                onBack = {
                    navController.popBackStack()
                },
                navigateToDetailsPage = { placeID ->
                    navController.navigate(
                        route = "detailsPage?placeID=$placeID"
                    )
                }
            )
        }
        composable(
            route = "detailsPage?placeID={placeID}",
            arguments = listOf(
                navArgument(
                    name = "placeID"
                ) {
                    type = NavType.StringType
                }
            )
        ) {
            val placeID = it.arguments?.getString("placeID") ?: ""
            DetailsScreen(
                mainViewModel = mainViewModel,
                placeID = placeID,
                onClickBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
