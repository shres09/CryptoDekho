package com.cryptowatch.models;

public class NewsArticle {
    private String title;
    private String description;
    private String image;
    private String source;
    private long publishedOn;
    private String url;

    public NewsArticle() {

    }

    public NewsArticle(String title, String description, String image, String source, long publishedOn, String url) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.source = source;
        this.publishedOn = publishedOn;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(long publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}