package com.remlexworld.moviedemo.network.services

import com.remlexworld.moviedemo.model.SearchResults
import com.remlexworld.moviedemo.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API Service
 */
interface MovieService {



    @GET("?type=movie")
    suspend fun getSearchMovies(
        @Query(value = "s") searchText : String,
        @Query(value = "apiKey") apiKey : String,
        @Query(value = "page") pageIndex : Int
    ) : Response<SearchResults>

    @GET("?plot=full")
    suspend fun getMovieDetailsData(
        @Query(value = "i") imdbId : String,
        @Query(value = "apiKey") apiKey: String
    ) : Response<Movie>
}