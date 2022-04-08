package com.mobile.travelapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mobile.travelapp.repo.database.entity.PlacesEntity
import com.mobile.travelapp.ui.main.MainViewModel
import com.mobile.travelapp.ui.theme.DeepPurple700
import kotlinx.coroutines.flow.StateFlow

@ExperimentalPagerApi
@Composable
fun HomeScreenBody(
    mainViewModel: MainViewModel,
    navigateToCategoryList: (String) -> Unit,
    navigateToTrendingList: () -> Unit,
    navigateToPopularList: () -> Unit,
    navigateToDetailsPage: (String) -> Unit
) {
    HomeScreen(
        heroPlaces = mainViewModel.heroBannerPlaces,
        popularPlaces = mainViewModel.popularPlaces,
        categories = mainViewModel.placesCategory,
        trendingPlaces = mainViewModel.trendingPlaces,
        onPlacesTapped = navigateToDetailsPage,
        navigateToCategoryList = navigateToCategoryList,
        navigateToPopularList = navigateToPopularList,
        navigateToTrendingList = navigateToTrendingList,
        onAddOrRemoveWishList = {
            mainViewModel.addOrRemovePlaceFromWishList(it)
        },
        wishList = mainViewModel.wishListedPlaces
    )
}

@ExperimentalPagerApi
@Composable
fun HomeScreen(
    heroPlaces: StateFlow<List<PlacesEntity>>,
    popularPlaces: StateFlow<List<PlacesEntity>>,
    categories: StateFlow<List<PlacesEntity>>,
    trendingPlaces: StateFlow<List<PlacesEntity>>,
    onPlacesTapped: (String) -> Unit,
    navigateToCategoryList: (String) -> Unit,
    navigateToPopularList: () -> Unit,
    navigateToTrendingList: () -> Unit,
    onAddOrRemoveWishList: (String) -> Unit,
    wishList: StateFlow<List<PlacesEntity>>
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    val heroPlacesFlowLifecycleAware = remember(heroPlaces, lifecycleOwner) {
        heroPlaces.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val heroPlacesList by heroPlacesFlowLifecycleAware.collectAsState(emptyList())

    val popularPlacesFlowLifecycleAware = remember(popularPlaces, lifecycleOwner) {
        popularPlaces.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val popularPlacesList by popularPlacesFlowLifecycleAware.collectAsState(emptyList())

    val categoriesFlowLifecycleAware = remember(categories, lifecycleOwner) {
        categories.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val categoriesList by categoriesFlowLifecycleAware.collectAsState(emptyList())

    val trendingPlacesFlowLifecycleAware = remember(trendingPlaces, lifecycleOwner) {
        trendingPlaces.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val trendingPlacesList by trendingPlacesFlowLifecycleAware.collectAsState(emptyList())
    val wishList by wishList.collectAsState(emptyList())

    HomeList(
        heroPlaces = heroPlacesList,
        popularPlaces = popularPlacesList,
        categories = categoriesList,
        trendingPlaces = trendingPlacesList,
        onPlacesTapped = onPlacesTapped,
        navigateToCategoryList = navigateToCategoryList,
        navigateToPopularList = navigateToPopularList,
        navigateToTrendingList = navigateToTrendingList,
        onAddOrRemoveWishList = onAddOrRemoveWishList,
        wishList = wishList
    )
}

@ExperimentalPagerApi
@Composable
private fun HomeList(
    heroPlaces: List<PlacesEntity>,
    popularPlaces: List<PlacesEntity>,
    categories: List<PlacesEntity>,
    trendingPlaces: List<PlacesEntity>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState(),
    onPlacesTapped: (String) -> Unit,
    navigateToCategoryList: (String) -> Unit,
    navigateToPopularList: () -> Unit,
    navigateToTrendingList: () -> Unit,
    onAddOrRemoveWishList: (String) -> Unit,
    wishList: List<PlacesEntity>
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        state = state
    ) {
        if (heroPlaces.isNotEmpty()) {
            item {
                HeroBanner(heroPlaces = heroPlaces)
            }
        }
        if (popularPlaces.isNotEmpty()) {
            item {
                PopularPlacesList(
                    popularPlaces = popularPlaces,
                    navigateToDetailsPage = onPlacesTapped,
                    navigateToPopularList = navigateToPopularList,
                    onAddOrRemoveWishList = onAddOrRemoveWishList,
                    wishList = wishList
                )
            }
        }
        if (categories.isNotEmpty()) {
            item {
                CategoriesList(
                    categories = categories,
                    navigateToCategoryList = navigateToCategoryList
                )
            }
        }
        if (trendingPlaces.isNotEmpty()) {
            item {
                TrendingPlacesList(
                    trendingPlaces = trendingPlaces,
                    navigateToDetailsPage = onPlacesTapped,
                    navigateToTrendingList = navigateToTrendingList
                )
            }
        }
    }
}

@Composable
private fun PopularPlacesList(
    popularPlaces: List<PlacesEntity>,
    navigateToDetailsPage: (String) -> Unit,
    navigateToPopularList: () -> Unit,
    state: LazyListState = rememberLazyListState(),
    onAddOrRemoveWishList: (String) -> Unit,
    wishList: List<PlacesEntity>
) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Popular Places",
                style = MaterialTheme.typography.subtitle1,
            )
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navigateToPopularList()
                    },
                text = "See More",
                color = DeepPurple700,
                style = MaterialTheme.typography.subtitle1,
            )
        }
        LazyRow(
            modifier = Modifier.padding(end = 16.dp),
            state = state
        ) {
            items(popularPlaces) { popularPlace ->
                PopularPlacesCard(
                    popularPlace = popularPlace,
                    navigateToArticle = navigateToDetailsPage,
                    onAddOrRemoveWishList = onAddOrRemoveWishList,
                    isWishListed = getWishListStatus(wishList, popularPlace),
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
        }
    }
}

private fun getWishListStatus(wishList: List<PlacesEntity>, placesEntity: PlacesEntity): Boolean {
    return wishList.contains(placesEntity)
}

@Composable
private fun CategoriesList(
    categories: List<PlacesEntity>,
    navigateToCategoryList: (String) -> Unit,
) {
    Column {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Search By Category",
            style = MaterialTheme.typography.subtitle1,
        )
        LazyRow(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            state = rememberLazyListState()
        ) {
            items(categories) { category ->
                CategoriesCard(
                    category = category,
                    navigateToCategoryList = navigateToCategoryList
                )
            }
        }
    }
}

@Composable
private fun TrendingPlacesList(
    trendingPlaces: List<PlacesEntity>,
    navigateToDetailsPage: (String) -> Unit,
    navigateToTrendingList: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Trending Places",
                style = MaterialTheme.typography.subtitle1,
            )
            IconButton(onClick = {
                navigateToTrendingList()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint = DeepPurple700
                )
            }
        }
        trendingPlaces.forEach { trendingPlace ->
            TrendingPlacesCard(
                trendingPlaces = trendingPlace,
                navigateToArticle = navigateToDetailsPage,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // HomeScreenBody()
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    // SearchBody()
}

@Preview(showBackground = true)
@Composable
fun WishListScreenPreview() {
    // WishlistBody()
}
