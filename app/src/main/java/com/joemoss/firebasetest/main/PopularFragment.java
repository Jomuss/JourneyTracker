package com.joemoss.firebasetest.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.joemoss.firebasetest.R;

public class PopularFragment extends Fragment {
    private String title;
    private int page;
    public static final String ARG_PAGE = "ARG_PAGE";

    public PopularFragment(){

    }

    public PopularFragment newInstance(int page, String title){
        PopularFragment popularFragment = new PopularFragment();
        Bundle args = new Bundle();
        args.putInt("pageNum", page);
        args.putString("pageTitle", title);
        popularFragment.setArguments(args);
        return popularFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_view, container, false);
        return view;
    }
}
