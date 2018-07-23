package com.example.android.movieposters.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieposters.BuildConfig;
import com.example.android.movieposters.adapter.Review_Adapter;
import com.example.android.movieposters.adapter.Trailer_Adapter;
import com.example.android.movieposters.api.MovieAPI;
import com.example.android.movieposters.data.FavoriteViewModel;
import com.example.android.movieposters.object.Movie;
import com.example.android.movieposters.R;
import com.example.android.movieposters.adapter.Movie_Adapter;
import com.example.android.movieposters.object.Review;
import com.example.android.movieposters.object.ReviewList;
import com.example.android.movieposters.object.Trailer;
import com.example.android.movieposters.object.TrailerList;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//https://www.coderefer.com/android-parcelable-example/

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String TAG = "MovieDetailsActivity";

    public String MOVIE = "movies";

    public static final String TMDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w342";
    public static final String TMDB_BACKDROP_PATH = "http://image.tmdb.org/t/p/original";


    private Movie mMovie;
    private Movie_Adapter movie_adapter;
    private RecyclerView recyclerViewTrailer;
    private RecyclerView recyclerViewReview;
    private Trailer_Adapter trailer_adapter;
    private List<Trailer> trailers;
    private Review_Adapter review_adapter;
    private List<Review> reviews;
    private FavoriteViewModel favoriteViewModel;


    ImageView poster, hero;
    TextView mTitle;
    TextView mOverview;
    TextView mReleaseDate;
    TextView mUserRating;
    TextView mTextRating;
    RatingBar ratingBar;

    private final AppCompatActivity activity = MovieDetailsActivity.this;

    String hero_poster, thumbnail, movieName, movieDescription, userRating, releaseDate;
    int movieId;
    final static String MOVIE_URL = "https://api.themoviedb.org/3/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        favoriteViewModel = ViewModelProviders.of(MovieDetailsActivity.this).get(FavoriteViewModel.class);
        mTitle = (TextView) findViewById(R.id.title);
        poster = (ImageView) findViewById(R.id.hero_poster);
        hero = (ImageView) findViewById(R.id.poster);
        mOverview = (TextView) findViewById(R.id.overview);

        //Convert the rating into a rating bar
        ratingBar = (RatingBar) findViewById(R.id.rating_Bar);
        mTextRating = (TextView) findViewById(R.id.tv_rating);
        mUserRating = (TextView) findViewById(R.id.rating);
        mReleaseDate = (TextView) findViewById(R.id.release_date);


        Intent intentStartDetails = getIntent();
        if (intentStartDetails.hasExtra(MOVIE)) {


            mMovie = getIntent().getParcelableExtra(MOVIE);

            movieId = mMovie.getId();
            movieName = mMovie.getOriginal_title();
            hero_poster = mMovie.getBackdrop_path();
            thumbnail = mMovie.getPoster_path();
            movieDescription = mMovie.getOverview();
            userRating = mMovie.getVote_average();
            releaseDate = mMovie.getRelease_date();

            Picasso.with(this)
                    .load(TMDB_IMAGE_PATH + mMovie.getPoster_path())
                    .placeholder(R.color.colorPrimaryDark)
                    .into(hero);

            Picasso.with(this)
                    .load(TMDB_BACKDROP_PATH + mMovie.getBackdrop_path())
                    .placeholder(R.color.colorPrimaryDark)
                    .into(poster);

            mTitle.setText(movieName);
            mOverview.setText(movieDescription);

            mTextRating.setText("Rating: ");
            ratingBar.setRating(Float.valueOf(userRating) / 2);
            mUserRating.setText(userRating);
            mReleaseDate.setText(releaseDate);
        } else {
            Toast.makeText(this, "No Movie Details Available", Toast.LENGTH_SHORT).show();
        }


        SpeedDialView button = findViewById(R.id.speedDialFavorite);
        button.addActionItem(new SpeedDialActionItem.Builder(R.id.add_favorite, R.drawable
                .heart)
                .setLabel("Add Favorite")
                .setLabelClickable(true)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()))
                .create());

        button.addActionItem(
                new SpeedDialActionItem.Builder(R.id.delete_favorite, R.drawable
                        .ic_favorite_border_black_24dp)
                        .setLabel("Remove Favorite")
                        .setLabelClickable(true)
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()))
                        .create());

        button.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {

            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {

                switch (speedDialActionItem.getId()) {
                    case R.id.add_favorite:

                        favoriteViewModel.loadMovieById(movieId).observe(MovieDetailsActivity.this, new Observer<Movie>() {

                            @Override
                            public void onChanged(@Nullable Movie favMovie) {
                                if (favMovie != null) {
                                    Log.d(TAG, "onChanged: " + favMovie.getId());
                                    mMovie = favMovie;
                                } else {
                                    Log.d(TAG, "onChanged: Null");
                                }
                            }
                        });

                        favoriteViewModel.addFavoriteMovie(mMovie);

                        return false;
                    case R.id.delete_favorite:

                        favoriteViewModel.loadMovieById(movieId).observe(MovieDetailsActivity.this, new Observer<Movie>() {

                            @Override
                            public void onChanged(@Nullable Movie favMovie) {
                                if (favMovie != null) {
                                    Log.d(TAG, "onChanged: " + mMovie.getId());
                                    mMovie = favMovie;
                                } else {
                                    Log.d(TAG, "onChanged: Null");
                                }
                            }
                        });

                        favoriteViewModel.removeFavoriteMovie(mMovie);
                        return false;
                    default:
                        return false;
                }
            }
        });


        trailerViews();
        reviewViews();
    }

    private void trailerViews() {
        trailers = new ArrayList<>();
        trailer_adapter = new Trailer_Adapter(this, trailers);

        recyclerViewTrailer = findViewById(R.id.trailer_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewTrailer.setLayoutManager(mLayoutManager);
        recyclerViewTrailer.setAdapter(trailer_adapter);

        loadTrailer();

    }

    private void loadTrailer() {
        try {
            if (BuildConfig.TMDB_API.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter your API Key in gradle.properties", Toast.LENGTH_SHORT).show();
                return;
            }

            final OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Movie_Adapter.LoggingInterceptor())
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieAPI requestService = retrofit.create(MovieAPI.class);


            Call<TrailerList> call = requestService.getVideos(movieId, BuildConfig.TMDB_API, "en-US");
            call.enqueue(new Callback<TrailerList>() {
                @Override
                public void onResponse(Call<TrailerList> call, Response<TrailerList> response) {
                    if (response.isSuccessful()) {
                        List<Trailer> trailer = response.body().getResults();
                        trailer_adapter.setTrailerList(trailer);
                    } else {
                        call.request().url().toString();
                    }
                }

                @Override
                public void onFailure(Call<TrailerList> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MovieDetailsActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void reviewViews() {
        reviews = new ArrayList<>();
        review_adapter = new Review_Adapter(this, reviews);

        recyclerViewReview = (RecyclerView) findViewById(R.id.review_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewReview.setLayoutManager(mLayoutManager);
        recyclerViewReview.setAdapter(review_adapter);

        loadReview();

    }

    private void loadReview() {
        try {
            if (BuildConfig.TMDB_API.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter your API Key in gradle.properties", Toast.LENGTH_SHORT).show();
                return;
            }

            final OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Movie_Adapter.LoggingInterceptor())
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieAPI requestService = retrofit.create(MovieAPI.class);


            Call<ReviewList> call = requestService.getReviews(movieId, BuildConfig.TMDB_API, "en-US");
            call.enqueue(new Callback<ReviewList>() {
                @Override
                public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                    if (response.isSuccessful()) {
                        List<Review> review = response.body().getResults();
                        review_adapter.setReviewList(review);
                    } else {
                        call.request().url().toString();
                    }
                }

                @Override
                public void onFailure(Call<ReviewList> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MovieDetailsActivity.this, "Error fetching reviews", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
