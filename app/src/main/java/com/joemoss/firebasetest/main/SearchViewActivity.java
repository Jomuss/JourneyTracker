package com.joemoss.firebasetest.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.joemoss.firebasetest.R;
import com.joemoss.firebasetest.journeySearch.SearchJourneyFragment;

public class SearchViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        Toolbar toolBar  = findViewById(R.id.toolbar);
//        toolBar.setTitle("Journey Tracker");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SearchView journeySearch = findViewById(R.id.searchView);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SearchJourneyFragment fragmentSearch = new SearchJourneyFragment();
        ft.replace(R.id.searchPlaceholder, fragmentSearch);
        ft.commit();
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
