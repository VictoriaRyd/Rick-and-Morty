package com.example.rickandmorty.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.ui.Repository
import com.example.rickandmorty.ui.character.Character
import com.example.rickandmorty.ui.location.Locations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    var location = MutableLiveData<Locations>()
    var charactersListLiveData = MutableLiveData<List<Character>>()
    var isLoading = MutableLiveData<Boolean>()
    var isNoCharacters = MutableLiveData<Boolean>()
    var isNotEnoughCharactersFound = MutableLiveData<Boolean>()
    var isNoDataFound = MutableLiveData<Boolean>()

    fun getLocationId(id: Int) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getLocationId(id)
            launch(Dispatchers.Main) {
                result?.let {
                    location.postValue(it)}
                    ?: run {isNoDataFound.value = true}
                isLoading.value = false
            }
        }
    }

    fun getCharacterId(charactersUrlList: List<String>) {
        if (charactersUrlList.isNullOrEmpty()) {
            updateIsNoCharacters()
        } else {
            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = ArrayList<Character>()
                for (characterUrl in charactersUrlList) {
                    if (characterUrl != "") {
                        val id = characterUrl.split("/").last().toInt()
                        val character = repository.getCharacterId(id)
                        character?.let {
                            result.add(it) }
                    }
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
        charactersList?.let {
            if (it.size < charactersUrlList.size) {
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