package com.astro.astro.views.utils

import android.app.Activity
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Thearith on 9/25/17.
 */

fun ViewGroup.inflate(@LayoutRes layoutId : Int) =
        LayoutInflater.from(context).inflate(layoutId, this, false)

fun <V : View> Activity.bindView(@IdRes resId : Int) : Lazy<V> {
    @Suppress("UNCHECKED_CAST")
    return lazy(LazyThreadSafetyMode.NONE){ findViewById(resId) as V }
}

fun View.setVisibility(visible : Boolean) {
    visibility = if(visible) View.VISIBLE else View.GONE
}

fun RecyclerView.isAtBottom() =
        !ViewCompat.canScrollVertically(this, 1)