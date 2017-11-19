package thearith.github.com.github_search.view.activities.search

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import thearith.github.com.github_search.R
import thearith.github.com.github_search.data.search.network.search.model.GitHubSearchModel
import thearith.github.com.github_search.presentation.presenter.search.MainPresenter
import thearith.github.com.github_search.view.adapter.GitHubSearchAdapter
import thearith.github.com.github_search.view.activities.base.BaseActivity
import thearith.github.com.github_search.view.custom.LogoWithTextView
import thearith.github.com.github_search.view.model.SearchFeedResponse
import thearith.github.com.github_search.view.model.Status
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.SearchView
import android.widget.ProgressBar
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.Observable
import thearith.github.com.github_search.view.activities.base.goToExternalUrl
import thearith.github.com.github_search.view.internal.di.components.ApplicationComponent
import thearith.github.com.github_search.view.model.viewevents.RecyclerViewScrollBottomEvent
import thearith.github.com.github_search.view.utils.bindView
import thearith.github.com.github_search.view.utils.isScrolledToBottom
import thearith.github.com.github_search.view.utils.setVisibility

/**
 * Main application screen. This is the app entry point.
 * */
class MainActivity : BaseActivity(), MainContract.View {

    // Views
    private val mSearchView: SearchView            by bindView(R.id.search_view)
    private val mSearchRecyclerView: RecyclerView  by bindView(R.id.rc_search)
    private val mSearchProgressBar: ProgressBar    by bindView(R.id.pb_search)
    private val mWelcomeView: LogoWithTextView     by bindView(R.id.view_welcome)
    private val mErrorView: LogoWithTextView       by bindView(R.id.view_error)
    private val mNoResultView: LogoWithTextView    by bindView(R.id.view_empty_result)
    private val mToolbar: AppBarLayout             by bindView(R.id.appbar_layout)

    // Presenter
    @Inject
    lateinit var mPresenter: MainPresenter

    // Adapter
    private val mSearchAdapter: GitHubSearchAdapter
            by lazy { GitHubSearchAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        updateUI(SearchFeedResponse(Status.IDLE))
    }

    override fun onStart() {
        super.onStart()
        setUpEventStreams()
    }

    override fun injectDependencies(appComponent: ApplicationComponent?) {
        appComponent?.inject(this)
    }


    /**
     * Sets up event streams
     * */
    private fun setUpEventStreams() {
        setUpSearchEventStream()
        setUpTitleClickStream()
    }

    /**
     * Sets up search event streams and handles according to GitHub search response
     * Search event stream can come from
     *      a SearchView query change (newSearchStream)
     *      a RecyclerView scroll (nextSearchStream)
     *
     * */
    private fun setUpSearchEventStream() {
        val newSearchStream = getNewSearchStream()
        val nextSearchStream = getNextSearchStream()
        val searchStream = Observable.merge(newSearchStream, nextSearchStream)

        val disposable = searchStream
                .subscribe(
                        { response -> updateUI(response) },
                        { handleError() }
                )

        addDisposable(disposable)
    }

