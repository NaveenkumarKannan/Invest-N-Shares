package com.androfocus.investnshare;

import android.graphics.Bitmap;

public class NewsFeedData {
    String title,content,link,date;
    Bitmap image;

    public NewsFeedData(String title, String content, String link, String date, Bitmap image) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.date = date;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
}
