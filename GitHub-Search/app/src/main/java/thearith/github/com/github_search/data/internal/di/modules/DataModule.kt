package thearith.github.com.github_search.data.internal.di.modules

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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

    @Provides
    @ApplicationScope
    fun providesConverterFactory() : Converter.Factory =
            GsonConverterFactory.create()

    @Provides
    @ApplicationScope
    fun providesAdapterFactory() : CallAdapter.Factory =
            RxJava2CallAdapterFactory.create()

    @Provides
    @ApplicationScope
    fun providesHttpLogIncepter() : HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return loggingInterceptor
    }

    @Provides
    @ApplicationScope
    fun providesHttpClient(loggingInterceptor: HttpLoggingInterceptor) : OkHttpClient =
            OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    @Provides
    @ApplicationScope
    fun providesRetrofitBuilder(httpClient : OkHttpClient,
                                converterFactory: Converter.Factory,
                                adatperFactory : CallAdapter.Factory) : Retrofit.Builder =
            Retrofit.Builder()
                .client(httpClient)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(adatperFactory)
}
