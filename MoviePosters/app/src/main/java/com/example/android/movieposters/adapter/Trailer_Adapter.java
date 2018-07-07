package com.example.android.movieposters.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieposters.BuildConfig;
import com.example.android.movieposters.R;
import com.example.android.movieposters.object.Trailer;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class Trailer_Adapter extends RecyclerView.Adapter<Trailer_Adapter.ViewHolder> {

    private Context mContext;
    private List<Trailer> trailerList;

    public Trailer_Adapter(Context mContext, List<Trailer> trailerList) {
        this.mContext = mContext;
        this.trailerList = trailerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_trailer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Trailer trailer = trailerList.get(position);
        holder.title.setText(trailerList.get(position).getName());

        Picasso.with(mContext)
                .load(R.drawable.youtube)
                .placeholder(R.color.colorPrimaryDark)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            VideoLinkBuilder.buildVideoUrl(trailer.getKey())));
                    Toast.makeText(v.getContext(), "You clicked " + trailer.getName(), Toast.LENGTH_SHORT).show();
                  /*if (position != RecyclerView.NO_POSITION) {
                    Trailer clickedDataItem = trailerList.get(position);
                    String videoId = trailerList.get(position).getKey();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("VIDEO_ID", videoId);
                    mContext.startActivity(intent);*/

                }
            });
      //  });
    }

    public static class VideoLinkBuilder{
        public static final String BASE = "www.youtube.com";
        public static final String BASE_SCHEME ="https";
        public static final String BASE_PATH = "watch";
        public static final String VIDEO_QUERY = "v";

        public static Uri buildVideoUrl(String mKey) {
            return (new Uri.Builder())
                    .scheme(BASE_SCHEME)
                    .authority(BASE)
                    .appendPath(BASE_PATH)
                    .appendQueryParameter(VIDEO_QUERY, mKey)
                    .build();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title;

        public ViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_trailer);
            imageView = (ImageView) itemView.findViewById(R.id.youtube);
        }
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList.clear();
        this.trailerList.addAll(trailerList);
        notifyDataSetChanged();
    }


    public static class LoggingInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            HttpUrl url = chain.request().url()
                    .newBuilder()
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
