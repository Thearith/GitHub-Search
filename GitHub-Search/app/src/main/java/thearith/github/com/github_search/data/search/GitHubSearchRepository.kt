package thearith.github.com.github_search.data.search.repository

import io.reactivex.Observable
import thearith.github.com.github_search.data.search.network.search.model.GitHubSearchModel

/**
 * Interface that represents a Repository for getting {@link GitHubSearchModel} related data.
 */
interface GitHubSearchRepository {

    fun searchGitHubRepo(
        searchParam : String,
        page : Int,
        sort : String = "",
        order : String = "desc",
        perPage : Int = 10 ) : Observable<GitHubSearchModel>
}