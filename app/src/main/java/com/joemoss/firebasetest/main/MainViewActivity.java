package com.joemoss.firebasetest.main;

import android.content.Intent;
import android.os.Bundle;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joemoss.firebasetest.GlideApp;
import com.joemoss.firebasetest.Models.CurrentUser;
import com.joemoss.firebasetest.R;
import com.joemoss.firebasetest.profileviews.ProfileViewActivity;
import com.joemoss.firebasetest.startscreens.SplashScreenActivity;

public class MainViewActivity extends AppCompatActivity {

    public final String APP_TAG = "Journey Tracker";
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    FirebaseAuth fAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

//        curUser = FirebaseAuth.getInstance().getCurrentUser();
        CurrentUser.getInstance().intializeUser(FirebaseAuth.getInstance().getCurrentUser());
        storage = FirebaseStorage.getInstance();
        fAuth = FirebaseAuth.getInstance();

        //Initialize ViewPager
        ViewPager viewPager = findViewById(R.id.main_view_pager);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), MainViewActivity.this));
        TabLayout tabs = findViewById(R.id.main_tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.new_journey_image_fab);

        //Set and Apply Toolbar
        Toolbar toolBar  = findViewById(R.id.toolbar);
        toolBar.setTitle("Journey Tracker");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize Drawer Layout
        DrawerLayout mDrawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolBar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        mDrawerToggle.syncState();
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch(id){
                    case R.id.sign_out_button:
                        logOut();
                    case R.id.view_user_profile_button:
                        viewProfile();
                    default: return true;
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newJourney();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        //If there was a change in the users data, reload the username and profile picture
        if(CurrentUser.getInstance().getProfileDataChangedValue()) {
            TextView username = findViewById(R.id.drawerUsernameText);
            username.setText(CurrentUser.getInstance().currentUser.getDisplayName());
            StorageReference profPicRef = storage.getReference("/users/" + CurrentUser.getInstance().currentUser.getUid() + "/profilePic.jpg");
            ImageView drawerPic = (ImageView) findViewById(R.id.drawerProfilePic);
            try {
                GlideApp.with(this)
                        .load(profPicRef)
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(drawerPic);
            } catch (Exception e) {
                e.printStackTrace();
            }
            CurrentUser.getInstance().profileDataChanged(false);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        TextView username = findViewById(R.id.drawerUsernameText);
        username.setText(CurrentUser.getInstance().currentUser.getDisplayName());
        StorageReference profPicRef = storage.getReference("/users/"+CurrentUser.getInstance().currentUser.getUid()+"/profilePic.jpg");
        ImageView drawerPic = (ImageView) findViewById(R.id.drawerProfilePic);
        try{
            GlideApp.with(this)
                    .load(profPicRef)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(drawerPic);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    private void newJourney(){
        Intent newJourneyIntent = new Intent(this, NewJourneyViewActivity.class);
        startActivity(newJourneyIntent);
    }
    //Logs User out and kicks user to splash screen
    private void logOut(){
        Intent splashIntent = new Intent(this, SplashScreenActivity.class);
        fAuth.signOut();
        startActivity(splashIntent);
        finish();
    }
    private void viewProfile(){
        Intent profIntent = new Intent(this, ProfileViewActivity.class);
        startActivity(profIntent);
    }


}