//package com.joemoss.firebasetest.main;
//
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//import com.joemoss.firebasetest.Models.JourneyModel;
//
//public class PopularRecyclerViewAdapter extends FirestoreRecyclerAdapter<JourneyModel, PopularRecyclerViewAdapter.MyViewHolder> {
//
//    /**
//     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
//     * FirestoreRecyclerOptions} for configuration options.
//     *
//     * @param options
//     */
//    public PopularRecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<JourneyModel> options) {
//        super(options);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull JourneyModel journeyModel) {
//
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//    }
//}
