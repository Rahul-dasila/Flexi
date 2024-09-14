package com.example.flexie.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexie.models.movie_home_row
import com.example.flexie.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository) : ViewModel() {
    private var recommendedList : MutableStateFlow<List<movie_home_row>> = MutableStateFlow<List<movie_home_row>>(
        emptyList()
    )
    var query by mutableStateOf("")
    val _recommendedList : StateFlow<List<movie_home_row>> = recommendedList
    var loading by mutableStateOf(true)
    var loading2 by mutableStateOf(false)
    var searching by mutableStateOf(true)

     var searchResult : MutableStateFlow<List<movie_home_row>> = MutableStateFlow<List<movie_home_row>>(
        emptyList()
    )

    var _searchResult : StateFlow<List<movie_home_row>> = searchResult

    init{
        loadRecommendedMovies()
    }
    private fun loadRecommendedMovies(){
        viewModelScope.launch {
            loading = true
            recommendedList.value = searchRepository.getRecommendedMovies()
            loading = false
        }
    }

    fun getSearchResult(query :String){
        if(query.isNotEmpty()) {
            viewModelScope.launch {
                loading2 = true
                searchResult.value = searchRepository.getSearchQuery(query)
                loading2 = false
                searching = false
            }
        }
    }
}