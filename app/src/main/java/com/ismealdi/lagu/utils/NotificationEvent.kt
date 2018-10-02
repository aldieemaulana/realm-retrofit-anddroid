package com.ismealdi.lagu.utils

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import com.ismealdi.lagu.logger.AlLog
import com.ismealdi.lagu.utils.Constants.INTENT.NOTIFICATION
import com.ismealdi.lagu.utils.Constants.INTENT.NOTIFICATION_ID


/**
 * Created by Al on 01/10/2018
 */

class NotificationEvent : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = intent.getParcelableExtra<Notification>(NOTIFICATION)
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        AlLog.e("id: $id")

        notification.flags = Notification.FLAG_INSISTENT;
        notification.sound = uri
        notification.vibrate = longArrayOf(1000, 1000, 1000, 1000, 1000)
        notificationManager.notify(id, notification)

    }

}