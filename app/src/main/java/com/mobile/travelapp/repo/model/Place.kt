package com.mobile.travelapp.repo.model

import com.mobile.travelapp.repo.database.entity.PlacesEntity

data class PlacesWrapper(
    val rows: List<Place>
)

data class Place(
    val id: String,
    val name: String,
    val about: String,
    val type: String,
    val image: String,
    val location: String,
    val city: String,
    val is_popular: Boolean,
    val is_trending: Boolean
) {
    companion object {
        fun convertModelToEntity(places: List<Place>): List<PlacesEntity> {
            val placesEntityList = ArrayList<PlacesEntity>()
            places.forEach { place ->
                placesEntityList.add(
                    PlacesEntity(
                        id = place.id,
                        name = place.name,
                        description = place.about,
                        type = place.type,
                        imageURL = place.image,
                        location = place.location,
                        city = place.city,
                        isPopular = place.is_popular,
                        isTrending = place.is_trending
                    )
                )
            }
            return placesEntityList
        }
    }
}

/*enum class PlaceType {
    Spiritual,
    WildLife,
    HillStation,
    Heritage,
    Beach
}*/
