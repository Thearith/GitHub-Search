package thearith.github.com.github_search.presentation.presenter.base


/**
 * Interface representing a Presenter in a model view presenter (MVP) pattern.
 */
interface Presenter {

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onDestroy() method.
     */
    fun destroy()
}
