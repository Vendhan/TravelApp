package com.mobile.travelapp.repo

import com.mobile.travelapp.repo.database.entity.PlacesEntity
import kotlinx.coroutines.flow.Flow

interface TravelRepo {

    suspend fun fetchPlacesFromService()

    suspend fun getPopularPlaces(): Flow<List<PlacesEntity>>

    suspend fun getCategories(): Flow<List<PlacesEntity>>

    suspend fun getTrendingPlaces(): Flow<List<PlacesEntity>>

    suspend fun searchPlacesWithQuery(query: String): Flow<List<PlacesEntity>>

    suspend fun getWishListedPlaces(): Flow<List<PlacesEntity>>

    suspend fun addOrRemovePlaceFromWishlist(id: String)

    suspend fun getPlacesWithCategory(category: String): Flow<List<PlacesEntity>>

    suspend fun getPlaceDetailWithID(id: String): Flow<PlacesEntity>

    suspend fun getComplementPlaces(): Flow<List<PlacesEntity>>
}
