package com.example.android.movieposters.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieposters.BuildConfig;
import com.example.android.movieposters.R;
import com.example.android.movieposters.object.Movie;
import com.example.android.movieposters.ui.MovieDetailsActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**Reference:
 * https://futurestud.io/tutorials/retrofit-2-how-to-add-query-parameters-to-every-request
 * **/

public class Movie_Adapter extends RecyclerView.Adapter<Movie_Adapter.ViewHolder> {
    private List<Movie> mMovieList;

    private LayoutInflater mInflater;
    private Context mContext;

    public static final String TMDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w500";

    //This constructor is responsible for passing values to the list
    public Movie_Adapter(Context context) { //, String[] values2
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMovieList = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movie_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Movie movie = mMovieList.get(position);

        // This is how to use Picasso to load images from the internet.
        Picasso.with(mContext)

                .load(TMDB_IMAGE_PATH + movie.getPoster_path())
                .placeholder(R.color.colorPrimaryDark)
                .into(holder.imageView);

        //Click on a specific poster and it should call the intent
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext = view.getContext();
                Movie clickedMovie = mMovieList.get(position);
                Intent intent = new Intent(mContext, MovieDetailsActivity.class);
                intent.putExtra("movies", clickedMovie);
                mContext.startActivity(intent);

               // Toast.makeText(mContext, clickedMovie + "Movie is clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return (mMovieList == null) ? 0 : mMovieList.size();
    }

    public void setMovieList(List<Movie> movieList)
    {
        this.mMovieList.clear();
        this.mMovieList.addAll(movieList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }


    public static class LoggingInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            HttpUrl url = chain.request().url()
                    .newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TMDB_API)
                    .build();

            Request request = chain.request().newBuilder().url(url).build();

            long t1 = System.nanoTime();
            String requestLog = String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers());

 /*          if(request.method().compareToIgnoreCase("post")==0){
                requestLog ="\n" + requestLog + "\n" + bodyToString(request);
            }
            Log.d("TAG","request" + "\n" + requestLog);*/

            okhttp3.Response response = chain.proceed(request);
            long t2 = System.nanoTime();

            String responseLog = String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers());

            String bodyString = response.body().string();

            Log.d("TAG","response" + "\n" + responseLog + "\n" + bodyString);

            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), bodyString))
                    .build();

        }
    }


}
