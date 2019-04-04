package com.androfocus.investnshare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public NewsFeedAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    public void add(NewsFeedData object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row = convertView;
        NewsFeedDataHolder newsFeedDataHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.news_feed_row_layout,parent,false);
            newsFeedDataHolder = new NewsFeedDataHolder();

            newsFeedDataHolder.tvNewsFeedTitle = (TextView) row.findViewById(R.id.tvNewsFeedTitle);
            newsFeedDataHolder.tvNewsFeedContent = (TextView) row.findViewById(R.id.tvNewsFeedContent);
            newsFeedDataHolder.ivNewsFeedImage = (ImageView) row.findViewById(R.id.ivNewsFeedImage);
            newsFeedDataHolder.tvNewsFeedLink = (TextView) row.findViewById(R.id.tvNewsFeedLink);
            newsFeedDataHolder.tvNewsFeedDate = (TextView) row.findViewById(R.id.tvNewsFeedDate);
            row.setTag(newsFeedDataHolder);
        }
        else {
            newsFeedDataHolder = (NewsFeedDataHolder) row.getTag();
        }
        NewsFeedData newsFeedData = (NewsFeedData) this.getItem(position);

        newsFeedDataHolder.tvNewsFeedTitle.setText(newsFeedData.getTitle());
        newsFeedDataHolder.tvNewsFeedContent.setText(newsFeedData.getContent());
        newsFeedDataHolder.ivNewsFeedImage.setImageBitmap(newsFeedData.getImage());
        //newsFeedDataHolder.tvNewsFeedLink.setText(newsFeedData.getLink());

        newsFeedDataHolder.tvNewsFeedDate.setText("Posted @ "+newsFeedData.getDate());

        final String link = newsFeedData.getLink();
        newsFeedDataHolder.tvNewsFeedLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Uri uri = Uri.parse(link); // missing 'http://' will cause crashed
                //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                Intent intent = new Intent(getContext(),WebViewActivity.class);
                intent.putExtra("Link",link );
                intent.putExtra("NewsFeed","NewsFeedActivity" );
                getContext().startActivity(intent);
            }
        });
        return row;
    }
    static class  NewsFeedDataHolder
    {
        TextView tvNewsFeedTitle,tvNewsFeedContent, tvNewsFeedLink, tvNewsFeedDate;
        ImageView ivNewsFeedImage;
    }



}
