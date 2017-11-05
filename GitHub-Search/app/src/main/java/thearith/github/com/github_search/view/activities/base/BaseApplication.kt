package thearith.github.com.github_search.view.activities.base

import android.app.Application

import com.squareup.leakcanary.BuildConfig
import com.squareup.leakcanary.LeakCanary

import thearith.github.com.github_search.view.internal.di.components.ApplicationComponent
import thearith.github.com.github_search.view.internal.di.components.DaggerApplicationComponent
import thearith.github.com.github_search.view.internal.di.modules.ApplicationModule

/**
 * Android Main Application
 */
class BaseApplication : Application() {

    var applicationComponent: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()

        initLeakCanary()
        initDependencies()
    }

    /**
     * Initializes Leak Canary (for monitoring leak memory)
     */
    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

        LeakCanary.install(this)
    }


    /**
     * Initializes Dagger 2 dependencies
     */
    private fun initDependencies() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}
