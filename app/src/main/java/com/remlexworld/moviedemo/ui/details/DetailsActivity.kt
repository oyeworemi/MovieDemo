package com.remlexworld.moviedemo.ui.details

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.remlexworld.moviedemo.Config
import com.remlexworld.moviedemo.R
import com.remlexworld.moviedemo.model.Movie
import com.remlexworld.moviedemo.model.Result
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.item_movie.view.*


/**
 * Shows detailed information about selected movie
 */

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        intent?.getSerializableExtra(EXTRAS_MOVIE_DETAILS)?.let { movie ->
            var selectedMovie = movie as Movie
            viewModel.fetchMovieDetailsData(selectedMovie.imdbID)
            subscribeUi()
            setUi(selectedMovie)

        } ?: showError("Error: Unknown Movie")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showError(msg: String) {
        Snackbar.make(vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    private fun subscribeUi() {
        viewModel.movieDetails.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let {
                        updateUi(it)
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


    private fun setUi(movie: Movie) {
        title = movie.Title
        tvTitle.text = movie.Title
        tvIMDBID.text = movie.imdbID
        tvType.text = movie.Type
        tvYear.text = movie.Year


        Glide
            .with(this)
            .load(movie.Poster)
            //.centerCrop()
            .placeholder(R.color.gray)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(ivCover)

    }

    private fun updateUi(movie : Movie) {
        tvDescription.text = movie.Plot
        tvIMDBID.text = movie.Genre
        tvType.text = movie.Rated

    }

    companion object {
        const val EXTRAS_MOVIE_DETAILS = "movie_details"
    }
}