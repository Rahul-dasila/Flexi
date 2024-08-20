package com.example.flexie.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexie.models.movie_detail_item
import com.example.flexie.models.movie_home_row
import com.example.flexie.repository.home_movie_category_repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModelMovie @Inject constructor (private val homeMovieCategoryRepository: home_movie_category_repository) : ViewModel() {
    var movieData : movie_detail_item? = null

    private val moreLikeThis : MutableStateFlow<List<movie_home_row>> = MutableStateFlow<List<movie_home_row>>(
        emptyList()
    )
    var loading1 by mutableStateOf(true)
    init {
        loading1 = true
    }
    var _moreLikeThis : StateFlow<List<movie_home_row>> = moreLikeThis
    var _category : MutableStateFlow<String> = MutableStateFlow<String>("")
    var movieId by mutableStateOf("")



    fun loadMoreMovies(){
        viewModelScope.launch {
            val list: List<movie_home_row> = homeMovieCategoryRepository.getMovies(_category.value)
            val filteredList = list.filter { it.id != movieId }
            moreLikeThis.value = filteredList
            loading1 = false
        }
    }

}