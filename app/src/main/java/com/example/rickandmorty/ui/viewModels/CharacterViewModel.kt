package com.example.rickandmorty.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.Repository
import com.example.rickandmorty.ui.character.Character
import com.example.rickandmorty.ui.episodes.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    var characterLiveData = MutableLiveData<Character>()
    var episodesListLiveData = MutableLiveData<List<Episode>>()
    var isLoading = MutableLiveData<Boolean>()
    var isNoEpisodes = MutableLiveData<Boolean>()
    var isNotEnoughEpisodesFound = MutableLiveData<Boolean>()
    var isNoDataFound = MutableLiveData<Boolean>()

    fun getCharacterById(id: Int) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getCharacterId(id)
            launch(Dispatchers.Main) {
                result?.let {
                    characterLiveData.postValue(it)} ?:
                    run{isNoDataFound.value = true}
                isLoading.value = false
            }
        }
    }

    fun getEpisodesId(episodesUrlList: List<String>) {
        if (episodesUrlList.isNullOrEmpty()) {
            updateIsNoEpisodes()
        } else {
            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = ArrayList<Episode>()
                for (episodeUrl in episodesUrlList) {
                    val id = episodeUrl.split("/").last().toInt()
                    val episode = repository.getEpisodeId(id)
                    episode?.let {
                        result.add(it) }
                }
                launch(Dispatchers.Main) {
                    updateEpisodesListLiveData(result, episodesUrlList)
                }
            }
        }
    }

    private fun updateEpisodesListLiveData(episodesList: List<Episode>?,
                                           episodesUrlList: List<String>) {
        this.episodesListLiveData.value = episodesList
        isLoading.value = false
        if (episodesList != null) {
            if (episodesList.size < episodesUrlList.size) {
                updateIsNotEnoughEpisodesFound()
            }
        }
    }

    private fun updateIsNoEpisodes() {
        isNoEpisodes.value = true
    }

    private fun updateIsNotEnoughEpisodesFound() {
        isNotEnoughEpisodesFound.value = true
    }

}