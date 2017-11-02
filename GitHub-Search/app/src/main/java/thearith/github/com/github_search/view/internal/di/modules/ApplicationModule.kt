package thearith.github.com.github_search.view.internal.di.modules

import android.content.Context

import dagger.Module
import dagger.Provides
import thearith.github.com.github_search.view.internal.di.ApplicationScope

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */

@Module
class ApplicationModule(private val mApplicationContext: Context) {

    @Provides
    @ApplicationScope
    internal fun provideContext(): Context {
        return mApplicationContext
    }


    // ---------------------------------------------------------
    // @Provide Dependency methods related to Presentation layer
    // should be provided here
    // ---------------------------------------------------------
}
