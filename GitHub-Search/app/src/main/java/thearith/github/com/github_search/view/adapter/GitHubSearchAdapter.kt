package thearith.github.com.github_search.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.astro.astro.views.utils.inflate
import thearith.github.com.github_search.R
import thearith.github.com.github_search.data.search.network.search.model.GitHubSearchItemModel

/**
 * Created by Thearith on 11/1/17.
 */
class GitHubSearchAdapter() :
        RecyclerView.Adapter<GitHubSearchAdapter.GitHubSearchViewHolder>() {

    private var mData : MutableList<GitHubSearchItemModel> = mutableListOf()

    fun addData(data : List<GitHubSearchItemModel>?) {
        if(data?.isNotEmpty() == true) {
            val toAddPos = mData.size
            val addedSize = data.size
            mData.addAll(data)

            notifyItemRangeInserted(toAddPos, addedSize)
        }
    }

    fun clearAll() {
        val size = mData.size
        mData.clear()

        notifyItemRangeRemoved(0, size)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: GitHubSearchViewHolder?, position: Int) {
        val data = mData.get(position)

        holder?.tvGitHubRepoName?.setText(data.fullName)
        holder?.tvGitHubRepoDescription?.setText(data.description)
        holder?.tvGitHubRepoLastUpdated?.setText(data.updatedAt)
        holder?.tvGitHubRepoStar?.setText(data.stars.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) : GitHubSearchViewHolder {
        val view = parent?.inflate(R.layout.item_search)
        return GitHubSearchViewHolder(view)
    }

    class GitHubSearchViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        val tvGitHubRepoName : TextView
                by lazy { itemView?.findViewById(R.id.tv_github_repo_name) as TextView }

        val tvGitHubRepoDescription : TextView
                by lazy { itemView?.findViewById(R.id.tv_github_description) as TextView }

        val tvGitHubRepoLastUpdated : TextView
                by lazy { itemView?.findViewById(R.id.tv_github_last_updated) as TextView }

        val tvGitHubRepoStar : TextView
                by lazy { itemView?.findViewById(R.id.tv_github_repo_star) as TextView }
    }

}