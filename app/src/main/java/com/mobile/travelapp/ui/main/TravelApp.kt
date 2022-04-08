package com.mobile.travelapp.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mobile.travelapp.mainscreen.TravelNavigationBar
import com.mobile.travelapp.ui.theme.TravelAppTheme
import kotlinx.coroutines.FlowPreview
import kotlin.math.roundToInt

@FlowPreview
@ExperimentalComposeUiApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun TravelApp(mainViewModel: MainViewModel) {
    TravelAppTheme {

        val items = MainMenuItems.values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = MainMenuItems.fromRoute(backstackEntry.value?.destination?.route)

        val bottomBarHeight = 92.dp
        val bottomBarHeightPx = with(LocalDensity.current) {
            bottomBarHeight.roundToPx().toFloat()
        }
        val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val delta = available.y
                    val newOffset = bottomBarOffsetHeightPx.value + delta
                    bottomBarOffsetHeightPx.value = newOffset.coerceIn(-bottomBarHeightPx, 0f)
                    return Offset.Zero
                }
            }
        }
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            modifier = Modifier.nestedScroll(nestedScrollConnection),
            bottomBar = {
                TravelNavigationBar(
                    items = items.take(3),
                    modifier = Modifier
                        .height(bottomBarHeight)
                        .offset {
                            IntOffset(x = 0, y = -bottomBarOffsetHeightPx.value.roundToInt())
                        },
                    onTabSelected = { screen ->
                        bottomBarOffsetHeightPx.value = 0f
                        if (screen != currentScreen) {
                            navController.navigate(screen.name) {
                                // popUpTo(MainMenuItems.Home.name)
                                navController.popBackStack()
                            }
                        }
                    },
                    currentScreen = currentScreen,
                )
            },
            scaffoldState = scaffoldState,

        ) {
            TravelNavHost(mainViewModel, navController)
        }
    }
}
