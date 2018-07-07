package com.example.android.movieposters.api;

import com.example.android.movieposters.object.MovieList;
import com.example.android.movieposters.object.ReviewList;
import com.example.android.movieposters.object.TrailerList;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

//TODO 4 Create an API to correspond with an endpoint of the REST API
//we reference the interface with the following API calls - @POST, @UPDATE, @PATCH, @DELETE, and @GET requests
// this is where the retrofit comes in

public interface MovieAPI {

    @GET("movie/popular")
    Call<MovieList> getMoviePopular();
    @GET("movie/top_rated")
    Call<MovieList> getMovieTopRated();
    @GET("movie/{movie_id}/videos")
    Call<TrailerList> getVideos(@Path("movie_id") int id, @Query("api_key") String apiKey
    ,@Query("language") String language);
    @GET("movie/{movie_id}/reviews")
    Call<ReviewList> getReviews(@Path("movie_id") int id, @Query("api_key") String apiKey
            , @Query("language") String language);
}
