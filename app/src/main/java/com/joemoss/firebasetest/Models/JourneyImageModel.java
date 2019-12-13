package com.joemoss.firebasetest.Models;

public class JourneyImageModel {
    private String pictureRef;
    private String imageText;
    private String imageTitle;

    public JourneyImageModel(){

    }

    public JourneyImageModel(String pictureRef, String imageText, String imageTitle){
        this.pictureRef = pictureRef;
        this.imageText = imageText;
        this.imageTitle = imageTitle;
    }

    public String getPictureRef() {
        return pictureRef;
    }

    public void setPictureRef(String pictureRef) {
        this.pictureRef = pictureRef;
    }

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }
}
