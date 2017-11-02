package thearith.github.com.github_search.presentation.presenter.base

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import thearith.github.com.github_search.presentation.schedulers.PostExecutionThread
import thearith.github.com.github_search.presentation.schedulers.ThreadExecutor

/**
 * [Presenter] that controls communication between views and models of the presentation
 * layer.
 *
 * This class should be the super class of all [Presenter] classes.
 */
abstract class BasePresenter(private val mThreadExecutor: ThreadExecutor, private val mPostExecutionThread: PostExecutionThread) {

    protected fun <T> Observable<T>.applySchedulers() =
            this.subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler())

}
