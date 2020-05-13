package com.joemoss.firebasetest.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class JourneyImageModel implements Parcelable {
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

    protected JourneyImageModel(Parcel in) {
        pictureRef = in.readString();
        imageText = in.readString();
        imageTitle = in.readString();
    }

    public static final Creator<JourneyImageModel> CREATOR = new Creator<JourneyImageModel>() {
        @Override
        public JourneyImageModel createFromParcel(Parcel in) {
            return new JourneyImageModel(in);
        }

        @Override
        public JourneyImageModel[] newArray(int size) {
            return new JourneyImageModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pictureRef);
        dest.writeString(imageText);
        dest.writeString(imageTitle);
    }
}
