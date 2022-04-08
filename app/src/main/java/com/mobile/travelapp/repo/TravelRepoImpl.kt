package com.mobile.travelapp.repo

import com.mobile.travelapp.di.CoroutinesDispatchersModule
import com.mobile.travelapp.repo.database.dao.PlacesDao
import com.mobile.travelapp.repo.database.dao.WishListDao
import com.mobile.travelapp.repo.database.entity.PlacesEntity
import com.mobile.travelapp.repo.database.entity.WishListEntity
import com.mobile.travelapp.repo.model.Place
import com.mobile.travelapp.repo.service.TravelApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelRepoImpl @Inject constructor(
    private val travelApi: TravelApi,
    private val placesDao: PlacesDao,
    private val wishListDao: WishListDao,
    @CoroutinesDispatchersModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TravelRepo {

    override suspend fun fetchPlacesFromService() {
        withContext(ioDispatcher) {
            val results = try {
                travelApi
                    .fetchPopularPlaces()
                    .rows
            } catch (ex: Throwable) {
                emptyList()
            }
            try {
                addPlacesToDB(results)
            } catch (e: Throwable) {
            }
        }
    }

    private suspend fun addPlacesToDB(places: List<Place>) {
        withContext(ioDispatcher) {
            placesDao
                .insertPlaces(
                    places = Place.convertModelToEntity(
                        places.filter {
                            it.id != "115"
                        }
                    )
                )
        }
    }

    override suspend fun getPopularPlaces(): Flow<List<PlacesEntity>> {
        return withContext(ioDispatcher) {
            placesDao.getPopularPlaces()
        }
    }

    override suspend fun getCategories(): Flow<List<PlacesEntity>> {
        return withContext(ioDispatcher) {
            placesDao.getCategories()
        }
    }

    override suspend fun getTrendingPlaces(): Flow<List<PlacesEntity>> {
        return withContext(ioDispatcher) {
            placesDao.getTrendingPlaces()
        }
    }

    override suspend fun searchPlacesWithQuery(query: String): Flow<List<PlacesEntity>> {
        return withContext(ioDispatcher) {
            placesDao.getPlacesMatchingSearchQuery(query = query)
        }
    }

    override suspend fun getWishListedPlaces(): Flow<List<PlacesEntity>> {
        return withContext(ioDispatcher) {
            placesDao.getWishListedPlaces()
        }
    }

    override suspend fun addOrRemovePlaceFromWishlist(id: String) {
        withContext(ioDispatcher) {
            val result = wishListDao
                .getWishList()
            if (result.contains(id))
                wishListDao.removeWishList(id)
            else
                wishListDao.insertWishList(WishListEntity(id = id))
        }
    }

    override suspend fun getPlacesWithCategory(category: String): Flow<List<PlacesEntity>> {
        return withContext(ioDispatcher) {
            placesDao.getPlacesWithCategory(category)
        }
    }

    override suspend fun getPlaceDetailWithID(id: String): Flow<PlacesEntity> {
        return withContext(ioDispatcher) {
            placesDao.getPlaceDetailWithID(id)
        }
    }

    override suspend fun getComplementPlaces(): Flow<List<PlacesEntity>> {
        return withContext(ioDispatcher) {
            placesDao.getAllPlaces()
        }
    }
}
