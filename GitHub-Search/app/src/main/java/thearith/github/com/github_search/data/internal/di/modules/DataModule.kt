package thearith.github.com.github_search.data.internal.di.modules

import dagger.Module
import dagger.Provides
import thearith.github.com.github_search.data.search.repository.GitHubSearchRepository
import thearith.github.com.github_search.data.search.repository.GitHubSearchRepositoryImpl
import thearith.github.com.github_search.view.internal.di.ApplicationScope
import thearith.github.com.github_search.view.internal.di.modules.ApplicationModule

/**
 * Module that controls dependencies for Data
 */

@Module(includes = arrayOf(ApplicationModule::class))
class DataModule  {

    // ---------------------------------------------------------
    // @Provide Dependency methods related to DOMAIN layer
    // should be provided here
    // ---------------------------------------------------------

    @Provides
    @ApplicationScope
    fun providesGitHubSearchRepo(githubSearchRepoImpl : GitHubSearchRepositoryImpl)
            : GitHubSearchRepository {
        return githubSearchRepoImpl
    }
}
