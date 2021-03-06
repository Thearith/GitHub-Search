package thearith.github.com.github_search.view.adapter.viewholders;

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import thearith.github.com.github_search.R
import thearith.github.com.github_search.data.search.network.search.model.GitHubSearchItemModel
import thearith.github.com.github_search.view.utils.convertToMonthDayYearFormat
import thearith.github.com.github_search.view.utils.formatWithSuffix
import thearith.github.com.github_search.view.utils.getString

class GitHubSearchViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        fun getLayoutResource() = R.layout.item_search
    }

    // Views
    private val tvGitHubRepoName : TextView
            by lazy { itemView?.findViewById(R.id.tv_github_repo_name) as TextView }

    private val tvGitHubRepoDescription : TextView
            by lazy { itemView?.findViewById(R.id.tv_github_description) as TextView }

    private val tvGitHubRepoLastUpdated : TextView
            by lazy { itemView?.findViewById(R.id.tv_github_last_updated) as TextView }

    private val tvGitHubRepoStar : TextView
            by lazy { itemView?.findViewById(R.id.tv_github_repo_star) as TextView }


    fun bind(data : GitHubSearchItemModel) {
        val title = data.fullName
        val description = data.description
        val updatedAtFormat = getString(R.string.search_result_updated_at)
        val updatedAtDate = data.updatedAt.convertToMonthDayYearFormat()
        val updatedAt = String.format(updatedAtFormat, updatedAtDate)
        val stars = data.stars.formatWithSuffix()

        tvGitHubRepoName.text = title
        tvGitHubRepoDescription.text = description
        tvGitHubRepoLastUpdated.text = updatedAt
        tvGitHubRepoStar.text = stars

        tvGitHubRepoName.tag = data.htmlUrl
    }

    fun getGitHubTitleClickStream() : Observable<String> =
            RxView.clicks(tvGitHubRepoName)
                    .map { tvGitHubRepoName }
                    .map { it.tag as String }
}