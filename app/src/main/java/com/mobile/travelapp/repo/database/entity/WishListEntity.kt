package com.mobile.travelapp.repo.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WishListEntity(
    @PrimaryKey
    val id: String
)
