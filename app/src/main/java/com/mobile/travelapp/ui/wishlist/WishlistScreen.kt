package com.mobile.travelapp.ui.wishlist

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobile.travelapp.repo.database.entity.PlacesEntity
import com.mobile.travelapp.ui.main.MainViewModel
import kotlinx.coroutines.launch

@Composable
@ExperimentalFoundationApi
@ExperimentalMaterialApi
fun WishlistScreen(
    mainViewModel: MainViewModel,
    navigateToDetailsPage: (String) -> Unit
) {
    WishlistScreen(
        mainViewModel = mainViewModel,
        onPlacesTapped = navigateToDetailsPage,
        onRemoveWishlist = mainViewModel::addOrRemovePlaceFromWishList
    )
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun WishlistScreen(
    mainViewModel: MainViewModel,
    onPlacesTapped: (String) -> Unit,
    onRemoveWishlist: (String) -> Unit
) {
    val wishListedPlaces by mainViewModel.wishListedPlaces.collectAsState()
    val listComparator = Comparator<PlacesEntity> { left, right ->
        left.id.compareTo(right.id)
    }
    val comparator by remember { mutableStateOf(listComparator) }

    WishListList(
        wishListedPlaces = wishListedPlaces,
        onPlacesTapped = onPlacesTapped,
        comparator = comparator,
        onRemoveWishlist = onRemoveWishlist
    )
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun WishListList(
    wishListedPlaces: List<PlacesEntity>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState(),
    onPlacesTapped: (String) -> Unit,
    comparator: Comparator<PlacesEntity>,
    onRemoveWishlist: (String) -> Unit
) {

    Column() {

        if (wishListedPlaces.isNotEmpty())
            LazyColumn(
                modifier = modifier,
                contentPadding = contentPadding,
                state = state
            ) {

                val sortedList = wishListedPlaces.sortedWith(comparator)

                items(sortedList, key = { it.id }) { item ->
                    val dismissState = rememberDismissState()

                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        onRemoveWishlist(item.id)
                    }
                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier
                            .padding(vertical = 1.dp)
                            .animateItemPlacement(),
                        directions = setOf(DismissDirection.EndToStart),
                        dismissThresholds = { direction ->
                            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                        },
                        background = {
                            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    DismissValue.Default -> Color.LightGray
                                    DismissValue.DismissedToEnd -> Color.Green
                                    DismissValue.DismissedToStart -> Color.Red
                                }
                            )
                            val alignment = when (direction) {
                                DismissDirection.StartToEnd -> Alignment.CenterStart
                                DismissDirection.EndToStart -> Alignment.CenterEnd
                            }
                            val icon = when (direction) {
                                DismissDirection.StartToEnd -> Icons.Default.Done
                                DismissDirection.EndToStart -> Icons.Default.Delete
                            }
                            val scale by animateFloatAsState(
                                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                            )
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = alignment
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = "Localized description",
                                    modifier = Modifier.scale(scale)
                                )
                            }
                        },
                        dismissContent = {
                            Card(
                                elevation = animateDpAsState(
                                    if (dismissState.dismissDirection != null) 4.dp else 0.dp
                                ).value
                            ) {
                                WishListCard(
                                    wishListedPlace = item,
                                    navigateToArticle = onPlacesTapped
                                )
                            }
                        }
                    )
                }
            }
        else
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Add places to wishlist to see here",
                    style = MaterialTheme.typography.h6,
                )
            }
    }
}

@Composable
fun showSnackBar(
    onUndoClicked: (Boolean) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = "Removed from Wishlist",
                actionLabel = "UNDO"
            )
            when (snackBarResult) {
                SnackbarResult.Dismissed -> {
                }
                SnackbarResult.ActionPerformed -> {
                    onUndoClicked(true)
                }
            }
        }
    }
}
