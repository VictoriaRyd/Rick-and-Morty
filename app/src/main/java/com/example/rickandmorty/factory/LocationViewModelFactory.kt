package com.example.rickandmorty.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.db.RepositoryImpl
import com.example.rickandmorty.ui.viewModels.LocationViewModel
import javax.inject.Inject

class LocationViewModelFactory @Inject constructor(
    private val repository: RepositoryImpl): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationViewModel(repository) as T
    }
}