package com.example.flexie.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexie.models.movie_view_pager
import com.example.flexie.repository.home_movie_viewpager_repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class d_homeScreen_ViewModel @Inject constructor(private val repository : home_movie_viewpager_repository) : ViewModel() {
    private var _moviesViewPager = MutableStateFlow<List<movie_view_pager>>(emptyList())
    val movieViewPager : StateFlow<List<movie_view_pager>> = _moviesViewPager

    init {
        loadMovieViewPagerData()
    }

    fun loadMovieViewPagerData(){
        viewModelScope.launch {
            _moviesViewPager.value = repository.getMovieViewpager()
        }
    }
}