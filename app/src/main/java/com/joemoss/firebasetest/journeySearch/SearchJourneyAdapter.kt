package com.joemoss.firebasetest.journeySearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.joemoss.firebasetest.R

class SearchJourneyAdapter : PagedListAdapter<SearchJourney, SearchJourneyViewHolder>(SearchJourneyAdapter){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchJourneyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.journey_search_row_view, parent, false)

        return SearchJourneyViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchJourneyViewHolder, position: Int) {
        val journey = getItem(position)

        if(journey != null) holder.bind(journey)

    }

    companion object : DiffUtil.ItemCallback<SearchJourney>() {
        override fun areItemsTheSame(oldItem: SearchJourney, newItem: SearchJourney): Boolean {
            return oldItem::class == newItem::class
        }

        override fun areContentsTheSame(oldItem: SearchJourney, newItem: SearchJourney): Boolean {
            return (oldItem.title == newItem.title) && (oldItem.authorName == newItem.authorName)
        }
    }
}