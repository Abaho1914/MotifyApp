package com.abahoabbott.motify.managers


import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.abahoabbott.motify.BuildConfig
import com.abahoabbott.motify.worker.MotifyWorker
import timber.log.Timber
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralized manager responsible for scheduling background work
 */
@Singleton
class WorkSchedulerManager @Inject constructor(
    private val workManager: WorkManager
) {
    companion object {
        const val DAILY_WORK_NAME = "daily_motivation_work"
        const val IMMEDIATE_WORK_NAME = "immediate_motivation_work"
        const val IMMEDIATE_WORK_TAG = "immediate_motivation"
        const val PERIODIC_WORK_TAG = "motivation_notification"

        //For testing and debugging purposes
        private val isDebugMode get() = BuildConfig.DEBUG

    }

    /**
     * Schedules a daily motivational quote to be shown via a background worker.
     *
     * - In debug mode, this runs every 15 minutes for testing purposes.
     * - In production, this runs once every 24 hours at a predefined hour (8 AM by default).
     *
     * If a worker is already scheduled with the same name, this will retain the existing one.
     */
    fun scheduleDailyMotivationWork() {
        try {
            Timber.d("Scheduling daily motivation work")

            // Calculate initial delay until next notification time
            val initialDelay = calculateInitialDelayUntilNextNotification()

            // Create work request with appropriate interval based on build type

            val (repeatInterval, timeUnit) = getRepeatInterval()

            val dailyWorkRequest = createDailyWorkRequest(initialDelay, repeatInterval, timeUnit)

            // Enqueue the work
            workManager.enqueueUniquePeriodicWork(
                DAILY_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                dailyWorkRequest
            )

            // Log work info for debugging
            logWorkInfoState()

            Timber.d("Daily motivation work scheduled successfully")
        } catch (e: Exception) {
            Timber.e(e, "Failed to schedule daily motivation work")
        }
    }

    /**
     * Trigger a one-time motivation work request for immediate execution
     * Useful for testing
     */
    fun triggerImmediateMotivationWork(): Operation {
        Timber.d("Triggering immediate motivation work")

        val oneTimeWork = OneTimeWorkRequestBuilder<MotifyWorker>()
            .addTag(IMMEDIATE_WORK_TAG)
            .build()

        return workManager.enqueueUniqueWork(
            IMMEDIATE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            oneTimeWork
        )
    }
    private fun getRepeatInterval(): Pair<Long, TimeUnit> {
        return if (isDebugMode) {
            30L to TimeUnit.MINUTES
        } else {
            24L to TimeUnit.HOURS
        }
    }


    /**
     * Builds a periodic work request for the motivational worker,
     * with specified delay, interval, and constraints.
     *
     * @param initialDelay Delay before first execution
     * @param repeatInterval Interval for subsequent executions
     * @param timeUnit Time unit for interval
     * @return Configured [PeriodicWorkRequest] object
     */
    private fun createDailyWorkRequest(
        initialDelay: Long,
        repeatInterval: Long,
        timeUnit: TimeUnit
    ): PeriodicWorkRequest {
        // Create work constraints based on best practices
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()

        // Build the work request
        return PeriodicWorkRequestBuilder<MotifyWorker>(repeatInterval, timeUnit)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .addTag(PERIODIC_WORK_TAG)
            .build()
    }

    /**
     * Calculates the delay (in milliseconds) from the current time to the next scheduled
     * notification hour (defaults to 8 AM).
     *
     * @return Milliseconds to delay before starting the periodic worker.
     */
    internal fun calculateInitialDelayUntilNextNotification(notificationHour: Int = 8): Long {
        val now = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, notificationHour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If the target time has already passed today, schedule for tomorrow
            if (before(now)) {
                add(Calendar.DATE, 1)
            }
        }

        val delayMillis = targetTime.timeInMillis - now.timeInMillis

        // Debug mode: if delay is too long, use a shorter delay for testing
        return if (isDebugMode && delayMillis > TimeUnit.MINUTES.toMillis(5)) {
            TimeUnit.MINUTES.toMillis(1) // 1 minute delay for testing
        } else {
            delayMillis
        }
    }

    /**
     * Logs the current state of the scheduled daily motivation worker.
     * Only active in debug mode.
     *
     * Note: Uses observeForever; use with caution to avoid memory leaks.
     */
    private fun logWorkInfoState() {
        if (!isDebugMode) return
        workManager.getWorkInfosForUniqueWorkLiveData(DAILY_WORK_NAME)
            .observeForever { workInfoList ->
                workInfoList?.let {
                    for (workInfo in it) {
                        Timber.d("Work ID: ${workInfo.id}")
                        Timber.d("Work State: ${workInfo.state}")
                        Timber.d("Work Tags: ${workInfo.tags}")
                    }
                }
            }
    }

    /**
     * Get the WorkInfo for the daily motivation work as LiveData
     */
    fun getDailyMotivationWorkInfo() =
        workManager.getWorkInfosForUniqueWorkLiveData(DAILY_WORK_NAME)

}