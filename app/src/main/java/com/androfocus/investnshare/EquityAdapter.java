package com.androfocus.investnshare;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EquityAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public EquityAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    public void add(EquityData object) {
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
        EquityDataHolder equityDataHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.equity_row_layout,parent,false);
            equityDataHolder = new EquityDataHolder();

            equityDataHolder.tvCompanyName = (TextView) row.findViewById(R.id.tvCompanyName);
            equityDataHolder.ivImage = (ImageView) row.findViewById(R.id.ivImage);
            equityDataHolder.tvBuy = (TextView) row.findViewById(R.id.tvBuy);
            equityDataHolder.tvSell = (TextView) row.findViewById(R.id.tvSell);
            equityDataHolder.tvTarget1 = (TextView) row.findViewById(R.id.tvTarget1);
            equityDataHolder.tvTarget2 = (TextView) row.findViewById(R.id.tvTarget2);
            equityDataHolder.tvTarget3 = (TextView) row.findViewById(R.id.tvTarget3);
            equityDataHolder.tvType2 = (TextView) row.findViewById(R.id.tvType2);
            equityDataHolder.tvType3 = (TextView) row.findViewById(R.id.tvType3);
            equityDataHolder.tvType4 = (TextView) row.findViewById(R.id.tvType4);
            equityDataHolder.tvNote = (TextView) row.findViewById(R.id.tvNote);
            equityDataHolder.tvDateTime = (TextView) row.findViewById(R.id.tvDateTime);
            equityDataHolder.llAlert = (LinearLayout) row.findViewById(R.id.llAlert);

            row.setTag(equityDataHolder);
        }
        else {
            equityDataHolder = (EquityDataHolder) row.getTag();
        }
        EquityData equityData = (EquityData) this.getItem(position);

        equityDataHolder.tvCompanyName.setText(equityData.getName());
        equityDataHolder.ivImage.setImageBitmap(equityData.getImage());

        equityDataHolder.tvSell.setText("Stop Loss @ "+equityData.getSell());
        equityDataHolder.tvTarget1.setText("@ "+equityData.getTarget1());
        equityDataHolder.tvTarget2.setText("@ "+equityData.getTarget2());
        equityDataHolder.tvTarget3.setText("@ "+equityData.getTarget3());
        equityDataHolder.tvNote.setText(equityData.getNote());
        equityDataHolder.tvDateTime.setText("@ "+equityData.getDate() +" "+equityData.getTime());

        equityDataHolder.llAlert.setVisibility(View.VISIBLE);
        equityDataHolder.tvCompanyName.setVisibility(View.VISIBLE);

        String type2 = equityData.getType2();
        equityDataHolder.tvType2.setText(type2);

        if(type2.equals("Buy")){
            equityDataHolder.tvType2.setBackgroundColor(getContext().getResources().getColor(R.color.BuyColor) );
            equityDataHolder.tvBuy.setText("Buy @ "+equityData.getBuy());
            equityDataHolder.tvBuy.setVisibility(View.VISIBLE);
            equityDataHolder.tvBuy.setTextColor(getContext().getResources().getColor(R.color.BuyColor) );
        }else if(type2.equals("Sell")){
            equityDataHolder.tvBuy.setVisibility(View.VISIBLE);
            equityDataHolder.tvBuy.setText("Sell @ "+equityData.getBuy());
            equityDataHolder.tvType2.setBackgroundColor(getContext().getResources().getColor(R.color.SellColor) );
            equityDataHolder.tvBuy.setTextColor(getContext().getResources().getColor(R.color.SellColor) );
        }else if(type2.equals("Stop Loss")){
            equityDataHolder.tvBuy.setVisibility(View.GONE);
            equityDataHolder.tvType2.setBackgroundColor(getContext().getResources().getColor(R.color.StopLossColor) );
            equityDataHolder.tvSell.setTextColor(getContext().getResources().getColor(R.color.StopLossColor) );
        }else if(type2.equals("Alert")){
            equityDataHolder.tvType2.setBackgroundColor(getContext().getResources().getColor(R.color.SellColor) );
            equityDataHolder.llAlert.setVisibility(View.GONE);
            equityDataHolder.tvCompanyName.setVisibility(View.GONE);
        }

        String type3 = equityData.getType3(), type4=equityData.getType4();
        equityDataHolder.tvType3.setText(type3);
        equityDataHolder.tvType4.setText(type4);
        if(type4.equals("Book Profit")||type4.equals("Target Met")){
            equityDataHolder.tvType4.setTextColor(getContext().getResources().getColor(R.color.BuyColor) );
        }else if(type4.equals("Stop Loss Hit")||type4.equals("Exit")){
            equityDataHolder.tvType4.setTextColor(getContext().getResources().getColor(R.color.SellColor) );
        }

        return row;
    }
    static class  EquityDataHolder
    {
        TextView tvCompanyName, tvBuy, tvSell, tvTarget1, tvTarget2, tvTarget3, tvType2,tvType3, tvType4, tvNote, tvDateTime;
        ImageView ivImage;
        LinearLayout llAlert;
    }



}
