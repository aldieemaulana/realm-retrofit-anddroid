package com.ismealdi.lagu.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import com.ismealdi.lagu.activity.calendar.DetailActivity
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

        notification.flags = Notification.FLAG_HIGH_PRIORITY
        notification.sound = uri
        notification.vibrate = longArrayOf(1000, 1000, 1000, 1000, 1000)
        notificationManager.notify(id, notification)

        clearNotification(context, id)

    }

    private fun clearNotification(context: Context, selectedId: Int) {
        if(selectedId > 0) {
            val ns = Context.NOTIFICATION_SERVICE
            val nMgr = context.getSystemService(ns) as NotificationManager
            nMgr.cancel(selectedId)
        }
    }


}