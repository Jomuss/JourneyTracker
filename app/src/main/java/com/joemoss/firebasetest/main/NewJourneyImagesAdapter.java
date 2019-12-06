package com.joemoss.firebasetest.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.joemoss.firebasetest.R;

public class NewJourneyImagesAdapter extends RecyclerView.Adapter<NewJourneyImagesAdapter.MyViewHolder> {


    NewJourneyImagesAdapter(Context context){

    }

    @NonNull
    @Override
    public NewJourneyImagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.new_journey_image_view, parent, false);
        final MyViewHolder vh = new MyViewHolder(v);

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }



    static class MyViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        private ImageView imagePreview;
        private FloatingActionButton newImageButton;
        private EditText imageText;
        private EditText imageTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            imagePreview = mView.findViewById(R.id.new_journey_image_thumbnail);
            newImageButton = mView.findViewById(R.id.new_journey_image_fab);
            imageText = mView.findViewById(R.id.new_journey_image_text);
            imageTitle = mView.findViewById(R.id.new_journey_image_title);

        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
