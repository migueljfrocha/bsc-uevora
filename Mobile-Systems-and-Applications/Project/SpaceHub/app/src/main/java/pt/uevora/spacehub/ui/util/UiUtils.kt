package pt.uevora.spacehub.ui.util

import android.content.Context
import android.content.Intent
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "UiUtils"

/**
 * Opens the Android share sheet to share text content.
 */
fun shareText(context: Context, title: String, text: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    Log.d(TAG, "Opening share sheet with title: $title")
    context.startActivity(Intent.createChooser(sendIntent, title))
}


/**
 * Converts a timestamp in milliseconds to an ISO date string (yyyy-MM-dd) in UTC.
 */
fun Long.toIsoDate(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(Date(this))
}


/**
 * Parses an ISO date string (yyyy-MM-dd) in UTC and returns the corresponding timestamp in milliseconds.
 * Returns null if the input string is not a valid date.
 */
fun String.toEpochMillis(): Long? {
    return try {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            isLenient = false
            timeZone = TimeZone.getTimeZone("UTC")
        }.parse(this)?.time
    } catch (_: ParseException) {
        null
    }
}

/**
 *  Checks if the string is a valid ISO date (yyyy-MM-dd) in UTC.
 */
fun String.isValidIsoDate(): Boolean {
    return try {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            isLenient = false
            timeZone = TimeZone.getTimeZone("UTC")
        }.parse(this) != null
    } catch (_: ParseException) {
        false
    }
}

/**
 * Converts the current date to an ISO date string (yyyy-MM-dd) in the device's default time zone.
 */
fun todayIsoDate(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getDefault()
    }.format(Date())
}
