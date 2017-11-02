package thearith.github.com.github_search.data.search.network

import io.reactivex.Observable
import thearith.github.com.github_search.data.base.network.getRetrofitClient
import thearith.github.com.github_search.data.search.datasource.GitHubSearchDataSource
import thearith.github.com.github_search.data.search.network.search.model.GitHubSearchModel
import thearith.github.com.github_search.view.internal.di.ApplicationScope
import javax.inject.Inject

/**
 * Created by Thearith on 10/31/17.
 */
@ApplicationScope
class GitHubSearchApi @Inject constructor() : GitHubSearchDataSource {

    override fun searchGitHubRepo(searchParam: String,
                                  page: Int,
                                  sort: String,
                                  order: String,
                                  perPage: Int) : Observable<GitHubSearchModel> {
        val retrofit = getRetrofitClient("https://api.github.com")
        val gitHubSearchService = retrofit.create(GitHubSearchService::class.java)
        return gitHubSearchService.searchGitHubRepo(searchParam, page, sort, order, perPage)
    }

}