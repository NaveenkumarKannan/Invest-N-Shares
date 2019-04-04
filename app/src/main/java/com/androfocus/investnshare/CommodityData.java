package com.androfocus.investnshare;

import android.graphics.Bitmap;

public class CommodityData {
   private String type1, type2,type3,type4, name, target1, target2, target3, buy, sell, note, date, time;
   private Bitmap image;

    public CommodityData(String type1, String type2, String name, String target1, String target2, String target3, String buy, String sell, String note, String date, Bitmap image, String time, String type3, String type4) {
        this.type1 = type1;
        this.type2 = type2;
        this.name = name;
        this.target1 = target1;
        this.target2 = target2;
        this.target3 = target3;
        this.buy = buy;
        this.sell = sell;
        this.note = note;
        this.date = date;
        this.image = image;
        this.time = time;
        this.type3 = type3;
        this.type4 = type4;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget1() {
        return target1;
    }

    public void setTarget1(String target1) {
        this.target1 = target1;
    }

    public String getTarget2() {
        return target2;
    }

    public void setTarget2(String target2) {
        this.target2 = target2;
    }

    public String getTarget3() {
        return target3;
    }

    public void setTarget3(String target3) {
        this.target3 = target3;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public String getType4() {
        return type4;
    }

    public void setType4(String type4) {
        this.type4 = type4;
    }
}
