package com.remlexworld.moviedemo.data.local

import com.remlexworld.moviedemo.data.local.AppDatabaseFake
import com.remlexworld.moviedemo.data.local.MovieDao
import com.remlexworld.moviedemo.model.Movie

class MovieDaoFake(
    private val appDatabaseFake: AppDatabaseFake
): MovieDao {


    override fun getAll(): List<Movie>? {
        return appDatabaseFake.movies // return the entire list for simplicity

    }

    override fun insertAll(movies: List<Movie>) {
        appDatabaseFake.movies.addAll(movies)

    }

    override fun delete(movie: Movie) {

    }

    override fun deleteAll(movie: List<Movie>) {
        appDatabaseFake.movies.clear()
    }
}

















