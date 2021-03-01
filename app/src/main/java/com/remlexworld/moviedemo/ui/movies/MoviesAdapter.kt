package com.remlexworld.moviedemo.ui.movies

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.remlexworld.moviedemo.R
import com.remlexworld.moviedemo.model.Movie
import com.remlexworld.moviedemo.ui.details.DetailsActivity
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesAdapter(private val context: Context, private val list: ArrayList<Movie>) :
        RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie) {
            itemView.setOnClickListener {
                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.EXTRAS_MOVIE_DETAILS, movie)
                context.startActivity(intent)

            }

            itemView.tv_title.text = movie.Title
            itemView.tv_year.text = movie.Year

            movie.Poster?.let {
             //   Log.d(TAG, it)

                Glide
                    .with(context)
                    .load(it)
                    .centerCrop()
                    .placeholder(R.color.gray)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemView.iv_poster)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(context, view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateData(newList: List<Movie>) {
        list.clear()
        val sortedList = newList.sortedBy { movie -> movie.Year }
        list.addAll(sortedList)
        notifyDataSetChanged()
    }
}