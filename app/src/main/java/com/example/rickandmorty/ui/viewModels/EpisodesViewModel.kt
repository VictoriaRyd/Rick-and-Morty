package com.example.rickandmorty.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.Repository
import com.example.rickandmorty.ui.episodes.Episode
import com.example.rickandmorty.ui.episodes.EpisodesFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodesViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    var listEpisodes = MutableLiveData<List<Episode>?>()
    var isLoading = MutableLiveData<Boolean>()
    var pages = MutableLiveData<Int>()
    var isEmptyFilteredResult = MutableLiveData<Boolean>()
    var isEmpty = MutableLiveData<Boolean>()
    var filter = EpisodesFilter()

    fun getEpisodes(page: Int, filter: EpisodesFilter){
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val episodes = repository.getEpisodes(page, filter)
            launch(Dispatchers.Main) {
                updateEpisodesList(episodes?.results)
                updatePages(episodes?.info?.pages)
            }
        }
    }

    private fun updateEpisodesList(list: List<Episode>?) {
        if (listEpisodes.value.isNullOrEmpty()) {
            if (list.isNullOrEmpty()) {
                isEmpty.value = true
            } else {
                listEpisodes.value = list
            }
        } else {
            if (list != null) {
                for (episode in list) {
                    listEpisodes.value = listEpisodes.value?.plus(episode)
                }
            }
        }
        isLoading.value = false
        isEmpty.value = false
    }

    private fun updatePages(pages: Int?) {
        this.pages.value = pages
    }

    fun getEpisodeFilter(filter: EpisodesFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getEpisodeFilter(filter)
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

    fun reloadEpisodes(page: Int, filter: EpisodesFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getEpisodes(page, filter)
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

    private fun setFilteredList(list: List<Episode>?) {
        listEpisodes.value = list
        isLoading.value = false
    }
}