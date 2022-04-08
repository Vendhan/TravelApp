package com.mobile.travelapp.repo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobile.travelapp.repo.database.dao.PlacesDao
import com.mobile.travelapp.repo.database.dao.WishListDao
import com.mobile.travelapp.repo.database.entity.PlacesEntity
import com.mobile.travelapp.repo.database.entity.WishListEntity

@Database(entities = [PlacesEntity::class, WishListEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun placesDao(): PlacesDao

    abstract fun wishListDao(): WishListDao
}
