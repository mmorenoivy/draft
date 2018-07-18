package com.example.android.movieposters.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.android.movieposters.object.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {

    private static final String LOG_TAG = Movie.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String Database_Name = "favoriteMovies";
    private static FavoriteDatabase favoriteInstance;

    public static FavoriteDatabase getInstance(Context mContext) {
        if (favoriteInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating database instance");
                favoriteInstance = Room.databaseBuilder(mContext.getApplicationContext(),
                        FavoriteDatabase.class, FavoriteDatabase.Database_Name)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Reading database instance");
        return favoriteInstance;
    }

    public abstract FavoriteDAO favoriteDAO();

}
