package thearith.github.com.github_search.data.search.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import thearith.github.com.github_search.data.search.network.search.model.GitHubSearchModel

/**
 * A Retrofit-based interface that represents GitHub Search requests
 */
interface GitHubSearchService {

    @GET("search/repositories")
    fun searchGitHubRepo(
        @Query("q")         searchParam : String,
        @Query("page")      page : Int,
        @Query("sort")      sort : String,
        @Query("order")     order : String,
        @Query("per_page")  perPage : Int ) : Observable<GitHubSearchModel>
}