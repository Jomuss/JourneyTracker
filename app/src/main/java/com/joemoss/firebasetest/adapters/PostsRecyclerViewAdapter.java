package com.joemoss.firebasetest.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.instantsearch.core.InstantSearch;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joemoss.firebasetest.GlideApp;
import com.joemoss.firebasetest.Models.JourneyModel;
import com.joemoss.firebasetest.R;
import com.joemoss.firebasetest.main.JourneyViewActivity;

public class PostsRecyclerViewAdapter extends FirestoreRecyclerAdapter<JourneyModel, PostsRecyclerViewAdapter.MyViewHolder> {
    Context context;
    FirestoreRecyclerOptions<JourneyModel> journeys;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    private Query query;
    public static final String JOURNEYDATA = "journeyData";
    public static final String JOURNEYSOURCE = "journeySource";



    public PostsRecyclerViewAdapter(Context context, FirestoreRecyclerOptions<JourneyModel> journeys){
        super(journeys);
        this.context = context;
        this.journeys = journeys;
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public PostsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.journey_list_row_view, parent, false);
        final MyViewHolder vh = new MyViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewJourney = new Intent(parent.getContext(), JourneyViewActivity.class);
                viewJourney.putExtra(JOURNEYDATA, vh.journey);
                viewJourney.putExtra(JOURNEYSOURCE, "0");
                parent.getContext().startActivity(viewJourney);
            }
        });
//        v.findViewById(R.id.give_kudos_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                giveKudos(vh);
//            }
//        });
//        v.findViewById(R.id.remove_kudos_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                takeKudos(vh);
//            }
//        });
        return vh;
    }



    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull final JourneyModel journey) {
        holder.journeyTitle.setText(journey.getTitle());
        holder.authorUsername.setText(journey.getAuthorUsername());
        holder.journey = journey;
        holder.authorKudos.setText(Integer.toString(journey.getJourneyKudos()));
        StorageReference profPic = storage.getReference(journey.getAuthorProfPicRef());
        GlideApp.with(this.context)
                .load(profPic)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.authorProfPic);
        if(journey.getVoteStatus() == 1){
            changeButton(holder, true);
        }else if(journey.getVoteStatus() == -1){
            changeButton(holder, false);
        }

        final MyViewHolder curHolder = holder;
        holder.giveKudos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveKudos(curHolder, journey);
            }
        });
        holder.takeKudos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeKudos(curHolder, journey);
            }
        });
    }



    @Override
    public int getItemCount() {
        return journeys.getSnapshots().size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        private TextView authorUsername;
        private TextView journeyTitle;
        private TextView authorKudos;
        private ImageView authorProfPic;
        private JourneyModel journey;
        private ImageButton giveKudos;
        private ImageButton takeKudos;
        //-1 if downvoted, 0 if no vote, 1 if voted;
        private int voted;


        MyViewHolder(View v){
            super(v);
            mView = v;
            authorUsername = mView.findViewById(R.id.journey_list_username);
            journeyTitle = mView.findViewById(R.id.journey_list_title);
            authorKudos = mView.findViewById(R.id.journey_list_kudos);
            authorProfPic = mView.findViewById(R.id.journey_list_author_pic);
            giveKudos = mView.findViewById(R.id.give_kudos_button);
            takeKudos = mView.findViewById(R.id.remove_kudos_button);


        }
    }

    public void giveKudos(MyViewHolder vh, JourneyModel J){
        J.setVoteStatus(1);
        changeButton(vh, true);
        final MyViewHolder curHolder = vh;
        DocumentReference journeyRef = firestore.collection("posts").document(vh.journey.getJourneyID());
        journeyRef.update("journeyKudos", FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        changeButton(curHolder, true);
//                              vh.giveKudos.setColorFilter(Color.argb(255, 240, 208, 79));
//                              vh.takeKudos.setColorFilter(Color.argb(255, 153, 155, 161));
                    }
                });
//        vh.giveKudos.setColorFilter(Color.argb(255, 240, 208, 79));
//        vh.takeKudos.setColorFilter(Color.argb(255, 153, 155, 161));
    }
    

    public void takeKudos(MyViewHolder vh, JourneyModel J){
        J.setVoteStatus(-1);
        changeButton(vh, false);
        final MyViewHolder curHolder = vh;
        DocumentReference journeyRef = firestore.collection("posts").document(vh.journey.getJourneyID());
        journeyRef.update("journeyKudos", FieldValue.increment(-1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        changeButton(curHolder, false);
//                        vh.giveKudos.setColorFilter(Color.argb(255, 153, 155, 161));
//                        vh.takeKudos.setColorFilter(Color.argb(255, 95, 37, 184));
                    }
                });
//        vh.giveKudos.setColorFilter(Color.argb(255, 153, 155, 161));
//        vh.takeKudos.setColorFilter(Color.argb(255, 95, 37, 184));
    }
    
    public void changeButton(MyViewHolder vh, boolean giveKudos){
        if(giveKudos){
//            vh.giveKudos.setBackgroundColor(Color.parseColor("FFDD9C26"));
//            vh.voted = 1;
            vh.giveKudos.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.kudos_given));
            vh.takeKudos.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.kudos_default));
//            vh.giveKudos.setColorFilter(Color.argb(255, 240, 208, 79));
//            vh.takeKudos.setColorFilter(Color.argb(255, 153, 155, 161));

        }else{
//            vh.voted = -1;
            vh.giveKudos.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.kudos_default));
            vh.takeKudos.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.kudos_taken));
//            vh.giveKudos.setColorFilter(Color.argb(255, 153, 155, 161));
//            vh.takeKudos.setColorFilter(Color.argb(255, 95, 37, 184));
        }

        vh.giveKudos.refreshDrawableState();
        vh.takeKudos.refreshDrawableState();
    }

    private void checkVote(){

    }


}
