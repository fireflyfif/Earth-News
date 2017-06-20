package com.example.android.earth_news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    /**
     * The Guardian Base URL
     */
    private static final String BASE_NEWS_URL = "http://content.guardianapis.com/search?q=";

    /**
     * The key word for the query
     */
    private static final String URL_SUBJECT = "environment";

    /**
     * The Api Key test need for the query
     */
    private static final String GUARDIAN_TEST_KEY = "test";

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = EarthNewsActivity.class.getName();

    private RecyclerView recyclerView;
    private EarthNewsAdapter mAdapter;

    private TextView emptyView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_news);

        Log.e(LOG_TAG, "onCreate is called");

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                              @Override
                                              public void onRefresh() {
                                                  mAdapter = new EarthNewsAdapter(getApplicationContext(),
                                                          new ArrayList<EarthNews>());
                                                  recyclerView.setAdapter(mAdapter);
                                              }
                                          });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        List<EarthNews> newsList = new ArrayList<>();
        mAdapter = new EarthNewsAdapter(this, newsList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        emptyView = (TextView) findViewById(R.id.text_no_news);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Get a reference to the ConnectivityManager to check of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.e(LOG_TAG, "There is an internet connection.");
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null
            // for the bundle. Pass in this activity for the LoaderCallbacks parameter
            // (which is valid because this activity implements the LoadersCallbacks interface.)
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            Log.e(LOG_TAG, "There is No internet connection.");
            // Otherwise, display error
            // First, hide loading indicator so error will be visible
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<List<EarthNews>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String showNews = sharedPrefs.getString(
                getString(R.string.settings_showNews_key),
                getString(R.string.settings_showNews_default)
        );

        String query = BASE_NEWS_URL + URL_SUBJECT;
        Uri baseUri = Uri.parse(query);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("show-fields", "all");
        uriBuilder.appendQueryParameter("page-size", "15");
        uriBuilder.appendQueryParameter("order-by", showNews);
        uriBuilder.appendQueryParameter("api-key", GUARDIAN_TEST_KEY);
        Log.e(LOG_TAG, "What is the current URL " + uriBuilder);

        // Create a new loader for the given URL
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<EarthNews>> loader, List<EarthNews> data) {
        Log.e(LOG_TAG, "onLoadFinished is called");

        // Set these views to invisible
        progressBar.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        swipeRefresh.setRefreshing(false);

        mAdapter = new EarthNewsAdapter(this, data);
        recyclerView.setAdapter(mAdapter);

        if (data != null && !data.isEmpty()) {
            mAdapter = new EarthNewsAdapter(this, data);
            recyclerView.setVisibility(View.VISIBLE);
            Log.e(LOG_TAG, "Where is the recyclerView?");
        } else {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(R.string.no_news_found);
            Log.e(LOG_TAG, "Why is the list null?");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<EarthNews>> loader) {
        Log.e(LOG_TAG, "onLoadReset is called");
        mAdapter = new EarthNewsAdapter(this, new ArrayList<EarthNews>());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
