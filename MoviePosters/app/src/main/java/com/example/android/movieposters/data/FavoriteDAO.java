package com.example.android.movieposters.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.movieposters.object.Movie;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface FavoriteDAO {

    @Query("SELECT * FROM Favorite")
    LiveData<List<Movie>> loadAllFavorites();

    /**If the table has more than one column, you can use

     @Insert(onConflict = OnConflictStrategy.REPLACE)

     to replace a row.
     * **/
    @Insert(onConflict = REPLACE)
    void insertFavoriteMovie(Movie favoriteEntity);

    @Delete
    void deleteFavoriteMovie(Movie favoriteEntity);

    @Query("SELECT * FROM Favorite WHERE movieId = :movieId")
    LiveData<Movie> loadMovieById(int movieId);
}
