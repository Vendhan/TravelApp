package com.mobile.travelapp.di

import com.mobile.travelapp.repo.TravelRepo
import com.mobile.travelapp.repo.TravelRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun provideTravelRepositoryImpl(repository: TravelRepoImpl): TravelRepo
}
