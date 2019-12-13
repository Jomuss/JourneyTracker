package com.joemoss.firebasetest.Models;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class JourneyModel {
    private String title;
    private String authorUID;
    private String authorUsername;
    private String authorProfPicRef;
    private String mainJourneyText;
    private String nextJourneyRef;
    private String prevJourneyRef;
    private String journeyTags;
    private List<JourneyImageModel> journeyImages;
    

    public JourneyModel(){

    }

    public JourneyModel(String title, String authorUID, String authorUsername, String authorProfPicRef, String mainJourneyText, String nextJourneyRef, String prevJourneyRef, String journeyTags, List<JourneyImageModel> journeyImages) {
        this.title = title;
        this.authorUID = authorUID;
        this.authorUsername = authorUsername;
        this.authorProfPicRef = authorProfPicRef;
        this.mainJourneyText = mainJourneyText;
        this.nextJourneyRef = nextJourneyRef;
        this.prevJourneyRef = prevJourneyRef;
        this.journeyTags = journeyTags;
        this.journeyImages = journeyImages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorUID() {
        return authorUID;
    }

    public void setAuthorUID(String authorUID) {
        this.authorUID = authorUID;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getAuthorProfPicRef() {
        return authorProfPicRef;
    }

    public void setAuthorProfPicRef(String authorProfPicRef) {
        this.authorProfPicRef = authorProfPicRef;
    }

    public String getMainJourneyText() {
        return mainJourneyText;
    }

    public void setMainJourneyText(String mainJourneyText) {
        this.mainJourneyText = mainJourneyText;
    }

    public String getNextJourneyRef() {
        return nextJourneyRef;
    }

    public void setNextJourneyRef(String nextJourneyRef) {
        this.nextJourneyRef = nextJourneyRef;
    }

    public String getPrevJourneyRef() {
        return prevJourneyRef;
    }

    public void setPrevJourneyRef(String prevJourneyRef) {
        this.prevJourneyRef = prevJourneyRef;
    }

    public String getJourneyTags() {
        return journeyTags;
    }

    public void setJourneyTags(String journeyTags) {
        journeyTags = journeyTags;
    }

    public List<JourneyImageModel> getJourneyImages() {
        return journeyImages;
    }

    public void setJourneyImages(List<JourneyImageModel> journeyImages) {
        this.journeyImages = journeyImages;
    }
}
