package com.example.rickandmorty.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.db.RepositoryImpl
import com.example.rickandmorty.ui.viewModels.EpisodeViewModel
import javax.inject.Inject

class EpisodeViewModelFactory @Inject constructor(
    private val repository: RepositoryImpl): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EpisodeViewModel(repository) as T
    }
}