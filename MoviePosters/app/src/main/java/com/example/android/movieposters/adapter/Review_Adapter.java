package com.example.android.movieposters.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieposters.R;
import com.example.android.movieposters.object.Review;


import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class Review_Adapter extends RecyclerView.Adapter<Review_Adapter.ViewHolder> {
    private Context mContext;
    private List<Review> reviewList;

    public Review_Adapter(Context mContext, List<Review> reviewList) {
        this.mContext = mContext;
        this.reviewList = reviewList;
    }

    @Override
    public Review_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_review, parent, false);
        return new Review_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Review_Adapter.ViewHolder holder, final int position) {
        final Review review = reviewList.get(position);
        holder.author.setText(reviewList.get(position).getAuthor() + " :");
        holder.reviews.setText("\" " + reviewList.get(position).getContent() + "\"");

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView reviews;
        public TextView author;

        public ViewHolder(final View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author);
            reviews = (TextView) itemView.findViewById(R.id.reviews);
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList.clear();
        this.reviewList.addAll(reviewList);
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
