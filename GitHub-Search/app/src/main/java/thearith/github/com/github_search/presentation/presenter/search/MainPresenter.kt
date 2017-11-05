package thearith.github.com.github_search.presentation.presenter.search

import android.text.TextUtils
import io.reactivex.Observable
import thearith.github.com.github_search.data.search.repository.GitHubSearchRepository
import thearith.github.com.github_search.presentation.schedulers.PostExecutionThread
import thearith.github.com.github_search.presentation.schedulers.ThreadExecutor
import thearith.github.com.github_search.presentation.presenter.base.BasePresenter
import thearith.github.com.github_search.view.model.SearchFeedResponse
import thearith.github.com.github_search.view.model.Status
import thearith.github.com.github_search.view.activities.search.MainContract
import javax.inject.Inject

/**
 * Presenter that controls communication between MainActivity (views) and
 * GitHubSearchRepository (models)
 */
class MainPresenter : BasePresenter, MainContract.Presenter {

    // Data Source
    private val mGitHubSearchRepository : GitHubSearchRepository

    // States
    private var mPageNumber : Int = 1

    @Inject
    constructor(threadExecutor: ThreadExecutor,
                postExecutionThread: PostExecutionThread,
                gitHubSearchRepository: GitHubSearchRepository) : super(threadExecutor, postExecutionThread) {
        mGitHubSearchRepository = gitHubSearchRepository
    }

    private fun initializePageNumber() {
        mPageNumber = 1
    }

    private fun incrementPageNumber() {
        mPageNumber++
    }


    /**
     * Creates a search api request that starts from the start pagination
     * The stream will be initiated with Status.IN_PROGRESS_WITH_REFRESH
     * as a notification to show loading progress on UI
     *
     * @param searchParam a String search query
     * @return Observable<SearchFeedResult>
     * */
    override fun loadNewSearch(searchParam: String) : Observable<SearchFeedResponse> {
        initializePageNumber()
        return loadSearch(searchParam, mPageNumber)
                .startWith(SearchFeedResponse(Status.IN_PROGRESS_WITH_REFRESH))
                .applySchedulers()
    }

    /**
     * Creates a search api request with the incremented pagination
     * The stream will be initiated with Status.IN_PROGRESS
     * as a notification to show loading progress on UI
     *
     * @param searchParam a String search query
     * @return Observable<SearchFeedResult> object
     * */
    override fun loadNextSearch(searchParam: String) : Observable<SearchFeedResponse> {
        incrementPageNumber()
        return loadSearch(searchParam, mPageNumber)
                .startWith(SearchFeedResponse(Status.IN_PROGRESS))
                .applySchedulers()
    }

    /**
     * Creates a search api request from search query and pagination param
     * If the search query is empty, the stream is just Status.IDLE state
     * Otherwise, it will return search api response
     *
     * @param searchParam a String search query
     * @param pageNumber pagination param
     * @return Observable<SearchFeedResult> object
     * */
    private fun loadSearch(searchParam : String, pageNumber : Int) =
            if(TextUtils.isEmpty(searchParam))
                loadEmptyQuerySearch()
            else
                loadNonEmptyQuerySearch(searchParam, pageNumber)

    private fun loadEmptyQuerySearch() =
            Observable.just(SearchFeedResponse(Status.IDLE))

    private fun loadNonEmptyQuerySearch(searchParam : String, pageNumber : Int) =
            mGitHubSearchRepository.searchGitHubRepo(searchParam, pageNumber)
                    .map {
                        val isNotEmpty = it.items?.isNotEmpty()
                        val mode = if(isNotEmpty == true) Status.COMPLETE else Status.NO_RESULT

                        SearchFeedResponse(mode, it)
                    }
                    .onErrorReturn { SearchFeedResponse(Status.ERROR) }
}