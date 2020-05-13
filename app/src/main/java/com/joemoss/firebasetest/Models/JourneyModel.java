package com.joemoss.firebasetest.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.List;


public class JourneyModel implements Parcelable {
    private String title;
    private String authorUID;
    private String authorUsername;
    private String authorProfPicRef;
    private String mainJourneyText;
    private String nextJourneyRef;
    private String prevJourneyRef;
    private String journeyTags;
    private List<JourneyImageModel> journeyImages;
    private int journeyKudos;
    private Timestamp timestamp;
    private String journeyID;
    private int voteStatus;
    

    public JourneyModel(){

    }

    public JourneyModel(String title, String authorUID, String authorUsername, String authorProfPicRef, String mainJourneyText, String nextJourneyRef, String prevJourneyRef, String journeyTags, List<JourneyImageModel> journeyImages, int journeyKudos, Timestamp timestamp) {
        this.title = title;
        this.authorUID = authorUID;
        this.authorUsername = authorUsername;
        this.authorProfPicRef = authorProfPicRef;
        this.mainJourneyText = mainJourneyText;
        this.nextJourneyRef = nextJourneyRef;
        this.prevJourneyRef = prevJourneyRef;
        this.journeyTags = journeyTags;
        this.journeyImages = journeyImages;
        this.journeyKudos = journeyKudos;
        this.timestamp = timestamp;
    }

    protected JourneyModel(Parcel in) {
        title = in.readString();
        authorUID = in.readString();
        authorUsername = in.readString();
        authorProfPicRef = in.readString();
        mainJourneyText = in.readString();
        nextJourneyRef = in.readString();
        prevJourneyRef = in.readString();
        journeyTags = in.readString();
        journeyKudos = in.readInt();
        journeyID = in.readString();
        journeyImages = in.createTypedArrayList(JourneyImageModel.CREATOR);
//        journeyImages = in.readList(journeyImages, JourneyImageModel.class.getClassLoader());
    }

    public static final Creator<JourneyModel> CREATOR = new Creator<JourneyModel>() {
        @Override
        public JourneyModel createFromParcel(Parcel in) {
            return new JourneyModel(in);
        }

        @Override
        public JourneyModel[] newArray(int size) {
            return new JourneyModel[size];
        }
    };

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
    public void setJourneyKudos(int journeyKudos){ this.journeyKudos = journeyKudos;}

    public int getJourneyKudos(){return journeyKudos;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(authorUID);
        dest.writeString(authorUsername);
        dest.writeString(authorProfPicRef);
        dest.writeString(mainJourneyText);
        dest.writeString(nextJourneyRef);
        dest.writeString(prevJourneyRef);
        dest.writeString(journeyTags);
        dest.writeInt(journeyKudos);
        dest.writeString(journeyID.toString());
        dest.writeTypedList(journeyImages);
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getJourneyID() {
        return journeyID;
    }

    public void setJourneyID(String journeyID) {
        this.journeyID = journeyID;
    }

    public int getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(int voted) {
        this.voteStatus = voted;
    }
}
