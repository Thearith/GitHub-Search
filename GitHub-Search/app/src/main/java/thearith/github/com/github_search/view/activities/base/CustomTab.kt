package thearith.github.com.github_search.view.activities.base

import android.app.Activity
import android.net.Uri
import android.support.customtabs.CustomTabsIntent



/**
 * Created by Thearith on 11/3/17.
 */

fun Activity.goToExternalUrl(url : String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}