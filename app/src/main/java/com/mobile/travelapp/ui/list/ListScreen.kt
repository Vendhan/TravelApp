package com.mobile.travelapp.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.mobile.travelapp.repo.database.entity.PlacesEntity
import com.mobile.travelapp.ui.home.TrendingPlacesCard
import com.mobile.travelapp.ui.main.MainViewModel
import com.mobile.travelapp.ui.theme.DeepPurple800
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ListScreen(
    mainViewModel: MainViewModel,
    categoryType: String,
    isTrending: Boolean,
    isPopular: Boolean,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    onBack: () -> Unit,
    navigateToDetailsPage: (String) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var title by rememberSaveable {
        mutableStateOf("")
    }
    val places: StateFlow<List<PlacesEntity>> = when {
        isPopular -> {
            title = "Popular Places"
            mainViewModel.popularPlaces
        }
        isTrending -> {
            title = "Trending Places"
            mainViewModel.trendingPlaces
        }
        else -> {
            mainViewModel.getPlacesWithCategory(categoryType = categoryType)
            title = categoryType
            mainViewModel.categoryPlaces
        }
    }
    val placesFlowLifecycleAware = remember(places, lifecycleOwner) {
        places.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val placeList by placesFlowLifecycleAware.collectAsState(
        initial = emptyList()
    )

    Row(modifier.fillMaxSize()) {
        val context = LocalContext.current
        ListScreenContent(
            places = placeList,
            title = title,
            navigationIconContent =
            {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            lazyListState = lazyListState,
            navigateToDetailsPage = navigateToDetailsPage
        )
    }
}

@Composable
private fun ListScreenContent(
    places: List<PlacesEntity>,
    title: String,
    navigationIconContent: @Composable (() -> Unit)? = null,
    lazyListState: LazyListState = rememberLazyListState(),
    navigateToDetailsPage: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.body1,
                            color = LocalContentColor.current,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1.5f)
                        )
                    }
                },
                navigationIcon = navigationIconContent,
                elevation = 4.dp,
                backgroundColor = DeepPurple800
            )
        },
    ) { innerPadding ->
        PlacesList(
            places = places,
            modifier = Modifier
                .padding(innerPadding),
            state = lazyListState,
            navigateToDetailsPage = navigateToDetailsPage
        )
    }
}

@Composable
fun PlacesList(
    places: List<PlacesEntity>,
    modifier: Modifier,
    state: LazyListState,
    navigateToDetailsPage: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        state = state
    ) {
        item {
            places.forEach { place ->
                TrendingPlacesCard(
                    trendingPlaces = place,
                    navigateToArticle = navigateToDetailsPage,
                )
            }
        }
    }
}
