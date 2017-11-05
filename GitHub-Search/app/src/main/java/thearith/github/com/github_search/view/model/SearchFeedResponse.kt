package thearith.github.com.github_search.view.model

import thearith.github.com.github_search.data.search.network.search.model.GitHubSearchModel

/**
 * A class that represents a GitHub search response and its Status
 */

data class SearchFeedResponse (val status : Status,
                               val response : GitHubSearchModel? = null)