package com.joemoss.firebasetest.profileviews;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joemoss.firebasetest.GlideApp;
import com.joemoss.firebasetest.Models.CurrentUser;
import com.joemoss.firebasetest.R;
import com.joemoss.firebasetest.main.MainPagerAdapter;
import com.joemoss.firebasetest.main.MainViewActivity;

public class ProfileViewActivity extends AppCompatActivity {
    FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        storage = FirebaseStorage.getInstance();

        //Initialize ViewPager
        ViewPager viewPager = findViewById(R.id.profile_view_pager);
        viewPager.setAdapter(new ProfilePagerAdapter(getSupportFragmentManager(), ProfileViewActivity.this));
        TabLayout tabs = findViewById(R.id.profile_tabs);
        tabs.setupWithViewPager(viewPager);


        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set Username TextView and User Profile Picture
        TextView username = findViewById(R.id.profileUsername);
        username.setText(CurrentUser.getInstance().currentUser.getDisplayName());
        StorageReference profPicRef = storage.getReference("/users/"+CurrentUser.getInstance().currentUser.getUid()+"/profilePic.jpg");
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
        TextView editProfile = findViewById(R.id.EditProfileTextView);
        editProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditProfile();

            }
        });
    }


    private void EditProfile(){
        Intent editProfileIntent = new Intent(this, EditProfileViewActivity.class);
        startActivity(editProfileIntent);
    }
}
