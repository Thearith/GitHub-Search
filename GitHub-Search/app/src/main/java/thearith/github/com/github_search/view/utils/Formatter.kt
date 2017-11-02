package thearith.github.com.github_search.view.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Thearith on 11/2/17.
 */

fun String.convertToMonthDayYearFormat() : String {
    val utcFormat = "yyyy-MM-dd'T'hh:mm:ss'Z'"
    val monthDayYearFormat = "dd MMM yyyy"

    val utcDateFormat = SimpleDateFormat(utcFormat)
    utcDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val monthDateYearDateFormat = SimpleDateFormat(monthDayYearFormat)

    val utcDate = utcDateFormat.parse(this)
    return monthDateYearDateFormat.format(utcDate)
}

fun Int.formatWithCommas() : String =
        NumberFormat.getNumberInstance(Locale.US).format(this)

fun Int.formatWithSuffix() : String {
    if (this < 1000) {
        return "" + this
    }

    val exp = (Math.log(this.toDouble()) / Math.log(1000.0)).toInt()

    return String.format("%.1f%c",
            this / Math.pow(1000.0, exp.toDouble()),
            "kMGTPE"[exp - 1])
}