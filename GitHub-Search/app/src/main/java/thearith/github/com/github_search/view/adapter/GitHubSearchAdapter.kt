package thearith.github.com.github_search.view.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.astro.astro.views.utils.inflate
import thearith.github.com.github_search.R
import thearith.github.com.github_search.data.search.network.search.model.GitHubSearchItemModel
import thearith.github.com.github_search.view.adapter.viewholders.GitHubSearchHeaderViewHolder
import thearith.github.com.github_search.view.adapter.viewholders.GitHubSearchViewHolder
import thearith.github.com.github_search.view.utils.convertToMonthDayYearFormat
import thearith.github.com.github_search.view.utils.formatWithSuffix

/**
 * Created by Thearith on 11/1/17.
 */
class GitHubSearchAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mCount : Int = -1
    private var mData : MutableList<GitHubSearchItemModel> = mutableListOf()

    fun addHeaderCount(count : Int?) {
        if(count != null) {
            mCount = count
            notifyItemChanged(0)
        }
    }

    fun addData(data : List<GitHubSearchItemModel>?) {
        if(data?.isNotEmpty() == true) {
            val toAddPos = mData.size + 1
            val addedSize = data.size
            mData.addAll(data)

            notifyItemRangeInserted(toAddPos, addedSize)
        }
    }

    fun clearAll() {
        val size = mData.size
        mData.clear()
        mCount = -1

        notifyItemRangeRemoved(0, size + 1)
    }

    override fun getItemCount() =
            if(mCount > 0)  mData.size + 1
            else            0

    override fun getItemViewType(position: Int) =
            if(position == 0) GitHubSearchHeaderViewHolder.getLayoutResource()
            else GitHubSearchViewHolder.getLayoutResource()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when(position) {
            0 -> {
                val searchHeaderHolder = holder as GitHubSearchHeaderViewHolder
                searchHeaderHolder.bind(mCount)
            }

            else -> {
                val searchItemHolder = holder as GitHubSearchViewHolder
                val data = mData[position - 1]
                searchItemHolder.bind(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) : RecyclerView.ViewHolder =
            when(viewType) {
                R.layout.item_search_header ->  {
                    val view = parent?.inflate(R.layout.item_search_header)
                    GitHubSearchHeaderViewHolder(view)
                }

                else -> {
                    val view = parent?.inflate(R.layout.item_search)
                    GitHubSearchViewHolder(view)
                }
            }

}