package com.example.android.movieposters.object;

import java.util.List;

public class ReviewList {

    /**
     * id : 100
     * page : 1
     * results : [{"author":"BradFlix","content":"I just plain love this movie!","id":"529bc23719c2957215011e7b","url":"https://www.themoviedb.org/review/529bc23719c2957215011e7b"},{"author":"Andres Gomez","content":"Far from being a good movie, with tons of flaws but already pointing to the pattern of the whole Ritchie's filmography.","id":"535856c30e0a26069400064c","url":"https://www.themoviedb.org/review/535856c30e0a26069400064c"},{"author":"David Perkins","content":"Genuinely one of my favorite movies of all time. Watched again recently and realised how well written, brilliantly shot, beautifully cast and cleverly produced this movie actually is!","id":"5873810f9251410e71009a68","url":"https://www.themoviedb.org/review/5873810f9251410e71009a68"}]
     * total_pages : 1
     * total_results : 3
     */

    private List<Review> results;

    public List<Review> getResults() {
        return results;
    }

}
