package com.example.flexie.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexie.models.movie_detail_item
import com.example.flexie.models.movie_home_row
import com.example.flexie.repository.Movie_detail_repository
import com.example.flexie.repository.home_movie_category_repository
import com.example.flexie.utils.IdObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Movie_detail_viewmodel @Inject constructor(private val movieDetailRepository: Movie_detail_repository , private val homeMovieCategoryRepository: home_movie_category_repository) : ViewModel() {

    private var pageData : MutableStateFlow<movie_detail_item?> = MutableStateFlow<movie_detail_item?>(null)
    val _pageData : StateFlow<movie_detail_item?> = pageData

    var posterurl by mutableStateOf("")

    private val moreLikeThis : MutableStateFlow<List<movie_home_row>> = MutableStateFlow<List<movie_home_row>>(
        emptyList()
    )
    var _moreLikeThis : StateFlow<List<movie_home_row>> = moreLikeThis

    fun loadMovieData(){
        viewModelScope.launch {
            val id = IdObject.id
            pageData.value = movieDetailRepository.getMovieDetail(id)
        }
    }

    fun loadMoreMovies(category : String){
        viewModelScope.launch {
            val list: List<movie_home_row> = homeMovieCategoryRepository.getMovies(category)
            val filteredList = list.filter { it.id != IdObject.id }
            moreLikeThis.value = filteredList
        }
    }
}