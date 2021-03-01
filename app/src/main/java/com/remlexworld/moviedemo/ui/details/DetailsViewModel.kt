package com.remlexworld.moviedemo.ui.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import retrofit2.Response
import com.remlexworld.moviedemo.Config
import com.remlexworld.moviedemo.data.MovieRepository
import com.remlexworld.moviedemo.model.Movie
import com.remlexworld.moviedemo.model.MovieDesc
import com.remlexworld.moviedemo.model.Result
import com.remlexworld.moviedemo.model.SearchResults
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


/**
 * ViewModel for Movie details screen
 */
class DetailsViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) : ViewModel() {


    private val _movieDetails = MutableLiveData<Result<Movie>>()
    val movieDetails = _movieDetails



    fun fetchMovieDetailsData(imdbId : String) {

        viewModelScope.launch {

            movieRepository.fetchMovieDetails(imdbId, Config.API_KEY).collect {
                _movieDetails.value = it
            }


        }

    }

}