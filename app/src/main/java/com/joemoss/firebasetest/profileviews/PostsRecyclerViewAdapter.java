package com.joemoss.firebasetest.profileviews;

import android.app.DownloadManager;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joemoss.firebasetest.GlideApp;
import com.joemoss.firebasetest.Models.JourneyModel;
import com.joemoss.firebasetest.R;

import java.util.List;

public class PostsRecyclerViewAdapter extends FirestoreRecyclerAdapter<JourneyModel, PostsRecyclerViewAdapter.MyViewHolder> {
//        RecyclerView.Adapter<PostsRecyclerViewAdapter.MyViewHolder> {
    Context context;
    FirestoreRecyclerOptions<JourneyModel> journeys;
    FirebaseStorage storage;
    private Query query;
//    private FirestoreRecyclerAdapter


    PostsRecyclerViewAdapter(Context context, FirestoreRecyclerOptions<JourneyModel> journeys){
        super(journeys);
        this.context = context;
        this.journeys = journeys;
        storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public PostsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.journey_list_row_view, parent, false);
        final MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }



    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull JourneyModel journey) {
        holder.journeyTitle.setText(journey.getTitle());
        holder.authorUsername.setText(journey.getAuthorUsername());
        StorageReference profPic = storage.getReference(journey.getAuthorProfPicRef());
        GlideApp.with(context)
                .load(profPic)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.authorProfPic);
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

        MyViewHolder(View v){
            super(v);
            mView = v;
            authorUsername = mView.findViewById(R.id.journey_list_username);
            journeyTitle = mView.findViewById(R.id.journey_list_title);
            authorKudos = mView.findViewById(R.id.journey_list_kudos);
            authorProfPic = mView.findViewById(R.id.journey_list_author_pic);
        }
    }
}
