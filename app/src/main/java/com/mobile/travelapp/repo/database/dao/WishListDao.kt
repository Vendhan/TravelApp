package com.mobile.travelapp.repo.database.dao

import androidx.room.*
import com.mobile.travelapp.repo.database.entity.WishListEntity

@Dao
interface WishListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWishList(wishListEntity: WishListEntity)

    @Query("DELETE FROM WishListEntity WHERE id = :id")
    fun removeWishList(id: String)

    @Query("SELECT * FROM WishListEntity")
    fun getWishList(): List<String>
}
