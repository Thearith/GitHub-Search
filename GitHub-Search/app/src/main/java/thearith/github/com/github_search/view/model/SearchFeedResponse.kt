package thearith.github.com.github_search.view.model

import thearith.github.com.github_search.data.search.network.search.model.GitHubSearchModel

/**
 * Created by Thearith on 11/1/17.
 */

data class SearchFeedResponse (val status : Status,
                               val response : GitHubSearchModel? = null)