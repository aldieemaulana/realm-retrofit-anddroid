package com.ismealdi.lagu.activity

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.view.View
import com.ismealdi.lagu.R
import com.ismealdi.lagu.base.BaseActivity
import com.ismealdi.lagu.databinding.ActivityMainBinding
import com.ismealdi.lagu.fragment.CalendarFragment
import com.ismealdi.lagu.fragment.CreateFragment
import com.ismealdi.lagu.fragment.FindFragment
import com.ismealdi.lagu.fragment.WishListFragment
import com.ismealdi.lagu.model.Event
import com.ismealdi.lagu.model.WishList
import com.ismealdi.lagu.utils.CircleTransform
import com.ismealdi.lagu.utils.Constants
import com.ismealdi.lagu.utils.Constants.INTENT.NOTIFICATION
import com.ismealdi.lagu.utils.Constants.INTENT.NOTIFICATION_ID
import com.ismealdi.lagu.utils.NotificationEvent
import com.squareup.picasso.Picasso
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_primary.*
import android.app.NotificationManager
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import com.ismealdi.lagu.activity.calendar.DetailActivity


class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var activityMainBinding : ActivityMainBinding

    private val findFragment = FindFragment()
    private val createFragment = CreateFragment()
    private val wishListFragment = WishListFragment()
    private val calendarFragment = CalendarFragment()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = findFragment

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_find -> {
                setTitle(getString(R.string.title_find))
                fragmentManager.beginTransaction().hide(activeFragment).show(findFragment).commit()
                activeFragment = findFragment

                return true
            }
            R.id.navigation_wish_list -> {
                setTitle(getString(R.string.title_wish_list))
                fragmentManager.beginTransaction().hide(activeFragment).show(wishListFragment).commit()
                activeFragment = wishListFragment

                return true
            }
            R.id.navigation_create -> {
                setTitle(getString(R.string.title_create))
                fragmentManager.beginTransaction().hide(activeFragment).show(createFragment).commit()
                activeFragment = createFragment

                return true
            }
            R.id.navigation_calendar -> {
                setTitle(getString(R.string.title_calendar))
                fragmentManager.beginTransaction().hide(activeFragment).show(calendarFragment).commit()
                activeFragment = calendarFragment

                return true
            }
            R.id.navigation_log_out -> {
                //logOut()

                return true
            }
        }

        return false
    }

    private fun setFragment() {
        setTitle(getString(R.string.title_find))

        fragmentManager.beginTransaction().add(R.id.frameLayout, createFragment, Constants.FRAGMENT.CREATE).hide(createFragment).commit()
        fragmentManager.beginTransaction().add(R.id.frameLayout, wishListFragment, Constants.FRAGMENT.WISH_LIST).hide(wishListFragment).commit()
        fragmentManager.beginTransaction().add(R.id.frameLayout, calendarFragment, Constants.FRAGMENT.CALENDAR.NAME).hide(calendarFragment).commit()
        fragmentManager.beginTransaction().add(R.id.frameLayout, findFragment, Constants.FRAGMENT.FIND.NAME).commit()
    }

    private fun setTitle(text: String) {
        activityMainBinding.title = text
    }

    private fun setListener() {
        bottomNavigation.setOnNavigationItemSelectedListener(this)
    }

    private fun setUser() {
        val uri = shared().photoUrl
        if(!uri.isEmpty())
            Picasso.get().load(uri).transform(CircleTransform()).into(imageViewPhoto)
    }

    private fun init() {
        activityMainBinding.back = View.INVISIBLE
        activityMainBinding.photo = View.VISIBLE

        setListener()
        setFragment()
        setUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.executePendingBindings()

        init()

        scheduleNotification(getNotification("Test Alarm", "1 second delay", 1), 1 * 1000, 4)
        scheduleNotification(getNotification("Test Alarm", "5 second delay", 2), 5 * 1000, 2)
        scheduleNotification(getNotification("Test Alarm", "35 second delay",3), 35 * 1000, 1)
        scheduleNotification(getNotification("Test Alarm", "40 second delay",4), 40 * 1000, 3)
    }

    fun refreshWishList() {
        wishListFragment.refreshWishList(realm.where<WishList>().findAll())
    }

    fun checkStateList() {
        findFragment.checkStateList()
    }

    fun getWishList(id: String, name: String): WishList? {
        return realm.where<WishList>().equalTo("id", id).equalTo("artistName", name).findFirst()
    }

    fun isWishList(id: String, name: String) : Boolean{
        val data = getWishList(id, name)

        if(data != null)
            return true

        return false
    }

    internal fun getLastPrimaryOfEvent() : Int {
        val currentIdNum = realm.where<Event>().max("id")
        return if (currentIdNum == null) {
            1
        } else {
            currentIdNum.toInt() + 1
        }
    }

    internal fun scheduleNotification(notification: Notification, delay: Long, id: Int = 1) {
        val delayTime = SystemClock.elapsedRealtime() + delay
        val notificationIntent = Intent(applicationContext, NotificationEvent::class.java)
        notificationIntent.putExtra(NOTIFICATION_ID, id)
        notificationIntent.putExtra(NOTIFICATION, notification)

        val pendingIntent = PendingIntent.getActivity(applicationContext, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, delayTime, pendingIntent)
    }

    internal fun getNotification(title: String = "", content: String = "", id: Int = 0): Notification {
        val builder = NotificationCompat.Builder(applicationContext)
        val intent = Intent(applicationContext, DetailActivity::class.java)
        intent.putExtra(Constants.FRAGMENT.CALENDAR.INTENT.SELECTED_ID, id)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        builder.setSmallIcon(R.drawable.ic_create_white)
        builder.setContentIntent(pendingIntent)
        builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_create_white))
        builder.setContentTitle(title)
        builder.setContentText(content)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1, builder.build())
        return builder.build()
    }

}
