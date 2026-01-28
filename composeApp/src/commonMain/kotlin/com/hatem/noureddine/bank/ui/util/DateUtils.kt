@file:Suppress("TooGenericExceptionCaught", "SwallowedException")

package com.hatem.noureddine.bank.ui.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

/**
 * Utility object for date and time formatting.
 * Provides methods to format epoch timestamps into readable strings.
 */
object DateUtils {
    /**
     * Formats an epoch timestamp (milliseconds) to "dd/MM/yyyy".
     *
     * @param epochMillis The timestamp in milliseconds.
     * @return Formatted date string, or "Unknown Date" on error.
     */
    @OptIn(kotlin.time.ExperimentalTime::class)
    fun formatEpoch(epochMillis: Long): String {
        val instant =
            try {
                Instant.fromEpochMilliseconds(epochMillis)
            } catch (e: Exception) {
                Instant.fromEpochMilliseconds(0)
            }
        val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val customFormat =
            LocalDate.Format {
                day()
                char('/')
                monthNumber()
                char('/')
                year()
            }
        return date.format(customFormat)
    }

    /**
     * Formats an epoch timestamp (milliseconds) to "dd/MM/yyyy HH:mm:ss".
     *
     * @param epochMillis The timestamp in milliseconds.
     * @return Formatted date-time string, or "Unknown Date" on error.
     */
    @OptIn(kotlin.time.ExperimentalTime::class)
    fun formatDateTimeWithSeconds(epochMillis: Long): String {
        val instant =
            try {
                Instant.fromEpochMilliseconds(epochMillis)
            } catch (e: Exception) {
                Instant.fromEpochMilliseconds(0)
            }

        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val customFormat =
            LocalDateTime.Format {
                day()
                char('/')
                monthNumber()
                char('/')
                year()
                char(' ')
                hour()
                char(':')
                minute()
                char(':')
                second()
            }
        return dateTime.format(customFormat)
    }

    fun getHeaderDate(epochMillis: Long): String = formatEpoch(epochMillis)
}
