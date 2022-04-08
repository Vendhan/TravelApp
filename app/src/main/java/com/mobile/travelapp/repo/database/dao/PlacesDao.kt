package com.mobile.travelapp.repo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.travelapp.repo.database.entity.PlacesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPlaces(places: List<PlacesEntity>)

    @Query("SELECT * FROM PlacesEntity")
    fun getAllPlaces(): Flow<List<PlacesEntity>>

    @Query("SELECT * FROM PlacesEntity WHERE isPopular = 1")
    fun getPopularPlaces(): Flow<List<PlacesEntity>>

    @Query("SELECT * FROM PlacesEntity GROUP BY type")
    fun getCategories(): Flow<List<PlacesEntity>>

    @Query("SELECT * FROM PlacesEntity as Places, WishListEntity as Wishlist WHERE Places.id == WishList.id ")
    fun getWishListedPlaces(): Flow<List<PlacesEntity>>

    @Query("SELECT * FROM PlacesEntity WHERE isTrending = 1")
    fun getTrendingPlaces(): Flow<List<PlacesEntity>>

    @Query("SELECT * FROM PlacesEntity WHERE name LIKE '%' || :query || '%'")
    fun getPlacesMatchingSearchQuery(query: String): Flow<List<PlacesEntity>>

    @Query("SELECT * FROM PlacesEntity WHERE type = :category")
    fun getPlacesWithCategory(category: String): Flow<List<PlacesEntity>>

    @Query("SELECT * FROM PlacesEntity WHERE id = :id")
    fun getPlaceDetailWithID(id: String): Flow<PlacesEntity>
}
