package com.example.rickandmorty.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.Repository
import com.example.rickandmorty.ui.location.Locations
import com.example.rickandmorty.ui.location.LocationsFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationsViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    var listLocations = MutableLiveData<List<Locations>?>()
    var isLoading = MutableLiveData<Boolean>()
    var pages = MutableLiveData<Int>()
    var isEmptyFilteredResult = MutableLiveData<Boolean>()
    var isEmpty = MutableLiveData<Boolean>()
    var filter = LocationsFilter()

    fun getLocations(page: Int, filter: LocationsFilter){
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getLocation(page, filter)
            launch(Dispatchers.Main) {
                updateLocationList(result?.results)
                updatePages(result?.info?.pages)
            }
        }
    }

    private fun updateLocationList(list: List<Locations>?) {
        if (listLocations.value.isNullOrEmpty()) {
            if(list.isNullOrEmpty()) {
                isEmpty.value = true
            } else {
                listLocations.value = list
            }
        } else {
            if (list != null) {
                for (location in list) {
                    listLocations.value = listLocations.value?.plus(location)
                }
            }
        }
        isLoading.value = false
        isEmpty.value = false
    }

    fun reloadLocations(page: Int, filter: LocationsFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getLocation(page, filter)
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

    private fun setFilteredList(list: List<Locations>?) {
        listLocations.value = list
        isLoading.value = false
    }

    private fun updatePages(pages: Int?) {
        this.pages.value = pages
    }

    fun getLocationFilter(filter: LocationsFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getLocationFilter(filter)
            launch(Dispatchers.Main) {
                result?.let {
                    setFilteredList(it.results)
                    if(it.results.isEmpty()) {
                        isEmptyFilteredResult.value = true
                    }
                    isEmptyFilteredResult.value = false
                }
                updatePages(result?.info?.pages)
            }
        }
    }
}