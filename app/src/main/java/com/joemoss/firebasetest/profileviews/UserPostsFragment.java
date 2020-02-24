package com.joemoss.firebasetest.profileviews;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.joemoss.firebasetest.Models.JourneyModel;
import com.joemoss.firebasetest.R;
import com.joemoss.firebasetest.adapters.PostsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserPostsFragment extends Fragment {

    List<JourneyModel> journeys = new ArrayList<>();
    private RecyclerView postsRecyclerView;
    private PostsRecyclerViewAdapter postsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firestore;
    FirebaseAuth fAuth;
    Context context;
    Query query;
    View frag;


    public UserPostsFragment(Context context){
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        frag = inflater.inflate(R.layout.fragment_user_posts, container, false);

        postsRecyclerView = frag.findViewById(R.id.user_posts_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        postsRecyclerView.setLayoutManager(layoutManager);

        final CollectionReference postsRef = firestore.collection("posts");
        query = postsRef.whereEqualTo("authorUID", fAuth.getCurrentUser().getUid());

        FirestoreRecyclerOptions<JourneyModel> options = new FirestoreRecyclerOptions.Builder<JourneyModel>()
                .setQuery(query, JourneyModel.class)
                .build();

        postsAdapter = new PostsRecyclerViewAdapter(context, options);
        postsRecyclerView.setAdapter(postsAdapter);

//        intializeRecyclerView(v);
        return frag;
    }

    @Override
    public void onStart() {
        super.onStart();

        postsAdapter.startListening();
    }

    private void intializeRecyclerView(final View v){
        firestore.collection("posts")
                .whereEqualTo("authorUID", fAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snapshot: queryDocumentSnapshots) {
                            journeys.add(snapshot.toObject(JourneyModel.class));
                        }
                    }
                });



    }

}
