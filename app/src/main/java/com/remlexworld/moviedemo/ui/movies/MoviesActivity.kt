package com.remlexworld.moviedemo.ui.movies

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.remlexworld.moviedemo.R
import com.remlexworld.moviedemo.model.Movie
import com.remlexworld.moviedemo.model.Result
import com.remlexworld.moviedemo.util.*
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.coroutines.delay


/**
 * Shows list of movie/show
 */
@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {

    private val list = ArrayList<Movie>()
    private val viewModel by viewModels<MoviesViewModel>()
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        init()
        subscribeUi()

    }

    private fun init() {
        title = "Movies"
        val layoutManager = LinearLayoutManager(this)
        rvMovies.layoutManager = layoutManager

        moviesAdapter = MoviesAdapter(this, list)
        rvMovies.adapter = moviesAdapter

        iv_searchIcon.setOnClickListener {
            searchMovies(
                et_searchField.text.toString().trim()
            )

             this?.dismissKeyboard(it)

        }

    }

    private fun subscribeUi() {
        viewModel.movieList.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.Search?.let { list ->
                        moviesAdapter.updateData(list)
                    }
                    loading.visibility = View.GONE
                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showError(it)
                    }
                    loading.visibility = View.GONE
                }

                Result.Status.LOADING -> {
                    loading.visibility = View.VISIBLE
                }
            }

        })
    }

    private fun showError(msg: String) {
        Snackbar.make(vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    private fun searchMovies(searchText : String) {

        lifecycleScope.launchWhenCreated {

            if (searchText.isNotEmpty() && searchText.length > 3) {
                viewModel.fetchMovies(searchText)

            } else {
                showError(getString(R.string.enter_minimum_of_four_characters))
            }
        }

    }





}