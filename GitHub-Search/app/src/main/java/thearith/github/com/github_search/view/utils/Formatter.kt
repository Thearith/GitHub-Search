package thearith.github.com.github_search.view.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Converts date from UTC format to "dd MMM yyyy" format
 *
 * @return a date String with "dd MMM yyyy" format
 * */
fun String.convertToMonthDayYearFormat() : String {
    val utcFormat = "yyyy-MM-dd'T'hh:mm:ss'Z'"
    val monthDayYearFormat = "dd MMM yyyy"

    val utcDateFormat = SimpleDateFormat(utcFormat)
    utcDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val monthDateYearDateFormat = SimpleDateFormat(monthDayYearFormat)

    val utcDate = utcDateFormat.parse(this)
    return monthDateYearDateFormat.format(utcDate)
}

/**
 * Represents a number with a comma between every 3 digits
 * For example, 1200000 will become 1,200,000
 *
 * @return a String object of the specified value
 * */
fun Int.formatWithCommas() : String =
        NumberFormat.getNumberInstance(Locale.US).format(this)


/**
 * Abbreviates a number with long digits
 * For example, 1200 will become 1.2k
 *
 * @return a String with abbreviated digits
 * */
fun Int.formatWithSuffix() : String {
    if (this < 1000) {
        return "" + this
    }

    val exp = (Math.log(this.toDouble()) / Math.log(1000.0)).toInt()

    return String.format("%.1f%c",
            this / Math.pow(1000.0, exp.toDouble()),
            "kMGTPE"[exp - 1])
}