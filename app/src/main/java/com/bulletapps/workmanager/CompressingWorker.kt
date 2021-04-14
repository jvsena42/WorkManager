package com.bulletapps.workmanager

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class CompressingWorker(context: Context, params: WorkerParameters): Worker(context,params) {

    override fun doWork(): ListenableWorker.Result {
        try {
            for (i in 0..600){
                Log.i(ContentValues.TAG, "Compressing: $i")
            }

            return ListenableWorker.Result.success()
        }catch (e:Exception){
            return ListenableWorker.Result.failure()
        }
    }
}