package com.example.rickandmorty.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.Repository
import com.example.rickandmorty.ui.character.Character
import com.example.rickandmorty.ui.character.CharactersFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharactersViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    var listCharacters = MutableLiveData<List<Character>?>()
    var isLoading = MutableLiveData<Boolean>()
    var pages = MutableLiveData<Int>()
    var isEmptyFilteredResult = MutableLiveData<Boolean>()
    var isEmpty = MutableLiveData<Boolean>()

    fun getAllCharacters(page: Int, filter: CharactersFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val characters = repository.getCharacters(page, filter)
            launch(Dispatchers.Main) {
                updateLiveData(characters?.results)
                updatePages(characters?.info?.pages)
            }
        }
    }

    fun getCharacterFilter(filter: CharactersFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getCharacterFilter(filter)
            launch(Dispatchers.Main) {
                result?.let {
                    setFilteredList(it.results)
                    if (it.results.isEmpty()) {
                        isEmptyFilteredResult.value = true
                    }
                    isEmptyFilteredResult.value = false
                }
                updatePages(result?.info?.pages)
            }
        }
    }

    fun reloadCharacters(page: Int, filter: CharactersFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getCharacters(page, filter)
            launch(Dispatchers.Main) {
                result?.let {
                    setFilteredList(it.results)
                    if (it.results.isEmpty()) {
                        isEmptyFilteredResult.value = true
                    }
                    isEmptyFilteredResult.value = false
                }
                updatePages(result?.info?.pages)
            }
        }
    }

    private fun updateLiveData(list: List<Character>?) {
        if (listCharacters.value.isNullOrEmpty()) {
            if (list.isNullOrEmpty()) {
                isEmpty.value = true
            } else {
                listCharacters.value = list
            }
        } else {
            if (list != null) {
                for (character in list) {
                    listCharacters.value = listCharacters.value?.plus(character)
                }
            }
        }
        isEmpty.value = false
        isLoading.value = false
    }

    private fun setFilteredList(list: List<Character>?) {
        listCharacters.value = list
        isLoading.value = false
    }

    private fun updatePages(pages: Int?) {
        this.pages.value = pages
    }
}