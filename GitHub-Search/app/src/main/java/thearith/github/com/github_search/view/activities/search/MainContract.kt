package thearith.github.com.github_search.view.activities.search

import io.reactivex.Observable
import thearith.github.com.github_search.view.model.SearchFeedResponse

/**
 * This specifies the contract between the view (MainActivity) and
 * the presenter (MainPresenter).
 */
interface MainContract {

    interface View {

        /**
         * Updates UI based on GitHub search response
         *
         * @param response SearchFeedResponse object that comes from a GitHub search api request
         * */
        fun updateUI(response : SearchFeedResponse)
    }

    interface Presenter {

        /**
         * Creates a search api request that starts from the start pagination
         * The stream will be initiated with Status.IN_PROGRESS_WITH_REFRESH
         * as a notification to show loading progress on UI
         *
         * @param searchParam a String search query
         * @return Observable<SearchFeedResult>
         * */
        fun loadNewSearch(searchParam : String) : Observable<SearchFeedResponse>

        /**
         * Creates a search api request with the incremented pagination
         * The stream will be initiated with Status.IN_PROGRESS
         * as a notification to show loading progress on UI
         *
         * @param searchParam a String search query
         * @return Observable<SearchFeedResult> object
         * */
        fun loadNextSearch(searchParam : String) : Observable<SearchFeedResponse>
    }

}