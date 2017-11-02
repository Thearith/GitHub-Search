package thearith.github.com.github_search.presentation.presenter.search

import android.text.TextUtils
import io.reactivex.Observable
import thearith.github.com.github_search.data.search.repository.GitHubSearchRepository
import thearith.github.com.github_search.presentation.executor.PostExecutionThread
import thearith.github.com.github_search.presentation.executor.ThreadExecutor
import thearith.github.com.github_search.presentation.presenter.base.BasePresenter
import thearith.github.com.github_search.view.model.SearchFeedResponse
import thearith.github.com.github_search.view.model.Status
import thearith.github.com.github_search.view.activities.search.MainContract
import javax.inject.Inject

/**
 * Created by Thearith on 10/31/17.
 */
class MainPresenter : BasePresenter, MainContract.Presenter {

    private val mGitHubSearchRepository : GitHubSearchRepository

    private var mPageNumber : Int = 1

    @Inject
    constructor(threadExecutor: ThreadExecutor,
                postExecutionThread: PostExecutionThread,
                gitHubSearchRepository: GitHubSearchRepository) : super(threadExecutor, postExecutionThread) {
        mGitHubSearchRepository = gitHubSearchRepository
    }


    /**
     * UI states
     * */

    private fun initializePageNumber() {
        mPageNumber = 1
    }

    private fun incrementPageNumber() {
        mPageNumber++
    }


    override fun loadNewSearch(searchParam: String) : Observable<SearchFeedResponse> {
        initializePageNumber()
        return loadSearch(searchParam, mPageNumber)
                .startWith(SearchFeedResponse(Status.IN_PROGRESS_WITH_REFRESH))
                .applySchedulers()
    }

    override fun loadNextSearch(searchParam: String) : Observable<SearchFeedResponse> {
        incrementPageNumber()
        return loadSearch(searchParam, mPageNumber)
                .startWith(SearchFeedResponse(Status.IN_PROGRESS))
                .applySchedulers()
    }

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