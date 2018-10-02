package com.ismealdi.lagu.activity.calendar

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.ismealdi.lagu.R
import com.ismealdi.lagu.adapter.EventAdapter
import com.ismealdi.lagu.base.BaseActivity
import com.ismealdi.lagu.databinding.ActivityCalendarDetailBinding
import com.ismealdi.lagu.model.Event
import com.ismealdi.lagu.utils.Constants
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_wish_list.*
import kotlinx.android.synthetic.main.toolbar_primary.*

/**
 * Created by Al on 30/09/2018
 */

class DetailActivity : BaseActivity() {

    private lateinit var calendarDetailBinding : ActivityCalendarDetailBinding
    private lateinit var adapter : EventAdapter
    private var selectedDate : String = ""
    private var selectedId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        calendarDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_calendar_detail)
        calendarDetailBinding.executePendingBindings()

        init()
    }

    private fun init() {

        if(!intent.getStringExtra(Constants.FRAGMENT.CALENDAR.INTENT.SELECTED_DATE).isNullOrEmpty()) {
            selectedDate = intent.getStringExtra(Constants.FRAGMENT.CALENDAR.INTENT.SELECTED_DATE)
        }else{
            selectedId = intent.getIntExtra(Constants.FRAGMENT.CALENDAR.INTENT.SELECTED_ID, 0)
            if(selectedId > 0) {
                val data = realm.where<Event>().equalTo("id", selectedId).findFirst()
                if (data != null) {
                    selectedDate = data.date
                }
            }
        }

        calendarDetailBinding.title = selectedDate
        calendarDetailBinding.back = View.VISIBLE
        calendarDetailBinding.photo = View.INVISIBLE

        if(!selectedDate.isEmpty())
            initList()

        setListener()
    }

    private fun setListener() {
        if(buttonBackToolbar != null) {
            buttonBackToolbar.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun initList() {
        showProgress()
        adapter = EventAdapter(realm.where<Event>().equalTo("date", selectedDate).findAll(), context)
        recyclerView.layoutManager = LinearLayoutManager(context,
                LinearLayout.VERTICAL, false)
        recyclerView.adapter = adapter

        hideProgress()
    }
}