package com.abahoabbott.motify.managers

import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.abahoabbott.motify.BuildConfig
import com.abahoabbott.motify.worker.GeminiQuoteWorker
import timber.log.Timber
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralized manager responsible for scheduling background work for motivational quotes.
 *
 * Features:
 * - Daily quote notifications at 8 AM (configurable)
 * - One-time quote fetching on demand
 * - Proper error handling and logging
 * - Debug mode support with shorter intervals for testing
 */
@Singleton
class WorkSchedulerManager @Inject constructor(
    private val workManager: WorkManager
) {

    private var workInfoObserver: Observer<List<WorkInfo>>? = null

    companion object {
        const val DAILY_WORK_NAME = "daily_motivation_work"
        const val PERIODIC_WORK_TAG = "motivation_notification"
        const val ONE_TIME_WORK_TAG = "one_time_motivation"

        // Configuration constants
        private const val DEFAULT_NOTIFICATION_HOUR = 8 // 8 AM
        private const val DEBUG_INTERVAL_MINUTES = 15L // For testing only
        private const val PRODUCTION_INTERVAL_HOURS = 24L
        private const val DEBUG_INITIAL_DELAY_MINUTES = 1L

        // Battery and storage constraints
        private const val REQUIRE_BATTERY_NOT_LOW = true
        private const val REQUIRE_STORAGE_NOT_LOW = true
    }

    /**
     * Triggers an immediate one-time quote fetch.
     * Useful for manual refresh or app startup scenarios.
     */
    fun fetchQuoteOfTheDay() {
        try {
            Timber.i("Enqueuing one-time quote fetch")

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false) // Allow on low battery for manual requests
                .build()

            val workRequest = OneTimeWorkRequestBuilder<GeminiQuoteWorker>()
                .setConstraints(constraints)
                .addTag(ONE_TIME_WORK_TAG)
                .build()

            workManager.enqueue(workRequest)
            Timber.d("One-time quote work enqueued with ID: ${workRequest.id}")

        } catch (e: Exception) {
            Timber.e(e, "Failed to enqueue one-time quote fetch")
        }
    }

    /**
     * Schedules daily motivational quote notifications.
     *
     * In production: Runs once every 24 hours at 8 AM
     * In debug mode: Runs every 15 minutes for testing (if enabled)
     *
     * @param notificationHour Hour of day to send notifications (0-23), defaults to 8 AM
     * @param enableDebugMode Override debug mode behavior (mainly for testing)
     */
    fun scheduleDailyMotivationWork(
        notificationHour: Int = DEFAULT_NOTIFICATION_HOUR,
        enableDebugMode: Boolean = BuildConfig.DEBUG
    ) {
        try {
            validateNotificationHour(notificationHour)

            Timber.i("Scheduling daily motivation work for ${if (enableDebugMode) "debug" else "production"} mode")

            val initialDelay = calculateInitialDelay(notificationHour, enableDebugMode)
            val (repeatInterval, timeUnit) = getRepeatInterval(enableDebugMode)

            val dailyWorkRequest = createPeriodicWorkRequest(
                initialDelay = initialDelay,
                repeatInterval = repeatInterval,
                timeUnit = timeUnit
            )

            // Use KEEP policy to avoid duplicating work
            workManager.enqueueUniquePeriodicWork(
                DAILY_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                dailyWorkRequest
            )

            logWorkSchedulingInfo(enableDebugMode, notificationHour, initialDelay, repeatInterval, timeUnit)

            // Set up work state monitoring in debug mode
            if (enableDebugMode) {
                startWorkStateMonitoring()
            }

            Timber.i("Daily motivation work scheduled successfully")

        } catch (e: Exception) {
            Timber.e(e, "Failed to schedule daily motivation work")
            throw WorkSchedulingException("Failed to schedule daily work", e)
        }
    }

    /**
     * Cancels all scheduled motivational work.
     * Useful for app settings changes or cleanup.
     */
    fun cancelAllMotivationWork() {
        try {
            Timber.i("Cancelling all motivation work")

            // Cancel periodic work
            workManager.cancelUniqueWork(DAILY_WORK_NAME)

            // Cancel any pending one-time work
            workManager.cancelAllWorkByTag(ONE_TIME_WORK_TAG)

            // Stop monitoring
            stopWorkStateMonitoring()

            Timber.i("All motivation work cancelled successfully")

        } catch (e: Exception) {
            Timber.e(e, "Failed to cancel motivation work")
        }
    }

    /**
     * Reschedules daily work with new parameters.
     * Cancels existing work and creates new schedule.
     */
    fun rescheduleDailyWork(notificationHour: Int = DEFAULT_NOTIFICATION_HOUR) {
        try {
            Timber.i("Rescheduling daily work for hour: $notificationHour")

            // Cancel existing work
            workManager.cancelUniqueWork(DAILY_WORK_NAME)

            // Schedule new work
            scheduleDailyMotivationWork(notificationHour)

        } catch (e: Exception) {
            Timber.e(e, "Failed to reschedule daily work")
        }
    }

    private fun validateNotificationHour(hour: Int) {
        if (hour !in 0..23) {
            throw IllegalArgumentException("Notification hour must be between 0 and 23, got: $hour")
        }
    }

    private fun getRepeatInterval(enableDebugMode: Boolean): Pair<Long, TimeUnit> {
        return if (enableDebugMode) {
            DEBUG_INTERVAL_MINUTES to TimeUnit.MINUTES
        } else {
            PRODUCTION_INTERVAL_HOURS to TimeUnit.HOURS
        }
    }

    private fun createPeriodicWorkRequest(
        initialDelay: Long,
        repeatInterval: Long,
        timeUnit: TimeUnit
    ): PeriodicWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(REQUIRE_BATTERY_NOT_LOW)
            .setRequiresStorageNotLow(REQUIRE_STORAGE_NOT_LOW)
            .setRequiresDeviceIdle(false) // Allow work even when device is active
            .build()

        return PeriodicWorkRequestBuilder<GeminiQuoteWorker>(repeatInterval, timeUnit)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .addTag(PERIODIC_WORK_TAG)
            .build()
    }

    /**
     * Calculates delay until next notification time.
     * In debug mode, uses shorter delay for testing.
     */
    private fun calculateInitialDelay(notificationHour: Int, enableDebugMode: Boolean): Long {
        if (enableDebugMode) {
            val debugDelay = TimeUnit.MINUTES.toMillis(DEBUG_INITIAL_DELAY_MINUTES)
            Timber.d("Using debug initial delay: $DEBUG_INITIAL_DELAY_MINUTES minutes")
            return debugDelay
        }

        return calculateDelayUntilHour(notificationHour)
    }

    private fun calculateDelayUntilHour(targetHour: Int): Long {
        val now = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If target time has passed today, schedule for tomorrow
            if (before(now)) {
                add(Calendar.DATE, 1)
            }
        }

        return targetTime.timeInMillis - now.timeInMillis
    }

    private fun logWorkSchedulingInfo(
        enableDebugMode: Boolean,
        notificationHour: Int,
        initialDelay: Long,
        repeatInterval: Long,
        timeUnit: TimeUnit
    ) {
        val mode = if (enableDebugMode) "DEBUG" else "PRODUCTION"
        val delayMinutes = TimeUnit.MILLISECONDS.toMinutes(initialDelay)
        val intervalDescription = if (enableDebugMode) {
            "$repeatInterval minutes"
        } else {
            "$repeatInterval hours (${notificationHour}:00)"
        }

        Timber.i("Work scheduled - Mode: $mode, Initial delay: ${delayMinutes}min, Interval: $intervalDescription")
    }

    /**
     * Starts monitoring work state for debugging purposes.
     * Only active in debug mode to avoid memory leaks in production.
     */
    private fun startWorkStateMonitoring() {
        stopWorkStateMonitoring() // Clean up any existing observer

        workInfoObserver = Observer { workInfoList ->
            workInfoList.let { workList ->
                if (workList.isNotEmpty()) {
                    Timber.d("=== Work State Update ===")
                    workList.forEach { workInfo ->
                        Timber.d("Work ID: ${workInfo.id}")
                        Timber.d("State: ${workInfo.state}")
                        Timber.d("Tags: ${workInfo.tags}")
                        Timber.d("Run attempt: ${workInfo.runAttemptCount}")
                        if (workInfo.state == WorkInfo.State.FAILED) {
                            Timber.w("Work failed - Output: ${workInfo.outputData}")
                        }
                    }
                    Timber.d("========================")
                }
            }
        }

        workManager.getWorkInfosForUniqueWorkLiveData(DAILY_WORK_NAME)
            .observeForever(workInfoObserver!!)
    }

    /**
     * Stops work state monitoring to prevent memory leaks.
     */
    private fun stopWorkStateMonitoring() {
        workInfoObserver?.let { observer ->
            workManager.getWorkInfosForUniqueWorkLiveData(DAILY_WORK_NAME)
                .removeObserver(observer)
            workInfoObserver = null
        }
    }

    /**
     * Gets current work status for debugging or UI purposes.
     */
    suspend fun getWorkStatus(): List<WorkInfo> {
        return try {
            workManager.getWorkInfosForUniqueWork(DAILY_WORK_NAME).get()
        } catch (e: Exception) {
            Timber.e(e, "Failed to get work status")
            emptyList()
        }
    }
}

/**
 * Custom exception for work scheduling errors
 */
class WorkSchedulingException(message: String, cause: Throwable? = null) : Exception(message, cause)