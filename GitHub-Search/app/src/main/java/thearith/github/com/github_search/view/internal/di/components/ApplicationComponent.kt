package thearith.github.com.github_search.view.internal.di.components


import android.content.Context

import dagger.Component
import thearith.github.com.github_search.data.internal.di.modules.DataModule
import thearith.github.com.github_search.presentation.internal.di.modules.PresentationModule
import thearith.github.com.github_search.view.internal.di.ApplicationScope
import thearith.github.com.github_search.view.internal.di.modules.ApplicationModule
import thearith.github.com.github_search.view.activities.search.MainActivity

/**
 * A component whose lifetime is the life of the application.
 */

@ApplicationScope
@Component(modules = arrayOf(ApplicationModule::class, PresentationModule::class, DataModule::class))
interface ApplicationComponent {

    fun context(): Context

    fun inject(activity : MainActivity)

    // Inject your dependencies here
}
