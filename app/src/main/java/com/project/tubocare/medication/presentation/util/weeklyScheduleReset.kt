package com.project.tubocare.medication.presentation.util

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.project.tubocare.medication.presentation.util.ResetChecklist.ResetChecklistWorker
import java.util.concurrent.TimeUnit

fun scheduleWeeklyReset(context: Context){
    val workRequest = PeriodicWorkRequestBuilder<ResetChecklistWorker>(7, TimeUnit.DAYS)
        .setInitialDelay(7, TimeUnit.DAYS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "ResetChecklistWorker",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}
