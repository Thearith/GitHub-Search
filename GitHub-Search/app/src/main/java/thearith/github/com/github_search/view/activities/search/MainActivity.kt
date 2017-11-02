package thearith.github.com.github_search.view.activities.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import com.astro.astro.views.utils.bindView
import com.astro.astro.views.utils.isAtBottom
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.widget.RxSearchView
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



class MainActivity : BaseActivity(), MainContract.View {

    // Views
    private val mSearchView : SearchView            by bindView(R.id.search_view)
    private val mSearchRecyclerView : RecyclerView  by bindView(R.id.rc_search)
    private val mSearchCountTextView : TextView     by bindView(R.id.tv_search_count)
    private val mSearchCountBorder : View           by bindView(R.id.tv_search_count_border)
    private val mWelcomeView : LogoWithTextView     by bindView(R.id.view_welcome)
    private val mErrorView : LogoWithTextView       by bindView(R.id.view_error)
    private val mNoResultView : LogoWithTextView    by bindView(R.id.view_empty_result)

    // Presenter
    @Inject
    lateinit var mPresenter : MainPresenter

    // Adapter
    private val mSearchAdapter : GitHubSearchAdapter by lazy { GitHubSearchAdapter() }

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


    /**
     * Sets up event streams
     * */

    private fun setUpEventStreams() {
        setUpSearchViewTextChangeStream()
        setUpRecyclerViewScrollStream()
    }

    private fun setUpSearchViewTextChangeStream() {
        val searchViewTextChangeStream =
                RxSearchView.queryTextChanges(mSearchView)
                        .debounce(100, TimeUnit.MILLISECONDS)
                        .map { it.toString() }

        val newSearchStream = searchViewTextChangeStream
                .switchMap { mPresenter.loadNewSearch(it) }

        val disposable = newSearchStream
                    .subscribe(
                            { response -> updateUI(response) },
                            { error -> handleError(error.localizedMessage) }
                    )

        addDisposable(disposable)
    }

    private fun setUpRecyclerViewScrollStream() {
        val recyclerViewScrollStream =
                RxRecyclerView.scrollStateChanges(mSearchRecyclerView)
                        .map { mSearchRecyclerView.isAtBottom() }
                        .distinctUntilChanged()
                        .filter { it }

        val nextSearchStream = recyclerViewScrollStream
                .switchMap {
                    val searchParam = mSearchView.query.toString()
                    mPresenter.loadNextSearch(searchParam)
                }

        val disposable = nextSearchStream
                        .subscribe(
                                { response -> updateUI(response) },
                                { error -> handleError(error.localizedMessage) }
                        )

        addDisposable(disposable)
    }

    private fun initRecyclerView() {
        val lLayout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mSearchRecyclerView.setHasFixedSize(true)
        mSearchRecyclerView.setLayoutManager(lLayout)

        mSearchRecyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))

        mSearchRecyclerView.adapter = mSearchAdapter
    }

    override fun updateUI(response: SearchFeedResponse) {
        when(response.status) {
            Status.IDLE                     -> updateUIInIdleMode()
            Status.IN_PROGRESS              -> updateUIInProgressMode(false)
            Status.IN_PROGRESS_WITH_REFRESH -> updateUIInProgressMode(true)
            Status.COMPLETE                 -> updateUIInCompleteMode(response)
            Status.NO_RESULT                -> showEmptyResult("EMPTY LOL")
            Status.ERROR                    -> handleError("Status error")
        }
    }

    private fun updateUIInIdleMode() {
        mWelcomeView.visibility = View.VISIBLE
        mErrorView.visibility = View.GONE
        mNoResultView.visibility = View.GONE
        mSearchCountTextView.visibility = View.GONE
        mSearchCountBorder.visibility = View.GONE
        mSearchRecyclerView.visibility = View.GONE
    }

    private fun updateUIInProgressMode(isRefresh : Boolean) {
        mWelcomeView.visibility = View.GONE
        mErrorView.visibility = View.GONE
        mNoResultView.visibility = View.GONE
        mSearchCountTextView.visibility = View.VISIBLE
        mSearchCountBorder.visibility = View.VISIBLE
        mSearchRecyclerView.visibility = View.VISIBLE

        showLoading(true)

        if(isRefresh) {
            mSearchAdapter.clearAll()
        }
    }

    private fun showLoading(isShown : Boolean) {

    }

    private fun updateUIInCompleteMode(response: SearchFeedResponse) {
        mWelcomeView.visibility = View.GONE
        mErrorView.visibility = View.GONE
        mNoResultView.visibility = View.GONE
        mSearchCountTextView.visibility = View.VISIBLE
        mSearchCountBorder.visibility = View.VISIBLE
        mSearchRecyclerView.visibility = View.VISIBLE

        showResultCount(response.response?.totalCount)
        populateList(response.response)
    }

    private fun showResultCount(count : Int?) {
        val resultStr = getString(R.string.search_result_repo_count)
        val result = String.format(resultStr, count)
        mSearchCountTextView.text = result
    }

    private fun populateList(response : GitHubSearchModel?) {
        val searchItemList = response?.items
        mSearchAdapter.addData(searchItemList)
    }

    private fun showEmptyResult(message : String) {
        mWelcomeView.visibility = View.GONE
        mErrorView.visibility = View.GONE
        mNoResultView.visibility = View.VISIBLE
        mSearchCountTextView.visibility = View.GONE
        mSearchCountBorder.visibility = View.GONE
        mSearchRecyclerView.visibility = View.GONE
    }

    override fun handleError(errorMsg : String) {
        mWelcomeView.visibility = View.GONE
        mErrorView.visibility = View.VISIBLE
        mNoResultView.visibility = View.GONE
        mSearchCountTextView.visibility = View.GONE
        mSearchCountBorder.visibility = View.GONE
        mSearchRecyclerView.visibility = View.GONE
    }

}
