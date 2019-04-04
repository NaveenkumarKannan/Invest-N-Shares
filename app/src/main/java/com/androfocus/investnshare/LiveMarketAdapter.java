package com.androfocus.investnshare;

import android.content.Context;
import android.content.Intent;
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

public class LiveMarketAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public LiveMarketAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    public void add(LiveMarketData object) {
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
        LiveMarketDataHolder liveMarketDataHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.live_market_row_layout,parent,false);
            liveMarketDataHolder = new LiveMarketDataHolder();

            liveMarketDataHolder.tvLiveMarketTitle = (TextView) row.findViewById(R.id.tvLiveMarketTitle);
            liveMarketDataHolder.tvLiveMarketContent = (TextView) row.findViewById(R.id.tvLiveMarketContent);
            liveMarketDataHolder.tvLiveMarketDate = (TextView) row.findViewById(R.id.tvLiveMarketDate);
            row.setTag(liveMarketDataHolder);
        }
        else {
            liveMarketDataHolder = (LiveMarketDataHolder) row.getTag();
        }
        LiveMarketData liveMarketData = (LiveMarketData) this.getItem(position);

        liveMarketDataHolder.tvLiveMarketTitle.setText(liveMarketData.getTitle());
        liveMarketDataHolder.tvLiveMarketContent.setText(liveMarketData.getContent());
        liveMarketDataHolder.tvLiveMarketDate.setText("Posted @ "+liveMarketData.getDate());
        
        return row;
    }
    static class  LiveMarketDataHolder
    {
        TextView tvLiveMarketTitle,tvLiveMarketContent, tvLiveMarketDate;
    }



}
