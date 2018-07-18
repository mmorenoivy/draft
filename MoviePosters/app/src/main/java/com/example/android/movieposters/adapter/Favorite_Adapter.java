package com.example.android.movieposters.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieposters.R;
import com.example.android.movieposters.object.Movie;
import com.example.android.movieposters.ui.MovieDetails;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class Favorite_Adapter extends RecyclerView.Adapter<Favorite_Adapter.ViewHolder> {
    private List<Movie> favoriteList;
    private Context mContext;
    private static final String TAG = "Favorite Adapter";
    public static final String TMDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w500";

    public Favorite_Adapter(Context mContext) {
        this.favoriteList = favoriteList;
        this.mContext = mContext;
    }

    @Override
    public Favorite_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_image, null);
        Favorite_Adapter.ViewHolder viewHolder = new Favorite_Adapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Favorite_Adapter.ViewHolder holder, final int position) {
       final Movie movie = favoriteList.get(position);

        Log.d(TAG, "onBindViewHolder called");

        Picasso.with(mContext)
                .load(TMDB_IMAGE_PATH + movie.getBackdrop_path())
                .placeholder(R.color.colorPrimaryDark)
                .into(holder.imageView);



        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, MovieDetails.class);

           /*
                intent.putExtra("poster", movie.getMoviePoster());
                intent.putExtra("hero_backdrop", movie.getMovieHero());
                intent.putExtra("original_title", movie.getMovieName());
                intent.putExtra("overview", movie.getMovieOverview());
                intent.putExtra("vote_average", movie.getUserRating());
                intent.putExtra("release_date", movie.getReleaseDate());*/
                mContext.startActivity(intent);
            }
        };
        holder.imageView.setOnClickListener(listener);
        /*
       holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mContext = view.getContext();
                Movie clickedMovie = favoriteList.get(position);
                Intent intent = new Intent(mContext, MovieDetails.class);
                intent.putExtra("movies", clickedMovie);
                mContext.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return (null != favoriteList ? favoriteList.size() : 0);
    }

   public static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.imageView);
        }
    }

}
