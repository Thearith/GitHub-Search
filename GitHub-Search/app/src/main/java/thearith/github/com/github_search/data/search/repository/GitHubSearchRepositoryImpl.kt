package thearith.github.com.github_search.data.search.repository

import thearith.github.com.github_search.data.search.network.GitHubSearchApi
import javax.inject.Inject

/**
 * GitHubSearchRepository for fetching GitHubSearchModel
 * This class coordinates between various data sources (API, Local Db, Memory)
 */
class GitHubSearchRepositoryImpl : GitHubSearchRepository {

    val mApiSource : GitHubSearchApi

    @Inject
    constructor(apiSource : GitHubSearchApi) {
        mApiSource = apiSource
    }

    override fun searchGitHubRepo(searchParam: String,
                                  page: Int,
                                  sort: String,
                                  order: String,
                                  perPage: Int) =
            mApiSource.searchGitHubRepo(searchParam, page, sort, order, perPage)
}