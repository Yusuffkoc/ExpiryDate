package com.example.expirydate

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.expirydate.worker.NotificationWM
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider {

    private val TAG: String = MyApplication::class.java.name

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate:called.")
//        createWorkManager()

//        val uploadWorkRequest = PeriodicWorkRequestBuilder<NotificationWM>(
//            1, TimeUnit.MINUTES, // repeatInterval (the period cycle)
//            15, TimeUnit.SECONDS
//        ) // flexInterval
//            .build()
//        WorkManager.getInstance(this).enqueue(uploadWorkRequest)
    }



    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}