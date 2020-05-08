package com.joemoss.firebasetest.journeySearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.joemoss.firebasetest.R


class SearchJourneyFragment : Fragment() {

    private val connection = ConnectionHandler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.journey_search_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(requireActivity())[SearchViewModel::class.java]

        viewModel.searchJourneys.observe(this, Observer { hits -> viewModel.searchJourneyAdapter.submitList(hits) })



        val rv = view.findViewById<RecyclerView>(R.id.searchJourneyList)
        rv.let {
            it.itemAnimator = null
            it.adapter = viewModel.searchJourneyAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
            it.autoScrollToStart(viewModel.searchJourneyAdapter)
        }
        val search = activity!!.findViewById<SearchView>(R.id.searchView);
        val searchBoxView = SearchBoxViewAppCompat(search)

        connection += viewModel.searchBox.connectView(searchBoxView);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.disconnect()
    }
}