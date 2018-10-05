package com.ismealdi.lagu.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ismealdi.lagu.R
import com.ismealdi.lagu.activity.MainActivity
import com.ismealdi.lagu.activity.calendar.DetailActivity
import com.ismealdi.lagu.utils.Constants
import kotlinx.android.synthetic.main.fragment_calendar.*

class CalendarFragment : Fragment() {

    private lateinit var mActivity : MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity = (activity as MainActivity)
        listener()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    private fun listener() {
        calendarView.setOnDateChangeListener { _, y, m, d ->
            val intent = Intent(context, DetailActivity::class.java)

            val mm = m + 1

            intent.putExtra(Constants.FRAGMENT.CALENDAR.INTENT.SELECTED_DATE, "$d/$mm/$y")
            startActivityForResult(intent, Constants.FRAGMENT.CALENDAR.INTENT.REQUEST_CODE)
        }
    }

}
