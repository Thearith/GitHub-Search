package thearith.github.com.github_search.presentation.internal.di.modules

import dagger.Module
import dagger.Provides
import thearith.github.com.github_search.presentation.schedulers.PostExecutionThread
import thearith.github.com.github_search.presentation.schedulers.ThreadExecutor
import thearith.github.com.github_search.presentation.schedulers.impl.JobExecutor
import thearith.github.com.github_search.presentation.schedulers.impl.UIThread
import thearith.github.com.github_search.view.internal.di.ApplicationScope
import thearith.github.com.github_search.view.internal.di.modules.ApplicationModule


/**
 * A module that controls all dependencies of Presentation
 */

@Module(includes = arrayOf(ApplicationModule::class))
class PresentationModule {

    @Provides
    @ApplicationScope
    internal fun provideThreadExecutor(jobExecutor: JobExecutor) : ThreadExecutor {
        return jobExecutor
    }

    @Provides
    @ApplicationScope
    internal fun providePostExecutionThread(uiThread: UIThread): PostExecutionThread {
        return uiThread
    }


    // ---------------------------------------------------------
    // @Provide Dependency methods related to Presentation layer
    // should be provided here
    // ---------------------------------------------------------

}
