package com.example.flexie.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexie.models.continueWatchingItem
import com.example.flexie.models.movie_categories
import com.example.flexie.models.movie_home_row
import com.example.flexie.models.movie_view_pager
import com.example.flexie.repository.ContinueWatchingRepository
import com.example.flexie.repository.home_movie_category_repository
import com.example.flexie.repository.home_movie_viewpager_repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class d_homeScreen_ViewModel @Inject constructor(
    private val repository: home_movie_viewpager_repository,
    private val homeMovieCategoryRepository: home_movie_category_repository,
    private val continueWatchingRepository: ContinueWatchingRepository
) : ViewModel() {


    private var _moviesViewPager = MutableStateFlow<List<movie_view_pager>>(emptyList())
    val movieViewPager: StateFlow<List<movie_view_pager>> = _moviesViewPager
    var isLoading by mutableStateOf(true)
    var isCategoryLoading by mutableStateOf(true)

    private var _movieCategories = MutableStateFlow<List<movie_categories>>(emptyList())
    val movieCategories : StateFlow<List<movie_categories>> = _movieCategories

    private var _moviesRow = MutableStateFlow<Map<String,List<movie_home_row>>>(emptyMap())
    val movieRow : StateFlow<Map<String , List<movie_home_row>>> = _moviesRow

    private var continueWatching : MutableStateFlow<List<String>> = MutableStateFlow<List<String>>(
        emptyList()
    )
    val _continueWatching : StateFlow<List<String>> = continueWatching


    private var continueItemList : MutableStateFlow<List<continueWatchingItem>> = MutableStateFlow<List<continueWatchingItem>>(
        emptyList<continueWatchingItem>().toMutableList()
    )
    val _continueItemList : StateFlow<List<continueWatchingItem>> = continueItemList

    private var loadingContinue : MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(true)
    val _loadingContinue : StateFlow<Boolean> = loadingContinue

    init {
        loadMovieViewPagerData()
        loadMovieCategories()
    }
    fun loadMovieViewPagerData() {
        viewModelScope.launch {
            _moviesViewPager.value = repository.getMovieViewpager()
            isLoading = false
        }
    }

    fun loadMovieCategories(){
        viewModelScope.launch {
            _movieCategories.value = homeMovieCategoryRepository.getMovieCategory()
            isCategoryLoading = false
        }
    }

    fun loadMovies(type : String){
        viewModelScope.launch {
            val list = homeMovieCategoryRepository.getMovies(type)
            _moviesRow.value = _moviesRow.value.toMutableMap().apply {
                this[type] = list
            }
        }
    }

    fun loadContinueWatching(){
        viewModelScope.launch {
          continueWatching.value =   continueWatchingRepository.getContinueWatching()
        }
    }

    fun getContinueMovieItem(movieId : List<String>){
        viewModelScope.launch {
            val item = continueWatchingRepository.getMovieItem(movieId)
            loadingContinue.value = false
            continueItemList.value = item
        }
    }
}