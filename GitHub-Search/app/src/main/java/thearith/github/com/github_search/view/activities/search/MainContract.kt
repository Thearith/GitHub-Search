package thearith.github.com.github_search.view.activities.search

import io.reactivex.Observable
import thearith.github.com.github_search.view.model.SearchFeedResponse

/**
 * Created by Thearith on 10/31/17.
 */

interface MainContract {

    interface View {
        fun updateUI(response : SearchFeedResponse)
        fun handleError(errorMsg : String)
    }

    interface Presenter {
        fun loadNewSearch(searchParam : String) : Observable<SearchFeedResponse>
        fun loadNextSearch(searchParam : String) : Observable<SearchFeedResponse>
    }

}