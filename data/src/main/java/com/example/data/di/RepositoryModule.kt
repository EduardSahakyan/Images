package com.example.data.di

import com.example.data.repositories.ImagesRepositoryImpl
import com.example.domain.repositories.ImagesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindImagesRepository(imagesRepositoryImpl: ImagesRepositoryImpl): ImagesRepository

}