package com.bulletapps.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object{
        const val KEY_COUNT_VALUE = "key_count"
    }

    lateinit var textView:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.button)
        textView = findViewById(R.id.textView)

        button.setOnClickListener {
//            setOneTimeWorkRequest()
            setPeriodicWorkRequest()
        }
    }

    private fun setOneTimeWorkRequest(){
        val workManager = WorkManager.getInstance(applicationContext)

        val data = Data.Builder()
            .putInt(KEY_COUNT_VALUE,12)
            .build()

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        val filteringRequest = OneTimeWorkRequest.Builder(FilteringWorker::class.java)
            .build()

        val compressingRequest = OneTimeWorkRequest.Builder(CompressingWorker::class.java)
            .build()

        workManager
            .beginWith(filteringRequest)
            .then(compressingRequest)
            .then(uploadRequest)
            .enqueue()

        workManager.getWorkInfoByIdLiveData(uploadRequest.id).observe(this, {
            textView.text = it.state.name

            if (it.state.isFinished) {
                val data = it.outputData
                val message = data.getString(UploadWorker.KEY_WORKER)
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun setPeriodicWorkRequest(){

        val workManager = WorkManager.getInstance(applicationContext)

        val periodicWorkRequest = PeriodicWorkRequest.Builder(CompressingWorker::class.java,16,TimeUnit.MINUTES)
            .build()

        workManager.enqueue(periodicWorkRequest)
    }
}