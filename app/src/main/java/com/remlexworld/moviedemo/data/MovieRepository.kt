package com.remlexworld.moviedemo.data

import com.remlexworld.moviedemo.data.local.MovieDao
import com.remlexworld.moviedemo.data.remote.MovieRemoteDataSource
import com.remlexworld.moviedemo.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Repository which fetches data from Remote or Local data sources
 */
class MovieRepository @Inject constructor(
        private val movieRemoteDataSource: MovieRemoteDataSource,
        private val movieDao: MovieDao
) {


    suspend fun fetchMovies(searchText: String, apiKey : String, pageIndex : Int): Flow<Result<SearchResults>?> {
        return flow {
            emit(fetchMoviesCached())
            emit(Result.loading())
            val result = movieRemoteDataSource.fetchMovies(searchText, apiKey, pageIndex)


            //Cache to database if response is successful
            if (result.status == Result.Status.SUCCESS) {
                result.data?.Search?.let { it ->
                    movieDao.deleteAll(it)
                    movieDao.insertAll(it)
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    private fun fetchMoviesCached(): Result<SearchResults>? =
            movieDao.getAll()?.let {
                Result.success(SearchResults(it))
            }



    suspend fun fetchMovieDetails(imdbId: String, apiKey : String): Flow<Result<Movie>> {
        return flow {
            emit(Result.loading())
            emit(movieRemoteDataSource.fetchMovieDetails(imdbId, apiKey))
        }.flowOn(Dispatchers.IO)
    }
}