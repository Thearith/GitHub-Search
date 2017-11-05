package thearith.github.com.github_search.data.internal.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import thearith.github.com.github_search.data.search.repository.GitHubSearchRepository
import thearith.github.com.github_search.data.search.repository.GitHubSearchRepositoryImpl
import thearith.github.com.github_search.data.utils.Constants
import thearith.github.com.github_search.view.internal.di.ApplicationScope
import thearith.github.com.github_search.view.internal.di.modules.ApplicationModule
import java.io.File

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
    fun providesHttpLogInterceptor() : HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return loggingInterceptor
    }

    @Provides
    @ApplicationScope
    fun providesCacheFile(context : Context) =
            File(context.cacheDir, Constants.CACHE_FILE_NAME)

    @Provides
    @ApplicationScope
    fun providesCache(cacheFile : File) =
            Cache(cacheFile, Constants.CACHE_SIZE)


    @Provides
    @ApplicationScope
    fun providesHttpClient(loggingInterceptor: HttpLoggingInterceptor,
                           cache : Cache) : OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .cache(cache)
                    .build()

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
