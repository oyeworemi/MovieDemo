package com.remlexworld.moviedemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.remlexworld.moviedemo.data.GenreConverters
import com.remlexworld.moviedemo.model.Movie

@Database(entities = [Movie::class], version = 2)
@TypeConverters(GenreConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}