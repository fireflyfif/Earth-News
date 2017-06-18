package com.example.android.earth_news;

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

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by iva on 6/14/17.
 */

public class EarthNewsAdapter extends RecyclerView.Adapter<EarthNewsAdapter.ViewHolder> {

    private static final String LOG_TAG = EarthNewsActivity.class.getName();

    private List<EarthNews> mNewsList;
    private Context mContext;

    public EarthNewsAdapter(Context context, List<EarthNews> earthNews) {
        mContext = context;
        mNewsList = earthNews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EarthNewsAdapter.ViewHolder holder, int position) {
        final EarthNews news = mNewsList.get(position);
        holder.title.setText(news.getWebTitle());
        holder.section.setText(news.getSectionName());
        holder.date.setText(news.getWebDate());
        holder.author.setText(news.getAuthor());
        holder.summary.setText(news.getSummary());
        holder.summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(news.getUrl()));
                mContext.startActivity(intent);
            }
        });

        if (holder.thumbnail == null) {
            holder.thumbnail = new ImageView(mContext);
        }

        holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(mContext)
                .load(news.getThumbnail())
                .placeholder(R.drawable.librarian_book_01)
                .error(R.drawable.librarian_book_01)
                .into(holder.thumbnail, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e(LOG_TAG, "Loading of the image is successful...");
                    }

                    @Override
                    public void onError() {
                        Log.e(LOG_TAG, "Loading of the image has error...");
                    }
                });


    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView section;
        public TextView date;
        public TextView author;
        public TextView summary;
        public ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.news_title);
            section = (TextView) view.findViewById(R.id.section_name);
            date = (TextView) view.findViewById(R.id.news_date);
            author = (TextView) view.findViewById(R.id.author_name);
            summary = (TextView) view.findViewById(R.id.trail_text);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail_news);
        }
    }
}
