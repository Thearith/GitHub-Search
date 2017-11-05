package thearith.github.com.github_search.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.subjects.PublishSubject
import thearith.github.com.github_search.R
import thearith.github.com.github_search.data.search.network.search.model.GitHubSearchItemModel
import thearith.github.com.github_search.view.adapter.viewholders.GitHubSearchHeaderViewHolder
import thearith.github.com.github_search.view.adapter.viewholders.GitHubSearchViewHolder
import thearith.github.com.github_search.view.utils.inflate

/**
 * Adapter which manages a collection of GitHubSearchItemModel
 * This adapter has two view types including a Search count Header view and invididual Search Item view
 * */
class GitHubSearchAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mCount : Int = -1
    private var mData : MutableList<GitHubSearchItemModel> = mutableListOf()

    private var mTitleClickSubject : PublishSubject<String> = PublishSubject.create()

    /**
     * Adds a search count header view
     * Header is always at the top of the list
     *
     * @param count Int nullable object
     * */
    fun addHeaderCount(count : Int?) {
        if(count != null) {
            mCount = count
            notifyItemChanged(0)
        }
    }

    /**
     * Appends a new list of GitHub search items to existing list
     *
     * @param data List<GitHubSearchItemModel>
     * */
    fun addData(data : List<GitHubSearchItemModel>?) {
        if(data?.isNotEmpty() == true) {
            val toAddPos = mData.size + 1 // including Header
            val addedSize = data.size
            mData.addAll(data)

            notifyItemRangeInserted(toAddPos, addedSize)
        }
    }

    /**
     * Clears both Header and all existing Search items
     *
     * */
    fun clearAll() {
        val size = mData.size
        mData.clear()
        mCount = -1

        notifyItemRangeRemoved(0, size + 1)
    }

    /**
     * Checks if current search list has already contains all of Search results
     *
     * @return a Boolean object of the specified value
     * */
    fun isSearchFull() = mData.size == mCount

    /**
     * Returns a stream of GitHub html urls which originates from the clicks on
     * its respective GitHub repo title
     *
     * @return a Observable<String> object of the specified value
     * */
    fun getTitleClickStream() = mTitleClickSubject


    override fun getItemCount() =
            if(mCount > 0)  mData.size + 1
            else            0

    override fun getItemViewType(position: Int) =
            if(position == 0)
                GitHubSearchHeaderViewHolder.getLayoutResource()
            else
                GitHubSearchViewHolder.getLayoutResource()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) : RecyclerView.ViewHolder =
            when(viewType) {
                R.layout.item_search_header -> onCreateSearchHeaderViewHolder(parent)
                else                        -> onCreateSearchItemViewHolder(parent)
            }

    private fun onCreateSearchHeaderViewHolder(parent: ViewGroup?) : RecyclerView.ViewHolder {
        val view = parent?.inflate(R.layout.item_search_header)
        return GitHubSearchHeaderViewHolder(view)
    }

    private fun onCreateSearchItemViewHolder(parent : ViewGroup?) : RecyclerView.ViewHolder {
        val view = parent?.inflate(R.layout.item_search)
        val viewHolder = GitHubSearchViewHolder(view)

        viewHolder.getGitHubTitleClickStream()
                .takeUntil(RxView.detaches(parent as View))
                .subscribe(mTitleClickSubject)

        return viewHolder
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when(position) {
            0       -> bindSearchHeaderViewHolder(holder)
            else    -> bindSearchItemViewHolder(holder, position)
        }
    }

    private fun bindSearchHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val searchHeaderHolder = holder as GitHubSearchHeaderViewHolder
        searchHeaderHolder.bind(mCount)
    }

    private fun bindSearchItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val searchItemHolder = holder as GitHubSearchViewHolder
        val data = mData[position - 1]
        searchItemHolder.bind(data)
    }


}