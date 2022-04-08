package com.mobile.travelapp.ui.home

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.travelapp.repo.database.entity.PlacesEntity

@Composable
fun CategoriesCard(
    category: PlacesEntity,
    navigateToCategoryList: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(end = 12.dp)
            .width(96.dp)
            .clickable(
                onClick = {
                    navigateToCategoryList(category.type)
                }
            ),
        shape = RoundedCornerShape(8.dp),
        elevation = 6.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)

        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(category.imageURL)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(80.dp)
                    .clip(MaterialTheme.shapes.small)
            )
            Text(
                text = category.type,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}
