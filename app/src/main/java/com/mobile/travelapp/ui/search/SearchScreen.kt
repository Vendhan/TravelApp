package com.mobile.travelapp.ui.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mobile.travelapp.repo.database.entity.PlacesEntity
import com.mobile.travelapp.ui.home.TrendingPlacesCard
import com.mobile.travelapp.ui.main.MainViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val URL = "https://assets4.lottiefiles.com/packages/lf20_wnqlfojb.json"

@FlowPreview
@ExperimentalComposeUiApi
@Composable
fun SearchScreen(
    mainViewModel: MainViewModel,
    navigateToDetailsPage: (String) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val places by mainViewModel.searchResults.collectAsState()
    SearchScreen(
        searchResults = places,
        onSearchInputChanged = { query ->
            coroutineScope.launch {
                query
                    .debounce(1000)
                    .distinctUntilChanged()
                    .collect {
                        mainViewModel.searchPlacesWithQuery(it)
                    }
            }
        },
        navigateToDetailsPage = navigateToDetailsPage,
        onSubmitSearch = mainViewModel::searchPlacesWithQuery
    )
}

@ExperimentalComposeUiApi
@Composable
fun SearchScreen(
    searchResults: List<PlacesEntity>,
    onSearchInputChanged: (Flow<String>) -> Unit,
    state: LazyListState = rememberLazyListState(),
    navigateToDetailsPage: (String) -> Unit,
    onSubmitSearch: (String) -> Unit
) {
    Column {
        var queryString by rememberSaveable {
            mutableStateOf("")
        }
        SearchBar(
            modifier = Modifier.padding(16.dp),
            onSearchInputChanged = onSearchInputChanged,
            onSubmitSearch = {
                queryString = it
            },
        )
        if (searchResults.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = state
            ) {
                if (searchResults.isNotEmpty()) {
                    item {
                        TrendingPlacesList(
                            places = searchResults,
                            navigateToDetailsPage = navigateToDetailsPage
                        )
                    }
                }
            }
        } else {
            if (queryString.isEmpty())
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Search results will appear here..",
                        style = MaterialTheme.typography.h6,
                    )
                }
            else
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp)
                ) {
                    Loader()
                    Text(
                        text = "No matching results found!",
                        style = MaterialTheme.typography.h6,
                    )
                }
        }
    }
}

@Composable
fun Loader() {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url(URL))
    LottieAnimation(composition)
}

@ExperimentalComposeUiApi
@Composable
fun TrendingPlacesList(
    places: List<PlacesEntity>,
    navigateToDetailsPage: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column {
        places.forEach { place ->
            TrendingPlacesCard(
                trendingPlaces = place,
                navigateToArticle = {
                    keyboardController?.hide()
                    navigateToDetailsPage(it)
                },
            )
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchInputChanged: (Flow<String>) -> Unit,
    onSubmitSearch: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface.copy(alpha = .6f)),
        elevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                IconButton(onClick = { /* Functionality not supported yet */ }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
                val focusManager = LocalFocusManager.current
                val keyboardController = LocalSoftwareKeyboardController.current

                val textState = rememberSaveable(
                    stateSaver = TextFieldValue.Saver
                ) {
                    mutableStateOf(TextFieldValue(""))
                }
                TextField(
                    value = textState.value,
                    onValueChange = {
                        textState.value = it
                        onSubmitSearch(it.text)
                        onSearchInputChanged(
                            flow {
                                emit(it.text)
                            }
                        )
                    },
                    placeholder = { Text("search") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ), // keyboardOptions change the newline key to a search key on the soft keyboard
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    // keyboardActions submits the search query when the search key is pressed
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSubmitSearch(textState.value.text)
                            keyboardController?.hide()
                            onSearchInputChanged(emptyFlow())
                        }
                    ),
                    modifier = Modifier
                        .interceptKey(Key.Enter) { // submit a search query when Enter is pressed
                            onSubmitSearch(textState.value.text)
                            onSearchInputChanged(emptyFlow())
                        }
                        .interceptKey(Key.Escape) { // dismiss focus when Escape is pressed
                            focusManager.clearFocus()
                        }
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.interceptKey(key: Key, onKeyEvent: () -> Unit): Modifier {
    return this.onPreviewKeyEvent {
        if (it.key == key && it.type == KeyEventType.KeyUp) { // fire onKeyEvent on KeyUp to prevent duplicates
            onKeyEvent()
            true
        } else it.key == key // only pass the key event to children if it's not the chosen key
    }
}
