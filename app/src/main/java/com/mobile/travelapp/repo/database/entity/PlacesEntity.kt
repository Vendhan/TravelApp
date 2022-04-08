package com.mobile.travelapp.repo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlacesEntity(

    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "imageURL")
    val imageURL: String,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "city")
    val city: String,
    @ColumnInfo(name = "isPopular")
    val isPopular: Boolean,
    @ColumnInfo(name = "isTrending")
    val isTrending: Boolean
)
