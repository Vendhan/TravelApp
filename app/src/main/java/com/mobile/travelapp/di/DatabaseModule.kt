package com.mobile.travelapp.di

import android.content.Context
import androidx.room.Room
import com.mobile.travelapp.repo.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "Places_DB"
    ).build()

    @Singleton
    @Provides
    fun providePlacesDao(appDatabase: AppDatabase) = appDatabase.placesDao()

    @Singleton
    @Provides
    fun provideWishListDao(appDatabase: AppDatabase) = appDatabase.wishListDao()
}
