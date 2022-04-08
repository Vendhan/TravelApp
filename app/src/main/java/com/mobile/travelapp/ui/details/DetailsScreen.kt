package com.mobile.travelapp.ui.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.travelapp.repo.database.entity.PlacesEntity
import com.mobile.travelapp.ui.home.TrendingPlacesCard
import com.mobile.travelapp.ui.main.MainViewModel

@Composable
fun DetailsScreen(
    mainViewModel: MainViewModel,
    placeID: String,
    onClickBack: () -> Unit
) {
    mainViewModel.getPlaceDetailWithID(placeID = placeID)
    mainViewModel.getComplementPlaces()
    val lifecycleOwner = LocalLifecycleOwner.current
    val place = mainViewModel.place
    val placeFlowLifecycleAware = remember(place, lifecycleOwner) {
        place.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val complementPlaces = mainViewModel.complementPlaces
    val complementPlacesFlowLifecycleAware = remember(complementPlaces, lifecycleOwner) {
        complementPlaces.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val complementPlacesList by complementPlacesFlowLifecycleAware.collectAsState(emptyList())

    val placeEntity by placeFlowLifecycleAware.collectAsState(mainViewModel.placesEntity)
    val wishListPlaces by mainViewModel.wishListedPlaces.collectAsState()
    DetailScreen(
        wishListPlaces = wishListPlaces,
        place = placeEntity,
        complementPlaces = complementPlacesList,
        onClickBack = onClickBack,
        onAddOrRemoveWishlist = {
            mainViewModel.addOrRemovePlaceFromWishList(it)
        },
        onClickLocation = {},
        navigateToDetailsPage = {}
    )
}

@Composable
fun DetailScreen(
    wishListPlaces: List<PlacesEntity>,
    place: PlacesEntity,
    complementPlaces: List<PlacesEntity>,
    onClickBack: () -> Unit,
    onAddOrRemoveWishlist: (String) -> Unit,
    onClickLocation: (String) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
    navigateToDetailsPage: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .padding(16.dp),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onAddOrRemoveWishlist(place.id)
            }) {
                if (getWishListStatus(wishList = wishListPlaces, placesEntity = place))
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = Color.White
                    )
                else
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.White
                    )
            }
        }
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp

            Card(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp)
            ) {
                Box {
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopStart),
                        onClick = {
                            onClickBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd),
                        onClick = {
                            onClickLocation(place.location)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(place.imageURL)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenWidth - 48.dp)
                    )
                }
            }
            Text(
                text = place.name,
                style = MaterialTheme.typography.h6,
                maxLines = 2,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 16.dp)
            )
            Text(
                text = "About",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 16.dp)
            )
            Text(
                text = place.description,
                style = MaterialTheme.typography.body2,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            ComplementPlaces(
                places = complementPlaces,
                state = lazyListState,
                navigateToDetailsPage = navigateToDetailsPage
            )
        }
    }
}

private fun getWishListStatus(wishList: List<PlacesEntity>, placesEntity: PlacesEntity): Boolean {
    return wishList.contains(placesEntity)
}

@Composable
fun ComplementPlaces(
    places: List<PlacesEntity>,
    modifier: Modifier = Modifier,
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
