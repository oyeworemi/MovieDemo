package com.remlexworld.moviedemo.ui.movies

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remlexworld.moviedemo.Config.API_KEY
import com.remlexworld.moviedemo.data.MovieRepository
import com.remlexworld.moviedemo.model.Result
import com.remlexworld.moviedemo.model.SearchResults
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

/**
 * ViewModel for MoviesActivity
 */
class MoviesViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) :
        ViewModel() {

    private val _movieList = MutableLiveData<Result<SearchResults>>()
    val movieList = _movieList


     fun fetchMovies(searchTitle: String) {
        viewModelScope.launch {

            movieRepository.fetchMovies(searchTitle, API_KEY, 1).collect {
                _movieList.value = it
            }

        }
    }


}