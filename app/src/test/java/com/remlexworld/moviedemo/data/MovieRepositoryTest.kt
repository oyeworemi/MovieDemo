package com.remlexworld.moviedemo.data

import com.google.gson.GsonBuilder
import com.remlexworld.moviedemo.data.MockWebServerResponses.movieListResponse
import com.remlexworld.moviedemo.data.MockWebServerResponses.movieWithIMBDtt0103776
import com.remlexworld.moviedemo.data.local.AppDatabaseFake
import com.remlexworld.moviedemo.data.local.MovieDaoFake
import com.remlexworld.moviedemo.data.remote.MovieRemoteDataSource
import com.remlexworld.moviedemo.model.Movie
import com.remlexworld.moviedemo.model.Result
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class MovieRepositoryTest {


    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val DUMMY_TOKEN = "123456" // can be anything
    private val DUMMY_QUERY = "batman" // can be anything
    private val DUMMY_imdbID = "tt0103776"


    // system in test
    private lateinit var movieRepository: MovieRepository

    // Dependencies
    private lateinit var movieDao: MovieDaoFake
    private lateinit var movieRemoteDataSource: MovieRemoteDataSource
    private lateinit var retrofit: Retrofit


    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/")

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        movieDao = MovieDaoFake(appDatabaseFake = appDatabase)

        movieRemoteDataSource = MovieRemoteDataSource(retrofit)

        // instantiate the system in test
        movieRepository = MovieRepository(
                movieRemoteDataSource,
                  movieDao
        )
    }


    /**
     * 1. Are the movies retrieved from the network?
     * 2. Are the movies inserted into the cache?
     * 3. Are the movies then emitted as a flow?
     */
    @Test
    fun fetchMoviesFromNetwork_emitMovies(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
                MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody(movieListResponse)
        )

        // confirm the cache is empty to start
        movieDao.getAll()?.isEmpty()?.let { assert(it) }

        val flowItems = movieRepository.fetchMovies(DUMMY_QUERY, DUMMY_TOKEN, 1).toList()

        // confirm the cache is no longer empty
        movieDao.getAll()?.isNotEmpty()?.let { assert(it) }

        //first emission is previously cached movies, if available
        // second emission should be `loading`
        assert(flowItems[1] == Result.loading(null))

        // third emission should be the list of movies
        val movies = flowItems[2]?.data
        assert(movies?.Search?.size ?: 0 > 0)

        // confirm they are actually Movie objects
        assert(movies?.Search?.get(index = 0) is Movie)

        assert(flowItems[2] != Result.loading(null)) //loading should be false now

    }


    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }


    @Test
    fun fetchMovieDetailsFromNetwork_emitMovie(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
                MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody(movieWithIMBDtt0103776)
        )


        val flowItems = movieRepository.fetchMovieDetails(DUMMY_imdbID, DUMMY_TOKEN).toList()


        // first emission should be `loading`
        assert(flowItems[0] == Result.loading(null))

        // second emission should be the movie details
        val movie = flowItems[1]?.data
        //confirm movie id
        assert(movie?.imdbID == DUMMY_imdbID)

        // confirm response is a Movie object
        assert(movie is Movie)


        assert(flowItems[1] != Result.loading(null)) //loading should be false now

    }


}