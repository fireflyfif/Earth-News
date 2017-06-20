package com.example.android.earth_news;

/**
 * Created by iva on 6/13/17.
 */

public class EarthNews {

    /**
     * Title of the news
     */
    private String mWebTitle;

    /**
     * Section name of the news
     */
    private String mSectionName;

    /**
     * Date of the news
     */
    private String mWebDate;

    /**
     * Summary of the news
     */
    private String mSummary;

    /**
     * Author of the news
     */
    private String mAuthor;

    /**
     * URL of the news
     */
    private String mUrl;

    /**
     * Thumbnail of the news
     */
    private String mThumbnail;


    /**
     * Create a new EarthNews object.
     *
     * @param webTitle    is the title of the news
     * @param sectionName is the name of the section of the news
     * @param webDate     is the date when the news is posted
     * @param url         is the url of the news
     */
    public EarthNews(String webTitle, String sectionName, String webDate, String summary,
                     String author, String url, String thumbnail) {
        mWebTitle = webTitle;
        mSectionName = sectionName;
        mWebDate = webDate;
        mSummary = summary;
        mAuthor = author;
        mUrl = url;
        mThumbnail = thumbnail;
    }

    /**
     * Get the title of the news
     */
    public String getWebTitle() {
        return mWebTitle;
    }

    /**
     * Get the section name of the news
     */
    public String getSectionName() {
        return mSectionName;
    }

    /**
     * Get the web date of the news
     */
    public String getWebDate() {
        return mWebDate;
    }

    /**
     * Get the summary of the news
     */
    public String getSummary() {
        return mSummary;
    }

    /**
     * Get the author of the news
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Get the URL of the news
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Get the thumbnail of the news
     */
    public String getThumbnail() {
        return mThumbnail;
    }
}
