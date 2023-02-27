package com.example.expirydate.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.expirydate.R
import com.example.expirydate.database.ProductDAO
import com.example.expirydate.di.ProductModule
import com.example.expirydate.repository.ProductRepository
import com.example.expirydate.repository.ProductRepositoryImpl
import com.example.expirydate.viewModel.ProductsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class NotificationWM @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
) :
    Worker(context, workerParameters) {

    private val TAG = NotificationWM::class.java.name
    //private val productRepository: ProductRepository = ProductRepositoryImpl(productDAO = ProductDAO)

    override fun doWork(): Result {
        sendNotification()
        return Result.success()
    }

    private fun sendNotification() {
        //var list = productRepository.getAllProducts().value
//        list.value?.forEach {
//            Log.d("Yusuf", it.productName.toString())
//        }
        notification()
        Log.d(TAG, "Work Manager is working")
    }

    private fun createNotificationChannel2() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "name"
            val descriptionText = "descriptionText"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("CHANNEL_ID", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this"
            val notificationManager = this.context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "getString(R.string.channel_name)"
            val descriptionText = "getString(R.string.channel_description)"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                this.context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun notification(title: String = "title", context: String="context") {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = NotificationChannel(
                "My notification",
                "My notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            var manager = this.context!!.getSystemService<NotificationManager>()
            manager!!.createNotificationChannel(channel)
        }

        var builder = NotificationCompat.Builder(this.context, "My notification")
        builder.setContentTitle(title)
        builder.setContentText(context)
        builder.setSmallIcon(R.mipmap.ic_launcher_foreground)
        builder.setAutoCancel(true)
//        builder.setContentIntent(
//            PendingIntent.getActivity(
//                context,
//                0,
//                Intent(context.applicationContext, MainActivity::class.java),
//                PendingIntent.FLAG_UPDATE_CURRENT
//            ))

        var manager = NotificationManagerCompat.from(this.context)
        manager.notify(1, builder.build())
    }

    override fun onStopped() {
        super.onStopped()
        Log.d(TAG,"on Stopped.")
    }

}