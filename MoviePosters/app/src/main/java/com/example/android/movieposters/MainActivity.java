package com.example.android.movieposters;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieposters.adapter.Movie_Adapter;
import com.example.android.movieposters.api.MovieAPI;
import com.example.android.movieposters.data.FavoriteViewModel;
import com.example.android.movieposters.object.Movie;
import com.example.android.movieposters.object.MovieList;
import com.facebook.stetho.Stetho;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * reference:
 * https://android--code.blogspot.com/2015/12/android-recyclerview-grid-layout-example.html
 * https://medium.com/@dds861/json-parsing-using-retrofit-and-recycleview-2300d9fdcf15
 * https://inthecheesefactory.com/blog/retrofit-2.0/en
 * https://antonioleiva.com/recyclerview/
 * https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/
 * https://medium.com/@dds861/json-parsing-using-retrofit-and-recycleview-2300d9fdcf15
 * https://dzone.com/articles/checking-an-internet-connection-in-android-2
 * https://blog.fossasia.org/using-stetho-android-database/ - for stetho
 * https://developers.google.com/web/tools/chrome-devtools/remote-debugging/
 * https://github.com/codepath/android_guides/wiki/Debugging-with-Stetho
 * https://github.com/leinardi/FloatingActionButtonSpeedDial
 * https://google-developer-training.gitbooks.io/android-developer-advanced-course-practicals/unit-6-working-with-architecture-components/lesson-14-room,-livedata,-viewmodel/14-1-a-room-livedata-viewmodel/14-1-a-room-livedata-viewmodel.html#task11intro
 * final code Stage 1 - 6/2/2018
 * final code Stage 2 - 7/14/2018
 * coded with my 2 year old Mark
 * *
 */

public class MainActivity extends AppCompatActivity {

    private final static String BASE_URL = "https://api.themoviedb.org/3/";
    private final static String TAG = "Movie App";
    private boolean flag;

    private TextView nullView;
    private boolean favorites;
    private RecyclerView mRecyclerView;
    private Movie_Adapter mRecyclerViewAdapter;
    private FavoriteViewModel favoriteViewModel;
    private List<Movie> moviesList;
    private ProgressBar loader;
    private Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

//        checkConnection();

        mRecyclerView = findViewById(R.id.recycledMovies);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerViewAdapter = new Movie_Adapter(this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        favoriteViewModel = ViewModelProviders.of(MainActivity.this).get(FavoriteViewModel.class);

        if (savedInstanceState != null && savedInstanceState.getBoolean("favorites")) {
            loader.setVisibility(View.GONE);
            getFavoriteMovies();
            favorites = true;
        } else if (isOnline()) {
            callMoviePopular();
            Toast.makeText(MainActivity.this, "You are connected to the Internet.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "You have no internet connection. Favorites are displayed", Toast.LENGTH_LONG).show();
            getFavoriteMovies();
            favorites = true;
        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called.");
        if (state != null)
            mRecyclerView.getLayoutManager().onRestoreInstanceState(state);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume called.");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called.");
        if (state != null)
            mRecyclerView.getLayoutManager().onRestoreInstanceState(state);

    }


    public void callMoviePopular() {

        //here is how to implement retrofit to call the api
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Movie_Adapter.LoggingInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieAPI requestService = retrofit.create(MovieAPI.class);

        Call<MovieList> movieListCall = requestService.getMoviePopular();
        movieListCall.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                mRecyclerViewAdapter.setMovieList(response.body().getResults());
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.d("Error", t.getLocalizedMessage());
            }
        });
    }

    public void callTopRated() {
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Movie_Adapter.LoggingInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieAPI requestService = retrofit.create(MovieAPI.class);

        Call<MovieList> movieListCall = requestService.getMovieTopRated();

        movieListCall.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {

                mRecyclerViewAdapter.setMovieList(response.body().getResults());
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.d("Error", t.getLocalizedMessage());
            }
        });
    }

    //tODO resolve this part
    public void getFavoriteMovies() {
        Log.d(TAG, "getFavoriteMovies called.");
        mRecyclerViewAdapter = new Movie_Adapter(this);

        final FavoriteViewModel viewModel = ViewModelProviders.of(MainActivity.this).get(FavoriteViewModel.class);
        viewModel.loadAllFavorites().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mRecyclerViewAdapter.setMovieList(movies);
            }
        });
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu_main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_popular) {
            callMoviePopular();
            return true;
        } else if (item.getItemId() == R.id.menu_top_rated) {
            callTopRated();
            return true;
        } else if (item.getItemId() == R.id.menu_favorites) {
            getFavoriteMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     * making sure that the connection does not time out when mobile phone is on airplane mode
     **/
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void checkConnection() {
        if (isOnline()) {
            callMoviePopular();
            flag = true;
            Toast.makeText(MainActivity.this, "You are connected to the Internet.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "You are not connected to the Internet. Check Connection Settings and Refresh", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState Called." + flag);
        if (!favorites) {
            outState.putBoolean("favorites", false);
        } else {
            outState.putBoolean("favorites", true);
        }

        state = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("mRecyclerView", state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(state !=null)
        {
            state = savedInstanceState.getParcelable("mRecyclerView");
        }
    }
}
