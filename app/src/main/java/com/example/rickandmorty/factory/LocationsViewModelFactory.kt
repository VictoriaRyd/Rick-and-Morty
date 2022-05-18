package com.example.rickandmorty.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.db.RepositoryImpl
import com.example.rickandmorty.ui.viewModels.LocationsViewModel
import javax.inject.Inject

class LocationsViewModelFactory @Inject constructor(
    private val repository: RepositoryImpl): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationsViewModel(repository) as T
    }
}