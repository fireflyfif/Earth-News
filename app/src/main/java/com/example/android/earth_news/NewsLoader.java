package com.example.android.earth_news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of news by using an AsyncTask to perform
 * the new request to the given URL.
 */

public class NewsLoader extends AsyncTaskLoader<List<EarthNews>> {

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructor a new {@link NewsLoader}
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<EarthNews> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news
        List<EarthNews> earthNews = QueryUtils.fetchNewsData(mUrl);
        return earthNews;
    }
}
