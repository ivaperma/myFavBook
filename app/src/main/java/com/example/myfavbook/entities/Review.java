package com.example.myfavbook.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Review {

    //attributes
    private String userEmail;
    private String review;

    //Constructors
    public Review(@JsonProperty("userEmail") String userEmail, @JsonProperty("review") String review) {
        this.userEmail = userEmail;
        this.review = review;
    }

    public Review() {

    }

    //Getters and setters
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
