package com.example.android.movieposters.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**TODO: Create an entity
 * https://codelabs.developers.google.com/codelabs/android-room-with-a-view/
 * **/

@Entity(tableName = "Favorite")
public class FavoriteEntity {

    @NonNull
    @PrimaryKey
    private int movieId;
    @ColumnInfo(name = "moviePoster")
    private String moviePoster;
    @ColumnInfo(name = "movieHero")
    private String movieHero;
    @ColumnInfo(name = "movieName")
    private String movieName;
    @ColumnInfo(name = "userRating")
    private String userRating;
    @ColumnInfo(name = "releaseDate")
    private String releaseDate;
    @ColumnInfo(name = "movieOverview")
    private String movieOverview;


    public FavoriteEntity(@NonNull int movieId, String moviePoster,
                          String releaseDate, String userRating,
                          String movieOverview, String movieHero, String movieName) {

        this.movieId = movieId;
        this.moviePoster = moviePoster;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
        this.movieHero = movieHero;
        this.movieOverview = movieOverview;
        this.movieName = movieName;
    }

    @Ignore
    public FavoriteEntity() {

    }

    @NonNull
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull int movieId) {
        this.movieId = movieId;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getMovieHero() {
        return movieHero;
    }

    public void setMovieHero(String movieHero) {
        this.movieHero = movieHero;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

}
