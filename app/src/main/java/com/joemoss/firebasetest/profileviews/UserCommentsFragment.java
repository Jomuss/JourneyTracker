package com.joemoss.firebasetest.profileviews;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.auth.data.model.User;
import com.joemoss.firebasetest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserCommentsFragment extends Fragment {

    public UserCommentsFragment(){

    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_comments, container, false);
    }

}
