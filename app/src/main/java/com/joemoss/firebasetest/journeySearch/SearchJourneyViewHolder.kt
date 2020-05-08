package com.joemoss.firebasetest.journeySearch

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.joemoss.firebasetest.GlideApp
import com.joemoss.firebasetest.R
import com.joemoss.firebasetest.adapters.PostsRecyclerViewAdapter
import com.joemoss.firebasetest.adapters.PostsRecyclerViewAdapter.JOURNEYSOURCE
import com.joemoss.firebasetest.main.JourneyViewActivity
import com.joemoss.firebasetest.main.SearchViewActivity


class SearchJourneyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
    val storage = FirebaseStorage.getInstance()
    
    fun bind(searchJourney : SearchJourney){
        view.findViewById<TextView>(R.id.journey_list_title).text = searchJourney.title
        view.findViewById<TextView>(R.id.journey_list_username).text = searchJourney.authorName
        val storRef = storage.getReference("users/"+searchJourney.authorUID+"/profilePic.jpg")
        GlideApp.with(view)
            .load(storRef)
            .circleCrop()
            .into(view.findViewById<ImageView>(R.id.journey_list_author_pic))
        view.setOnClickListener{
            val intent = Intent(view.context, JourneyViewActivity::class.java)
            intent.putExtra(PostsRecyclerViewAdapter.JOURNEYDATA, searchJourney.objectID)
            intent.putExtra(JOURNEYSOURCE, "1");
            startActivity(view.context, intent, null)
        }
    }

}