package thearith.github.com.github_search.view.activities.search

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.astro.astro.views.utils.bindView
import com.astro.astro.views.utils.isAtBottom
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
import android.widget.Toolbar
import com.astro.astro.views.utils.setVisibility
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.Observable
import thearith.github.com.github_search.view.activities.base.goToExternalUrl

class MainActivity : BaseActivity(), MainContract.View, GitHubSearchAdapter.OnClickListener {

    // Views
    private val mSearchView : SearchView            by bindView(R.id.search_view)
    private val mSearchRecyclerView : RecyclerView  by bindView(R.id.rc_search)
    private val mSearchProgressBar : ProgressBar    by bindView(R.id.pb_search)
    private val mWelcomeView : LogoWithTextView     by bindView(R.id.view_welcome)
    private val mErrorView : LogoWithTextView       by bindView(R.id.view_error)
    private val mNoResultView : LogoWithTextView    by bindView(R.id.view_empty_result)
    private val mToolbar : AppBarLayout             by bindView(R.id.appbar_layout)

    // Presenter
    @Inject
    lateinit var mPresenter : MainPresenter

    // Adapter
    private val mSearchAdapter : GitHubSearchAdapter
            by lazy {
                GitHubSearchAdapter().apply {
                    setOnClickListener(this@MainActivity)
                }
            }

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

    override fun injectDependencies() {
        val appComponent = getApplicationComponent()
        appComponent?.inject(this)
    }

    override fun onTitleClick(url: String) {
        goToExternalUrl(url)
    }


    /**
     * Sets up event streams
     * */

    private fun setUpEventStreams() {
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

    private fun getNewSearchStream() : Observable<SearchFeedResponse> {
        val searchViewTextChangeStream =
                RxSearchView.queryTextChanges(mSearchView)
                        .debounce(400, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .map { it.toString() }

        return searchViewTextChangeStream.switchMap { mPresenter.loadNewSearch(it) }
    }

    private fun getNextSearchStream() : Observable<SearchFeedResponse> {
        val recyclerViewScrollStream =
                RxRecyclerView.scrollEvents(mSearchRecyclerView)
                        .filter { it.dy() > 0 }
                        .map {
                            val recyclerView = it.view()
                            val adapter = recyclerView.adapter as GitHubSearchAdapter

                            val isScrolledToBottom = recyclerView.isAtBottom()
                            val isNextSearchable = !adapter.isSearchFull()

                            isScrolledToBottom && isNextSearchable
                        }
                        .distinctUntilChanged()
                        .filter { it }

        return recyclerViewScrollStream.switchMap {
                    val searchParam = mSearchView.query.toString()
                    mPresenter.loadNextSearch(searchParam)
                }
    }

    private fun initRecyclerView() {
        mSearchRecyclerView.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        mSearchRecyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))

        mSearchRecyclerView.adapter = mSearchAdapter
    }

    override fun updateUI(response: SearchFeedResponse) {
        when(response.status) {
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

    private fun showResultCount(count : Int?) {
        mSearchAdapter.addHeaderCount(count)
    }

    private fun populateList(response : GitHubSearchModel?) {
        val searchItemList = response?.items
        mSearchAdapter.addData(searchItemList)
    }

    private fun showEmptyResult() {
        updateUIVisibility(Status.NO_RESULT)
    }

    private fun handleError() {
        updateUIVisibility(Status.ERROR)
    }

    private fun updateUIVisibility(mode : Status) {
        val viewList = listOf(mWelcomeView, mErrorView, mNoResultView, mSearchRecyclerView, mSearchProgressBar)
        viewList.forEach { it.setVisibility(false) }

        when(mode) {
            Status.IDLE         -> mWelcomeView.visibility = View.VISIBLE

            Status.IN_PROGRESS,
            Status.IN_PROGRESS_WITH_REFRESH -> {
                    mSearchRecyclerView.visibility = View.VISIBLE
                    mSearchProgressBar.visibility = View.VISIBLE
            }

            Status.COMPLETE     -> mSearchRecyclerView.visibility = View.VISIBLE

            Status.NO_RESULT    -> mNoResultView.visibility = View.VISIBLE
            Status.ERROR        -> mErrorView.visibility = View.VISIBLE
        }
    }

}
