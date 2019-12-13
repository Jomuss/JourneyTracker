package com.joemoss.firebasetest.profileviews;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joemoss.firebasetest.GlideApp;
import com.joemoss.firebasetest.Models.CurrentUser;
import com.joemoss.firebasetest.Models.JourneyModel;
import com.joemoss.firebasetest.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewActivity extends AppCompatActivity {
    FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage;
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;
    TextView bioText;
    boolean bioToggled = false;
    List<JourneyModel> journeys = new ArrayList<>();
    private RecyclerView postsRecyclerView;
    private PostsRecyclerViewAdapter postsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Slide slide = new Slide();
//        slide.setSlideEdge(Gravity.RIGHT);
//        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
//        getWindow().setEnterTransition(slide);
        setContentView(R.layout.activity_profile_view);

        storage = FirebaseStorage.getInstance();
        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        //Initialize ViewPager
        ViewPager userViewPager = findViewById(R.id.profile_viewpager);
        userViewPager.setAdapter(new ProfilePagerAdapter(getSupportFragmentManager(), ProfileViewActivity.this));
        TabLayout tabs = findViewById(R.id.profile_tabs);
        tabs.setupWithViewPager(userViewPager);

        //initialize toolbar
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        //Set Username TextView, User Profile Picture, and Bio
        TextView username = findViewById(R.id.profileUsername);
        username.setText(CurrentUser.getInstance().currentUser.getDisplayName());
        StorageReference profPicRef = storage.getReference("/users/"+fAuth.getCurrentUser().getUid()+"/profilePic.jpg");
        ImageView drawerPic = (ImageView) findViewById(R.id.profileProfPic);
        try{
            GlideApp.with(this)
                    .load(profPicRef)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(drawerPic);
        }catch (Exception e){
            e.printStackTrace();
        }
        bioText = findViewById(R.id.profile_bio_text);
        DocumentReference userDocRef = firestore.collection("users").document(fAuth.getCurrentUser().getUid());
        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                bioText.setText(snapshot.get("bio").toString());
            }
        });
        bioText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bioToggled){
                    bioText.setMaxLines(Integer.MAX_VALUE);
                    bioToggled = true;
                }else{
                    bioText.setMaxLines(2);
                    bioToggled = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_view_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.edit_profile_button) {
                EditProfile();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    private void EditProfile(){
        Intent editProfileIntent = new Intent(this, EditProfileViewActivity.class);
        startActivity(editProfileIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

//    private void intializeRecyclerView(){
//        firestore.collection("posts")
//                .whereEqualTo("authorUID", fAuth.getCurrentUser().getUid())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        journeys = queryDocumentSnapshots.toObjects(JourneyModel.class);
//                    }
//                });
//        postsRecyclerView = findViewById(R.id.user_posts_recycler_view);
//        postsAdapter = new PostsRecyclerViewAdapter(this, journeys);
//        layoutManager = new LinearLayoutManager(ProfileViewActivity.this);
//        postsRecyclerView.setLayoutManager(layoutManager);
//        postsRecyclerView.setAdapter(postsAdapter);
//    }


}
