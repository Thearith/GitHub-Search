package thearith.github.com.github_search.view.model

/**
 * Enum class that represents API response status
 */
enum class Status {

    /**
     * Idle State
     * */
    IDLE,

    /**
     * Progress State
     * */
    IN_PROGRESS,

    /**
     * Progress State with the need to refresh or clear UI
     * */
    IN_PROGRESS_WITH_REFRESH,

    /**
     * Complete State with results
     * */
    COMPLETE,

    /**
     * Complete State with NO results
     * */
    NO_RESULT,

    /**
     * Error State (ex: Connection error, Error 403, etc)
     * */
    ERROR
}