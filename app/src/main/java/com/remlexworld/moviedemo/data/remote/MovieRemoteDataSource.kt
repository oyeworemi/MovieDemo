package com.remlexworld.moviedemo.data.remote

import com.remlexworld.moviedemo.model.*
import com.remlexworld.moviedemo.network.services.MovieService
import com.remlexworld.moviedemo.util.ErrorUtils
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * fetches data from remote source
 */
class MovieRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {


    suspend fun fetchMovies(searchTitle: String,
                            apiKey: String,
                            pageIndex: Int): Result<SearchResults> {
        val movieService = retrofit.create(MovieService::class.java)
        return getResponse(
            request = { movieService.getSearchMovies(searchTitle, apiKey, pageIndex) },
            defaultErrorMessage = "Error fetching Movie list")

    }

    suspend fun fetchMovieDetails(imdbId: String, apiKey: String): Result<Movie> {
        val movieService = retrofit.create(MovieService::class.java);
        return getResponse(
                request = { movieService.getMovieDetailsData(imdbId, apiKey) },
                defaultErrorMessage = "Error fetching Movie Description")
    }


    private suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Result<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error", null)
        }
    }
}