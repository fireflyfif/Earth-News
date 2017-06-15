package com.example.android.earth_news;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iva on 6/14/17.
 */

public class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<EarthNews> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList to add news to
        List<EarthNews> newsList = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            JSONObject newsObject = baseJsonResponse.getJSONObject("response");

            JSONArray resultsArray = newsObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                // Get a single news and position it within the list of news
                JSONObject currentNews = resultsArray.getJSONObject(i);

                // Extract the value from the key called "sectionName"
                String sectionName = currentNews.getString("sectionName");

                // Extract the value from the key called "webTitle"
                String webTitle = currentNews.getString("webTitle");

                // Extract the value from the key called "webPublicationDate"
                String webDate = currentNews.getString("webPublicationDate");

                // Extract the value from the key called "webUrl"
                String webUrl = currentNews.getString("webUrl");

                // TODO extract the thumbnail from JSONObject "fields"

                JSONObject newsFields = currentNews.getJSONObject("fields");

                // Extract the value from the key called "trailText"
                // This is the summary of the news
                String summary = newsFields.getString("trailText");

                // Extract the value for authors
                JSONArray authorsArray;
                String authors = "";
                String firstName, lastName;
                if (currentNews.has("tags")) {
                    authorsArray = currentNews.getJSONArray("tags");
                    if (authorsArray.length() != 0) {
                        for (int n = 0; n < authorsArray.length(); n++) {
                            JSONObject authorObject = authorsArray.getJSONObject(n);
                            if (authorObject.has("firstName")) {
                                firstName = authorObject.getString("firstName");
                            } else
                                firstName = "";
                            if (authorObject.has("lastName"))
                                lastName = authorObject.getString("lastName");
                            else
                                lastName = "";
                            authors = firstName + " " + lastName;
                        }
                    } else
                        authors = "Unknown Author";
                } else
                    authors = "Unknown Author";

                // Create a new {@link EarthNews} object with the webTitle, sectionName,
                // webDate, summary and authors from the JSON response.
                EarthNews earthNewsObject = new EarthNews(webTitle, sectionName, webDate, summary,
                        authors, webUrl);

                // Add the new {@link EarthNews} to the list of news.
                newsList.add(earthNewsObject);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return newsList;
    }

    /**
     * Query the Guardian API and return a list of{@link EarthNews} object.
     */
    public static List<EarthNews> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        // Return the list of {@link EarthNews}
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error building the URL", e);
        }
        return url;
    }

    /**
     * Make HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            // If the request was successful (response code 200)
            // then read the input stream and parse the response
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.e(LOG_TAG, "Code of the URL " + urlConnection.getResponseCode());
            } else {
                Log.e(LOG_TAG, "Response code of the object: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON result: ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the whole JSON response
     * from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                result.append(line);
                line = reader.readLine();
            }
        }
        return result.toString();
    }
}

