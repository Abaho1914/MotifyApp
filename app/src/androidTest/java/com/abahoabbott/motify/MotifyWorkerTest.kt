package com.abahoabbott.motify

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import com.abahoabbott.motify.worker.MotifyWorker
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MotifyWorkerTest {

    private lateinit var context: Context


    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
//initialize WorkManager for testing
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
    }

    @Test
    fun testMotifyWorkerRunsSuccessfully() {
        //create request
        val request = OneTimeWorkRequestBuilder<MotifyWorker>()
            .build()

        val workManger = WorkManager.getInstance(context)
        workManger.enqueue(request).result.get()

        //simulate constraints being met
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)
        testDriver?.setAllConstraintsMet(request.id)

        //check status
        val workInfo = workManger.getWorkInfoById(request.id).get()
        assertEquals(WorkInfo.State.SUCCEEDED,workInfo?.state)

    }



}