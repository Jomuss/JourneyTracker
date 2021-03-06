package com.joemoss.firebasetest.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.joemoss.firebasetest.Models.JourneyModel;
import com.joemoss.firebasetest.R;
import com.joemoss.firebasetest.adapters.PostsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PopularFragment extends Fragment {
    List<JourneyModel> journeys = new ArrayList<>();
    private RecyclerView postsRecyclerView;
    private PostsRecyclerViewAdapter postsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firestore;
    FirebaseAuth fAuth;
    Context context;
    Query query;
    View frag;

    public PopularFragment(Context context){this.context = context;}


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        firestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        frag = inflater.inflate(R.layout.fragment_popular_view, container, false);

        postsRecyclerView = frag.findViewById(R.id.popular_posts_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        postsRecyclerView.setLayoutManager(layoutManager);

        final CollectionReference postsRef = firestore.collection("posts");
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
        query = postsRef.orderBy("timestamp", Query.Direction.DESCENDING).orderBy("journeyKudos", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<JourneyModel> options = new FirestoreRecyclerOptions.Builder<JourneyModel>()
                .setQuery(query, JourneyModel.class)
                .build();

        postsAdapter = new PostsRecyclerViewAdapter(context, options);
        postsRecyclerView.setAdapter(postsAdapter);

        return frag;
    }

    @Override
    public void onStart() {
        super.onStart();

        postsAdapter.startListening();
    }
}
