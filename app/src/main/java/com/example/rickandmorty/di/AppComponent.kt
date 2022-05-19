package com.example.rickandmorty.di

import com.example.rickandmorty.api.RetrofitClient
import com.example.rickandmorty.ui.MainActivity
import com.example.rickandmorty.ui.character.CharactersFragment
import com.example.rickandmorty.ui.character.DetailsCharactersFragment
import com.example.rickandmorty.ui.episodes.DetailsEpisodesFragment
import com.example.rickandmorty.ui.episodes.EpisodesFragment
import com.example.rickandmorty.ui.location.DetailsLocationFragment
import com.example.rickandmorty.ui.location.LocationFragment
import com.example.rickandmorty.ui.viewModels.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RetrofitClient::class, DataModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(charactersFragment: CharactersFragment)
    fun inject(charactersDetailFragment: DetailsCharactersFragment)
    fun inject(charactersViewModel: CharactersViewModel)
    fun inject(characterViewModel: CharacterViewModel)
    fun inject(episodeDetailFragment: DetailsEpisodesFragment)
    fun inject(episodesFragment: EpisodesFragment)
    fun inject(episodesViewModel: EpisodesViewModel)
    fun inject(episodeViewModel: EpisodeViewModel)
    fun inject(locationDetailsFragment: DetailsLocationFragment)
    fun inject(locationsFragment: LocationFragment)
    fun inject(locationsViewModel: LocationsViewModel)
    fun inject(locationViewModel: LocationViewModel)
}