package com.joemoss.firebasetest.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.joemoss.firebasetest.R;

public class SubscribedFragment extends Fragment {
    private String title;
    private int page;

    public static SubscribedFragment newInstance(int page, String title){
        SubscribedFragment subscribedFragment = new SubscribedFragment();
        Bundle args = new Bundle();
        args.putInt("pageNum", page);
        args.putString("pageTitle", title);
        subscribedFragment.setArguments(args);
        return subscribedFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("pageNum", 0);
        title = getArguments().getString("pageTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribed_view, container, false);
        return view;
    }
}
