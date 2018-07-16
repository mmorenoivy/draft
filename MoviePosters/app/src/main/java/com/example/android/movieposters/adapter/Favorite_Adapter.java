package com.example.android.movieposters.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieposters.R;
import com.example.android.movieposters.data.FavoriteEntity;
import com.example.android.movieposters.ui.MovieDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Favorite_Adapter extends RecyclerView.Adapter<Favorite_Adapter.ViewHolder> {
    private List<FavoriteEntity> favoriteList;
    private Context mContext;
    private static final String TAG = "Favorite Adapter";

    public Favorite_Adapter(Context mContext, List<FavoriteEntity> favoriteList)
    {
        this.favoriteList = favoriteList;
        this.mContext = mContext;
    }

    @Override
    public Favorite_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_favorites, null);
        Favorite_Adapter.ViewHolder viewHolder = new Favorite_Adapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Favorite_Adapter.ViewHolder holder, int position) {
        final FavoriteEntity movie = favoriteList.get(position);

        if (!TextUtils.isEmpty(movie.getMoviePoster())) {
            Log.d(TAG, "onBindViewHolder called");

            Picasso.with(mContext)
                    .load(movie.getMoviePoster())
                    .resize(185, 277)
                    .placeholder(R.color.colorPrimaryDark)
                    .into(holder.imageView);
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieDetails.class);
                intent.putExtra("poster", movie.getMoviePoster());
                intent.putExtra("hero_backdrop", movie.getMovieHero());
                intent.putExtra("original_title", movie.getMovieName());
                intent.putExtra("overview", movie.getMovieOverview());
                intent.putExtra("vote_average", movie.getUserRating());
                intent.putExtra("release_date", movie.getReleaseDate());
                intent.putExtra("movie_id", movie.getMovieId());
                mContext.startActivity(intent);
            }
        };
        holder.imageView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return (null != favoriteList ? favoriteList.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView imageView;

        public ViewHolder(View view)
        {
            super(view);
            this.imageView = view.findViewById(R.id.poster);
        }
    }
}
