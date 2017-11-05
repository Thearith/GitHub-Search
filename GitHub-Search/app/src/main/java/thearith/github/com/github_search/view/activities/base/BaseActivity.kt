package thearith.github.com.github_search.view.activities.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import thearith.github.com.github_search.view.internal.di.components.ApplicationComponent


/**
 * Base Activity class for every Activity in this application.
 *
 */
abstract class BaseActivity : AppCompatActivity() {

    protected val mDisposables: CompositeDisposable by lazy { CompositeDisposable() }

    protected abstract fun injectDependencies(appComponent : ApplicationComponent?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = getApplicationComponent()
        injectDependencies(appComponent)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyDisposables()
    }

    private fun getApplicationComponent() =
            (application as BaseApplication).applicationComponent

    protected fun addDisposable(disposable: Disposable) {
        mDisposables.add(disposable)
    }

    private fun destroyDisposables() {
        if (!mDisposables.isDisposed) {
            mDisposables.dispose()
        }
    }
}
