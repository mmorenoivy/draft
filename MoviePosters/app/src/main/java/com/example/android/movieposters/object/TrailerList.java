package com.example.android.movieposters.object;

import java.util.List;

public class TrailerList {
    private int trailer_id;
    private List<Trailer> results;

    public int getTrailer_id()
    {
        return trailer_id;
    }

    public void setTrailer_id(int trailer_id){
        this.trailer_id = trailer_id;
    }

    public List<Trailer> getResults(){
        return results;
    }
}
