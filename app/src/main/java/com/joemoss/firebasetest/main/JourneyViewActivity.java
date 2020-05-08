package com.joemoss.firebasetest.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joemoss.firebasetest.GlideApp;
import com.joemoss.firebasetest.Models.JourneyModel;
import com.joemoss.firebasetest.R;
import com.joemoss.firebasetest.adapters.PostsRecyclerViewAdapter;
import com.joemoss.firebasetest.profileviews.ProfileViewActivity;

import java.util.Objects;

public class JourneyViewActivity extends AppCompatActivity {

    TextView journeyTitle;
    TextView journeyAuthor;
    TextView journeyKudos;
    TextView journeyMainText;
    ImageView authorProfPic;
    FirebaseStorage storage;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_view);

        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        String source = i.getStringExtra(PostsRecyclerViewAdapter.JOURNEYSOURCE);
        if(source.equals("0")){
            final JourneyModel journey = (JourneyModel) i.getParcelableExtra(PostsRecyclerViewAdapter.JOURNEYDATA);
            initJourney(journey);
        }
        else{

            final String journeyID = i.getStringExtra(PostsRecyclerViewAdapter.JOURNEYDATA);
            firestore.collection("posts").document(journeyID).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            final JourneyModel journey = documentSnapshot.toObject(JourneyModel.class);
                            initJourney(journey);
                        }
                    });
        }



        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


    }

    private void initJourney(final JourneyModel journey){
        journeyTitle = findViewById(R.id.journey_title);
        journeyTitle.setText(journey.getTitle());
        journeyAuthor = findViewById(R.id.journey_author_username);
        journeyAuthor.setText(journey.getAuthorUsername());
        journeyKudos = findViewById(R.id.journey_kudos);
        journeyKudos.setText(Integer.toString(journey.getJourneyKudos()));
        journeyMainText = findViewById(R.id.journey_main_text);
        journeyMainText.setText(journey.getMainJourneyText());
        authorProfPic = findViewById(R.id.journey_view_prof_pic);
        StorageReference profPic = storage.getReference(journey.getAuthorProfPicRef());
        GlideApp.with(this)
                .load(profPic)
                .circleCrop()
                .into(authorProfPic);

        journeyAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profIntent = new Intent(JourneyViewActivity.this, ProfileViewActivity.class);
                profIntent.putExtra("UID", journey.getAuthorUID());
                startActivity(profIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
