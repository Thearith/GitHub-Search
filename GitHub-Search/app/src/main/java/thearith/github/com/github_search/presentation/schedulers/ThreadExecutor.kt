package thearith.github.com.github_search.presentation.schedulers

import java.util.concurrent.Executor

/**
 * Executor implementation can be based on different frameworks or techniques of asynchronous
 * execution, but every implementation will execute out of the UI thread.
 */
interface ThreadExecutor : Executor