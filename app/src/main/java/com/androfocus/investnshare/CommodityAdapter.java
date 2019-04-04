package com.androfocus.investnshare;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CommodityAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public CommodityAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    public void add(CommodityData object) {
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
        CommodityAdapter.CommodityDataHolder commodityDataHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.commodity_row_layout,parent,false);
            commodityDataHolder = new CommodityAdapter.CommodityDataHolder();

            commodityDataHolder.ivImage = (ImageView) row.findViewById(R.id.commodityivImage);
            commodityDataHolder.tvTarget1 = (TextView) row.findViewById(R.id.commoditytvTarget1);
            commodityDataHolder.tvTarget2 = (TextView) row.findViewById(R.id.commoditytvTarget2);
            commodityDataHolder.tvType2 = (TextView) row.findViewById(R.id.commoditytvType2);
            commodityDataHolder.tvDate= (TextView) row.findViewById(R.id.commoditytvDate);
            commodityDataHolder.tvName= (TextView) row.findViewById(R.id.commoditytvName);
            commodityDataHolder.tvBuy = (TextView) row.findViewById(R.id.commoditytvBuy);
            commodityDataHolder.tvSell = (TextView) row.findViewById(R.id.commoditytvSell);
            commodityDataHolder.tvType3 = (TextView) row.findViewById(R.id.commoditytvType3);
            commodityDataHolder.tvType4 = (TextView) row.findViewById(R.id.commoditytvType4);
            commodityDataHolder.tvNote = (TextView) row.findViewById(R.id.commoditytvNote);
            commodityDataHolder.llAlert = (LinearLayout) row.findViewById(R.id.llAlert);

            row.setTag(commodityDataHolder);
        }
        else {
            commodityDataHolder = (CommodityAdapter.CommodityDataHolder) row.getTag();
        }
        CommodityData commodityData = (CommodityData) this.getItem(position);

        commodityDataHolder.ivImage.setImageBitmap(commodityData.getImage());
        commodityDataHolder.tvTarget1.setText("@ "+commodityData.getTarget1());
        commodityDataHolder.tvTarget2.setText("@ "+commodityData.getTarget2());
        commodityDataHolder.tvType2.setText(commodityData.getType2());
        commodityDataHolder.tvDate.setText("@ "+commodityData.getDate()+" "+commodityData.getTime());
        commodityDataHolder.tvName.setText(commodityData.getName());
        commodityDataHolder.tvSell.setText("Stoploss @ "+commodityData.getSell());
        commodityDataHolder.tvNote.setText(commodityData.getNote());
        String type2 = commodityData.getType2();

        commodityDataHolder.llAlert.setVisibility(View.VISIBLE);
        commodityDataHolder.tvName.setVisibility(View.VISIBLE);
        if(type2.equals("Buy")){
            commodityDataHolder.tvType2.setBackgroundColor(getContext().getResources().getColor(R.color.BuyColor) );
            commodityDataHolder.tvBuy.setText("Buy @ "+commodityData.getBuy());
            commodityDataHolder.tvBuy.setTextColor(getContext().getResources().getColor(R.color.BuyColor) );
            commodityDataHolder.tvSell.setVisibility(View.VISIBLE);
        }else if(type2.equals("Sell")){
            commodityDataHolder.tvBuy.setText("Sell @ "+commodityData.getBuy());
            commodityDataHolder.tvType2.setBackgroundColor(getContext().getResources().getColor(R.color.SellColor) );
            commodityDataHolder.tvBuy.setTextColor(getContext().getResources().getColor(R.color.SellColor) );
            commodityDataHolder.tvSell.setVisibility(View.VISIBLE);
        }else if(type2.equals("Stop Loss")){
            commodityDataHolder.tvBuy.setText("Stoploss @ "+commodityData.getSell());
            commodityDataHolder.tvBuy.setTextColor(getContext().getResources().getColor(R.color.StopLossColor) );
            commodityDataHolder.tvType2.setBackgroundColor(getContext().getResources().getColor(R.color.StopLossColor) );
            commodityDataHolder.tvSell.setVisibility(View.GONE);
        }else if(type2.equals("Alert")){
            commodityDataHolder.tvType2.setBackgroundColor(getContext().getResources().getColor(R.color.SellColor) );
            commodityDataHolder.llAlert.setVisibility(View.GONE);
            commodityDataHolder.tvName.setVisibility(View.GONE);

        }

        String type3 = commodityData.getType3(), type4=commodityData.getType4();
        commodityDataHolder.tvType3.setText(type3);
        commodityDataHolder.tvType4.setText(type4);

        if(type4.equals("Book Profit")||type4.equals("Target Met")){
            commodityDataHolder.tvType4.setTextColor(getContext().getResources().getColor(R.color.BuyColor) );
        }else if(type4.equals("Stop Loss Hit")||type4.equals("Exit")){
            commodityDataHolder.tvType4.setTextColor(getContext().getResources().getColor(R.color.SellColor) );
        }

        return row;
    }
    static class  CommodityDataHolder
    {
        TextView  tvTarget1, tvTarget2, tvType2,tvType3, tvType4,tvDate,tvName,tvBuy, tvSell,tvNote;
        ImageView ivImage;
        LinearLayout llAlert;
    }



}
