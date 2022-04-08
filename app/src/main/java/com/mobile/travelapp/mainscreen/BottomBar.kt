package com.mobile.travelapp.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobile.travelapp.ui.main.MainMenuItems
import com.mobile.travelapp.ui.theme.DeepPurple900

@Composable
fun TravelNavigationBar(
    modifier: Modifier,
    items: List<MainMenuItems>,
    onTabSelected: (MainMenuItems) -> Unit,
    currentScreen: MainMenuItems
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
        NavigationBar(
            modifier = modifier
                .padding(
                    start = 24.dp,
                    bottom = 16.dp,
                    end = 24.dp
                )
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomEnd = 16.dp,
                        bottomStart = 16.dp
                    )
                ),
            tonalElevation = 4.dp,
            containerColor = DeepPurple900
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        if (item == currentScreen)
                            Icon(
                                imageVector = item.iconSelected,
                                contentDescription = null,
                            )
                        else
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = Color.White
                            )
                    },
                    label = {
                        Text(
                            text = item.name,
                            color = Color.White
                        )
                    },
                    selected = currentScreen == item,
                    onClick = {
                        onTabSelected(item)
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    TravelNavigationBar(
        modifier = Modifier,
        items = MainMenuItems.values().toList(),
        onTabSelected = {},
        currentScreen = MainMenuItems.Home
    )
}
