package com.mobile.travelapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.travelapp.repo.TravelRepo
import com.mobile.travelapp.repo.database.entity.PlacesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val travelRepo: TravelRepo,
) : ViewModel() {

    val placesEntity = PlacesEntity(
        id = "0",
        name = "",
        description = "",
        imageURL = "",
        isTrending = false,
        isPopular = false,
        type = "",
        city = "",
        location = ""
    )

    private val _networkIssue = MutableStateFlow(false)
    val networkIssue: StateFlow<Boolean>
        get() = _networkIssue

    private val _heroBannerPlaces = MutableStateFlow<List<PlacesEntity>>(emptyList())
    val heroBannerPlaces: StateFlow<List<PlacesEntity>>
        get() = _heroBannerPlaces

    private val _popularPlaces = MutableStateFlow<List<PlacesEntity>>(emptyList())
    val popularPlaces: StateFlow<List<PlacesEntity>>
        get() = _popularPlaces

    private val _placesCategory = MutableStateFlow<List<PlacesEntity>>(emptyList())
    val placesCategory: StateFlow<List<PlacesEntity>>
        get() = _placesCategory

    private val _trendingPlaces = MutableStateFlow<List<PlacesEntity>>(emptyList())
    val trendingPlaces: StateFlow<List<PlacesEntity>>
        get() = _trendingPlaces

    private val _searchResults = MutableStateFlow<List<PlacesEntity>>(emptyList())
    val searchResults: StateFlow<List<PlacesEntity>>
        get() = _searchResults

    private val _wishListedPlaces = MutableStateFlow<List<PlacesEntity>>(emptyList())
    val wishListedPlaces: StateFlow<List<PlacesEntity>>
        get() = _wishListedPlaces

    private val _categoryPlaces = MutableStateFlow<List<PlacesEntity>>(emptyList())
    val categoryPlaces: StateFlow<List<PlacesEntity>>
        get() = _categoryPlaces

    private val _place = MutableStateFlow<PlacesEntity>(placesEntity)
    val place: StateFlow<PlacesEntity>
        get() = _place

    private val _complementPlaces = MutableStateFlow<List<PlacesEntity>>(emptyList())
    val complementPlaces: StateFlow<List<PlacesEntity>>
        get() = _complementPlaces

    init {
        fetchPlaces()
        getHeroBannerPlaces()
        getPopularPlaces()
        getCategories()
        getTrendingPlaces()
        getWishListedPlaces()
    }

    private fun fetchPlaces() {
        viewModelScope.launch {
            try {
                travelRepo.fetchPlacesFromService()
            } catch (ex: Throwable) {
            }
        }
    }

    private fun getHeroBannerPlaces() {
        viewModelScope.launch {
            travelRepo
                .getTrendingPlaces()
                .catch {
                }
                .collect {
                    if (it.isNotEmpty())
                        _heroBannerPlaces.value = it.slice(0..5)
                }
        }
    }

    private fun getPopularPlaces() {
        viewModelScope.launch {
            travelRepo.getPopularPlaces()
                .catch {
                }
                .collect {
                    _popularPlaces.value = it
                }
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            travelRepo.getCategories()
                .catch {
                }
                .collect {
                    _placesCategory.value = it
                }
        }
    }

    private fun getTrendingPlaces() {
        viewModelScope.launch {
            travelRepo
                .getTrendingPlaces()
                .catch {
                }
                .collect {
                    if (it.isNotEmpty())
                        _trendingPlaces.value = it.slice(0..5)
                }
        }
    }

    private fun getWishListedPlaces() {
        viewModelScope.launch {
            travelRepo
                .getWishListedPlaces()
                .catch {
                }
                .collect {
                    _wishListedPlaces.value = it
                }
        }
    }

    fun addOrRemovePlaceFromWishList(id: String) {
        viewModelScope.launch {
            travelRepo
                .addOrRemovePlaceFromWishlist(
                    id = id
                )
        }
    }

    fun searchPlacesWithQuery(query: String) {
        viewModelScope.launch {
            if (query.isBlank() || query.isEmpty())
                _searchResults.value = emptyList()
            else
                travelRepo
                    .searchPlacesWithQuery(query = query)
                    .catch {
                    }
                    .collect {
                        _searchResults.value = it
                    }
        }
    }

    fun getPlacesWithCategory(categoryType: String) =
        viewModelScope.launch {
            travelRepo
                .getPlacesWithCategory(category = categoryType)
                .catch {
                }
                .collect {
                    _categoryPlaces.value = it
                }
        }

    fun getPlaceDetailWithID(placeID: String) {
        viewModelScope.launch {
            travelRepo
                .getPlaceDetailWithID(id = placeID)
                .catch {
                }
                .collect {
                    _place.value = it
                }
        }
    }

    fun getComplementPlaces() {
        viewModelScope.launch {
            travelRepo
                .getComplementPlaces()
                .catch {
                }
                .collect {
                    _complementPlaces.value = it
                }
        }
    }
}
