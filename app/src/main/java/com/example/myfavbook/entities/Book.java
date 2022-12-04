package com.example.myfavbook.entities;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {

    // creating string, int and array list variables for our book details
    private String title;
    private String subtitle;
    private ArrayList<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private int pageCount;
    private String thumbnail;
    private String previewLink;
    private String infoLink;
    private String buyLink;
    private ArrayList<Review> reviews;
    private int totalRating;
    private Double rate;

    public Book(String title, String subtitle, String publisher,
                String publishedDate, String description, int pageCount, String thumbnail,
                String previewLink, String infoLink, String buyLink) {
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
        this.thumbnail = thumbnail;
        this.previewLink = previewLink;
        this.infoLink = infoLink;
        this.buyLink = buyLink;
        this.reviews = new ArrayList<>();
        this.totalRating = 0;
        this.rate = 0.0;
    }

    public Book(String title, String subtitle, String publisher,
                String publishedDate, String description, int pageCount, String thumbnail,
                String previewLink, String infoLink, String buyLink, ArrayList<Review> reviews,
                int totalRating, Double rate) {
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
        this.thumbnail = thumbnail;
        this.previewLink = previewLink;
        this.infoLink = infoLink;
        this.buyLink = buyLink;
        this.reviews = reviews;
        this.totalRating = totalRating;
        this.rate = rate;
    }


    // creating getter and setter methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public String getBuyLink() {
        return buyLink;
    }

    public void setBuyLink(String buyLink) {
        this.buyLink = buyLink;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(int totalRating) {
        this.totalRating = totalRating;
    }

    public void incrementByOneTotalRates(){
        this.totalRating++;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public void addPointsToRate(Double rate){
        this.rate += rate;
    }

    public Double getAverageRate() {
        try {
            return Double.valueOf(this.rate / this.totalRating);
        } catch (Exception e) {
            Log.d(TAG, "Error");
        }
        return 0.0;
    }
}



