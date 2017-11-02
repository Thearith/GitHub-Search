package thearith.github.com.github_search.presentation.schedulers.impl

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import thearith.github.com.github_search.presentation.schedulers.PostExecutionThread
import thearith.github.com.github_search.view.internal.di.ApplicationScope
import javax.inject.Inject

/**
 * MainThread (UI Thread) implementation based on a [Scheduler]
 * which will execute actions on the Android UI thread
 */
@ApplicationScope
class UIThread @Inject constructor() : PostExecutionThread {

    override fun getScheduler() = AndroidSchedulers.mainThread()

}
