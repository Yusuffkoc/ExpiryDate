package com.example.expirydate.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.expirydate.helper.NotificationHelper
import com.example.expirydate.repository.ProductRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver() : BroadcastReceiver() {

    @Inject lateinit var productRepository: ProductRepository

    override fun onReceive(context: Context?, intent: Intent?) {

        val productList = productRepository.getAllProducts().value

        if (!productList.isNullOrEmpty()){
            productList
                .forEach { product ->
                    if (product.id == 1){
                        val notificationHelper = NotificationHelper(context)
                        val nb: NotificationCompat.Builder = notificationHelper.channelNotification
                        notificationHelper.manager?.notify(1, nb.build())
                    }
                }
        }




        /*val i = Intent(context!!, MainActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context!!, "expirtyDate")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Expirty Date Title")
            .setContentText("Context")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123, builder.build())*/

    }
}