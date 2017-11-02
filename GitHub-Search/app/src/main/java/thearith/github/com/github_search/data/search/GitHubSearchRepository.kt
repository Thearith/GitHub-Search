package thearith.github.com.github_search.data.search.repository

import io.reactivex.Observable
import thearith.github.com.github_search.data.search.network.search.model.GitHubSearchModel

/**
 * Created by Thearith on 10/31/17.
 */
interface GitHubSearchRepository {

    fun searchGitHubRepo(
        searchParam : String,
        page : Int,
        sort : String = "",
        order : String = "desc",
        perPage : Int = 10 ) : Observable<GitHubSearchModel>
}