package thearith.github.com.github_search.view.model.viewevents

import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent

/**
 * A class that represents a RecyclerView scroll event and its scrolling status
 */
data class RecyclerViewScrollBottomEvent (
    val event : RecyclerViewScrollEvent,
    val isScrolledToBottom : Boolean
)