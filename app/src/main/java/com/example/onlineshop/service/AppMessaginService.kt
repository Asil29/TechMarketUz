package com.example.onlineshop.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.onlineshop.BuildConfig
import com.example.onlineshop.R
import com.example.onlineshop.screen.MainActivity
import com.example.onlineshop.utils.PrefUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.activity_main.view.*

class AppMessaginService: FirebaseMessagingService(){
    override fun onNewToken(token: String){
        PrefUtils.setFCMToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        try{
            var title = message.notification?.title
            var body = message.notification?.body
            showMessage(title ?: "", body ?: "")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun showMessage(title:String, body:String, id:Long = System.currentTimeMillis()){
        val defaultSoundUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var intent = Intent(this, MainActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = com.example.onlineshop.BuildConfig.APPLICATION_ID
        var builder = NotificationCompat.Builder(this, channelId)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setSmallIcon(R.drawable.ic_menu)
            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_menu))
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setColor(Color.parseColor("fff"))
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var channel = NotificationChannel(channelId,
                "${BuildConfig.APPLICATION_ID} channel",
                 NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        manager.notify(id.toInt(), builder.build())
    }
}