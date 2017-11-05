package thearith.github.com.github_search.view.adapter.viewholders

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import thearith.github.com.github_search.R
import thearith.github.com.github_search.view.utils.formatWithCommas
import thearith.github.com.github_search.view.utils.getString

class GitHubSearchHeaderViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        fun getLayoutResource() = R.layout.item_search_header
    }

    private val mSearchCountTextView : TextView
            by lazy { itemView?.findViewById(R.id.tv_search_count) as TextView }

    fun bind(count : Int?) {
        val resultStr = getString(R.string.search_result_repo_count)
        val resultCount = count?.formatWithCommas()
        val result = String.format(resultStr, resultCount)
        mSearchCountTextView.text = result
    }

}