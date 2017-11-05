package thearith.github.com.github_search.view.utils

import android.app.Activity
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


fun ViewGroup.inflate(@LayoutRes layoutId : Int) : View? =
        LayoutInflater.from(context).inflate(layoutId, this, false)

fun <V : View> Activity.bindView(@IdRes resId : Int) : Lazy<V> {
    @Suppress("UNCHECKED_CAST")
    return lazy(LazyThreadSafetyMode.NONE){ findViewById(resId) as V }
}

fun View.setVisibility(visible : Boolean) {
    visibility = if(visible) View.VISIBLE else View.GONE
}

fun RecyclerView.ViewHolder.getString(@StringRes stringRes: Int) =
        itemView.context.getString(stringRes)

/**
 * Checks if a RecyclerView is already scrolled the bottom
 *
 * @return a Boolean object of the specified value
 * */
fun RecyclerView.isScrolledToBottom() =
        !ViewCompat.canScrollVertically(this, 1)