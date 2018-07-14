package com.example.android.movieposters.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private static final String TAG = FavoriteViewModel.class.getSimpleName();
    private final LiveData<List<FavoriteEntity>> mFavoriteMovies;
    private FavoriteDatabase favoriteDatabase;

    public FavoriteViewModel(Application application) {
        super(application);

        favoriteDatabase = FavoriteDatabase.getInstance(this.getApplication());
        mFavoriteMovies = favoriteDatabase.favoriteDAO().loadAllFavorites();
        Log.d(TAG, "FavoriteEntity RoomDatabase is loaded");
    }

    public LiveData<List<FavoriteEntity>> loadAllFavorites() {
        return mFavoriteMovies;
    }

    public LiveData<FavoriteEntity> loadMovieById(int movieId){
        return favoriteDatabase.favoriteDAO().loadMovieById(movieId);
    }

    public void addFavoriteMovie(FavoriteEntity favoriteEntity){
        Log.d(TAG, "adding favorite movie");
        new insertAsyncTask(favoriteDatabase).execute(favoriteEntity);
    }

    public void removeFavoriteMovie(FavoriteEntity favoriteEntity){
        Log.d(TAG, "delete favorite movie");
        new deleteAsyncTask(favoriteDatabase).execute(favoriteEntity);
    }

    private static class insertAsyncTask extends AsyncTask<FavoriteEntity, Void, Void> {
        private FavoriteDatabase favoriteDatabase;

        insertAsyncTask(FavoriteDatabase favoriteDatabase)
        {
            favoriteDatabase = favoriteDatabase;
        }

        @Override
        protected Void doInBackground(final FavoriteEntity... favoriteEntities) {
            favoriteDatabase.favoriteDAO().insertFavoriteMovie(favoriteEntities[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<FavoriteEntity, Void, Void>
    {
        private FavoriteDatabase favoriteDatabase;

        deleteAsyncTask(FavoriteDatabase favoriteDatabase)
        {
            favoriteDatabase = favoriteDatabase;
        }

        @Override
        protected Void doInBackground(final FavoriteEntity... favoriteEntities) {
            favoriteDatabase.favoriteDAO().deleteFavoriteMovie(favoriteEntities[0]);
            return null;
        }
    }


}
