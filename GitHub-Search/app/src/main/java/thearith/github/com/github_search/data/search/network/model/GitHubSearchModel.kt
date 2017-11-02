package thearith.github.com.github_search.data.search.network.search.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Thearith on 10/31/17.
 */

data class GitHubSearchModel(
        @SerializedName("total_count")          val totalCount : Int,
        @SerializedName("incomplete_results")   val incompleteResults: Boolean,
        @SerializedName("items")                val items : List<GitHubSearchItemModel>?
)