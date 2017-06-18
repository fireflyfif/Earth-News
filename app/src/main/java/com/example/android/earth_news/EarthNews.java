package com.example.android.earth_news;

/**
 * Created by iva on 6/13/17.
 */

public class EarthNews {

    private String mWebTitle;

    private String mSectionName;

    private String mWebDate;

    private String mSummary;

    private String mAuthor;

    private String mUrl;

    private String mThumbnail;


    /**
     * Create a new EarthNews object.
     *
     * @param webTitle is the title of the news
     * @param sectionName is the name of the section of the news
     * @param webDate is the date when the news is posted
     * @param url is the url of the news
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

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getWebDate() {
        return mWebDate;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getThumbnail() {
        return mThumbnail;
    }
}
