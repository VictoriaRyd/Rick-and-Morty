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

class EpisodeViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    var episodeLiveData = MutableLiveData<Episode>()
    var charactersListLiveData = MutableLiveData<List<Character>>()
    var isLoading = MutableLiveData<Boolean>()
    var isNoCharacters = MutableLiveData<Boolean>()
    var isNotEnoughCharactersFound = MutableLiveData<Boolean>()
    var isNoDataFound = MutableLiveData<Boolean>()

    fun getEpisodeId(id: Int) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getEpisodeId(id)
            launch(Dispatchers.Main) {
                result?.let {
                    episodeLiveData.postValue(it)}
                    ?: run {isNoDataFound.value = true}
                isLoading.value = false
            }
        }
    }

    fun getCharactersId(charactersUrlList: List<String>) {
        if (charactersUrlList.isNullOrEmpty()) {
            updateIsNoCharacters()
        }else {
            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = ArrayList<Character>()
                for (characterUrl in charactersUrlList) {
                    val id = characterUrl.split("/").last().toInt()
                    val character = repository.getCharacterId(id)
                    character?.let {
                        result.add(it) }
                }
                launch(Dispatchers.Main) {
                    updateCharactersListLiveData(result, charactersUrlList)
                }
            }
        }
    }

    private fun updateCharactersListLiveData(charactersList: List<Character>?,
                                             charactersUrlList: List<String>) {
        this.charactersListLiveData.value = charactersList
        isLoading.value = false
        if (charactersList != null) {
            if (charactersList.size < charactersUrlList.size) {
                updateIsNotEnoughCharactersFound()
            }
        }
    }

    private fun updateIsNoCharacters() {
        isNoCharacters.value = true
    }

    private fun updateIsNotEnoughCharactersFound() {
        isNotEnoughCharactersFound.value = true
    }
}