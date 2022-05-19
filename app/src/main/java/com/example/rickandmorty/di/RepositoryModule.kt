package com.example.rickandmorty.di

import com.example.rickandmorty.db.RepositoryImpl
import com.example.rickandmorty.ui.Repository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideRepository(repository: RepositoryImpl): Repository

}