    /**
     * Creates a GitHub search stream that originates from user's search query text change
     *
     * @return Observable<SearchFeedResponse>
     * */
    private fun getNewSearchStream(): Observable<SearchFeedResponse> {
        val searchViewTextChangeStream =
                RxSearchView.queryTextChanges(mSearchView)
                        .debounce(400, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .map { it.toString() }

        // SwitchMap is used to discard any previous search response
        // (as we only need the most recent search query)
        return searchViewTextChangeStream.switchMap {
            mPresenter.loadNewSearch(it)
        }
    }

    /**
     * Creates a GitHub search stream that originates from user's search result scroll to bottom
     * There will not be an event if user has scrolled to the end of the whole result list
     *
     * @return Observable<SearchFeedResponse>
     * */
    private fun getNextSearchStream(): Observable<SearchFeedResponse> {
        // RecyclerView stream of user's scrolling to bottom of the list
        val scrollStream =
                RxRecyclerView.scrollEvents(mSearchRecyclerView)
                        .filter { it.dy() > 0 }
                        .map {
                            val isBottom = it.view().isScrolledToBottom()
                            RecyclerViewScrollBottomEvent(it, isBottom)
                        }
                        .distinctUntilChanged { x, y -> x.isScrolledToBottom == y.isScrolledToBottom }
                        .filter { it.isScrolledToBottom }

        // RecyclerView stream of user's scrolling to bottom of the list
        // but has not reached the end of the whole GitHub result list
        val scrollableStream =
                scrollStream.filter {
                    val adapter = it.event.view().adapter as GitHubSearchAdapter
                    val isNextSearchable = !adapter.isSearchFull()

                    isNextSearchable
                }

        // FlatMap is used so that we do not discard previous search response
        return scrollableStream.flatMap {
            val searchParam = mSearchView.query.toString()
            mPresenter.loadNextSearch(searchParam)
        }
    }

    /**
     * Sets up a click stream that comes from user's clicking on a GitHub repo title
     * Clicking on a GitHub Repo title will navigate to its repository on GitHub
     * on an external browser (Custom Tab)
     *
     * */
    private fun setUpTitleClickStream() {
        val disposable = mSearchAdapter.getTitleClickStream()
                .subscribe { goToExternalUrl(it) }

        addDisposable(disposable)
    }

    private fun initRecyclerView() {
        mSearchRecyclerView.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        mSearchRecyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))

        mSearchRecyclerView.adapter = mSearchAdapter
    }

    /**
     * Updates UI based on GitHub search response
     *
     * @param response SearchFeedResponse object that comes from a GitHub search api request
     * */
    override fun updateUI(response: SearchFeedResponse) {
        when (response.status) {
            Status.IDLE                     -> showIdleMode()
            Status.IN_PROGRESS              -> showProgressMode()
            Status.IN_PROGRESS_WITH_REFRESH -> showProgressModeWithRefresh()
            Status.COMPLETE                 -> showCompleteMode(response)
            Status.NO_RESULT                -> showEmptyResult()
            Status.ERROR                    -> handleError()
        }
    }

    private fun showIdleMode() {
        updateUIVisibility(Status.IDLE)
        expandToolbar()
    }

    private fun showProgressMode() {
        updateUIVisibility(Status.IN_PROGRESS)
    }

    private fun showProgressModeWithRefresh() {
        updateUIVisibility(Status.IN_PROGRESS_WITH_REFRESH)
        mSearchAdapter.clearAll()
        expandToolbar()
    }

    private fun expandToolbar() {
        mToolbar.setExpanded(true, true)
    }

    private fun showCompleteMode(response: SearchFeedResponse) {
        updateUIVisibility(Status.COMPLETE)

        showResultCount(response.response?.totalCount)
        populateList(response.response)
    }

    private fun showResultCount(count: Int?) {
        mSearchAdapter.addHeaderCount(count)
    }

    private fun populateList(response: GitHubSearchModel?) {
        val searchItemList = response?.items
        mSearchAdapter.addData(searchItemList)
    }

    private fun showEmptyResult() {
        updateUIVisibility(Status.NO_RESULT)
        expandToolbar()
    }

    private fun handleError() {
        updateUIVisibility(Status.ERROR)
        expandToolbar()
    }

    /**
     * Decides what UI components to show based on the status of GitHub search response
     *
     * @param mode Status object of GitHub search api response
     * */
    private fun updateUIVisibility(mode: Status) {
        val viewList = listOf(mWelcomeView, mErrorView, mNoResultView, mSearchRecyclerView, mSearchProgressBar)
        viewList.forEach { it.setVisibility(false) }

        when (mode) {
            Status.IDLE -> {
                mWelcomeView.visibility = View.VISIBLE
            }

            Status.IN_PROGRESS,
            Status.IN_PROGRESS_WITH_REFRESH -> {
                mSearchProgressBar.visibility = View.VISIBLE
                mSearchRecyclerView.visibility = View.VISIBLE
            }

            Status.COMPLETE -> {
                mSearchRecyclerView.visibility = View.VISIBLE
            }

            Status.NO_RESULT -> {
                mNoResultView.visibility = View.VISIBLE
            }

            Status.ERROR -> {
                mErrorView.visibility = View.VISIBLE
            }
        }
    }

}
