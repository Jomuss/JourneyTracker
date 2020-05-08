package com.joemoss.firebasetest.journeySearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.LogLevel

class SearchViewModel : ViewModel() {
    val client = ClientSearch(ApplicationID("1ZAZ1WFMSG"), APIKey("60b12eb2867ad6aac5d23e2dd97e6f85"), LogLevel.ALL)
    val index = client.initIndex(IndexName("PostsCollection_prod"))
    val searcher = SearcherSingleIndex(index)

    val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher) { hit ->
        SearchJourney(
                hit.json.getPrimitive("title").content,
                hit.json.getPrimitive("tags").content,
                hit.json.getPrimitive("authorName").content,
                hit.json.getPrimitive("authorUID").content,
                hit.json.getPrimitive("objectID").content
        )
    }

    val pagedListConfig = PagedList.Config.Builder().setPageSize(50).build()
    val searchJourneys: LiveData<PagedList<SearchJourney>> = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    val searchJourneyAdapter = SearchJourneyAdapter();

    val searchBox = SearchBoxConnectorPagedList(searcher, listOf(searchJourneys))
    val connection = ConnectionHandler()

    init {
        connection += searchBox
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        connection.disconnect()
    }
}