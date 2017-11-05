package thearith.github.com.github_search.data.search.datasource

import io.reactivex.Observable
import thearith.github.com.github_search.data.search.network.search.model.GitHubSearchModel

/**
 * Interface that represents a Data Source for getting {@link GitHubSearchModel} related data.
 * Every data source (API, Memory, Local Database) that needs to fetch GitHubSearchModel must implement
 * this interface.
 *
 */
interface GitHubSearchDataSource {

    fun searchGitHubRepo(
            searchParam : String,
            page : Int,
            sort : String = "",
            order : String = "desc",
            perPage : Int = 10) : Observable<GitHubSearchModel>
}