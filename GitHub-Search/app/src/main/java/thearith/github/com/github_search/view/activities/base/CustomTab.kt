package thearith.github.com.github_search.view.activities.base

import android.app.Activity
import android.net.Uri
import android.support.customtabs.CustomTabsIntent


/**
 * A utility class for navigating user to an external browser using Custom Tab
 */

fun Activity.goToExternalUrl(url : String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}