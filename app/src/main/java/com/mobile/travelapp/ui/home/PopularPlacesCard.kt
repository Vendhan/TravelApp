package com.mobile.travelapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.travelapp.repo.database.entity.PlacesEntity

@Composable
fun PopularPlacesCard(
    popularPlace: PlacesEntity,
    navigateToArticle: (String) -> Unit,
    onAddOrRemoveWishList: (String) -> Unit,
    isWishListed: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.size(280.dp, 220.dp)
    ) {
        Box(modifier = Modifier.clickable(onClick = { navigateToArticle(popularPlace.id) })) {
            Column {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(popularPlace.imageURL)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = popularPlace.name,
                        style = MaterialTheme.typography.h6,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = popularPlace.description,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body2,
                    )
                    Text(
                        text = popularPlace.city,
                        style = MaterialTheme.typography.body2,
                    )
                }
            }

            IconToggleButton(
                checked = isWishListed,
                onCheckedChange = {
                    onAddOrRemoveWishList(popularPlace.id)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                if (isWishListed)
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
    }
}
