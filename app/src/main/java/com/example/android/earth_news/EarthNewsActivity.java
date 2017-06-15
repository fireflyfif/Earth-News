package com.example.android.earth_news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthNewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<EarthNews>> {

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    private static final String EARTH_NEWS_URL =
            "http://content.guardianapis.com/search?show-tags=contributor&show-references=all&show-fields=all&q=politics&api-key=test";

    private static final String LOG_TAG = EarthNewsActivity.class.getName();

    private List<EarthNews> newsList;
    private RecyclerView recyclerView;
    private EarthNewsAdapter mAdapter;

    private TextView emptyView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_news);

        Log.e(LOG_TAG, "onCreate is called");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        newsList = new ArrayList<>();
        mAdapter = new EarthNewsAdapter(this, newsList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        emptyView = (TextView) findViewById(R.id.text_no_news);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);


        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.e(LOG_TAG, "There is an internet connection.");
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            Log.e(LOG_TAG, "There is No internet connection.");
            progressBar.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("No Internet Connection");
        }
    }


    @Override
    public Loader<List<EarthNews>> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG, "What is the URL " + EARTH_NEWS_URL);

        return new NewsLoader(this, EARTH_NEWS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<EarthNews>> loader, List<EarthNews> data) {
        Log.e(LOG_TAG, "onLoadFinished is called");

        progressBar.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);


        mAdapter = new EarthNewsAdapter(this, new ArrayList<EarthNews>());

        if (newsList != null && !newsList.isEmpty()) {
            mAdapter = new EarthNewsAdapter(this, newsList);
            recyclerView.setVisibility(View.VISIBLE);
            Log.e(LOG_TAG, "Where is the recyclerView?");
        } else {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("No News Found");
            Log.e(LOG_TAG, "Is it really null?");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<EarthNews>> loader) {
        Log.e(LOG_TAG, "onLoadReset is called");
        mAdapter = new EarthNewsAdapter(this, new ArrayList<EarthNews>());
    }
}
