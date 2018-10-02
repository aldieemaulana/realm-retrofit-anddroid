package com.ismealdi.lagu.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ismealdi.lagu.R
import com.ismealdi.lagu.activity.MainActivity
import com.ismealdi.lagu.base.BaseFragment
import com.ismealdi.lagu.model.Event
import com.ismealdi.lagu.utils.Texts
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.fragment_create.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


class CreateFragment : BaseFragment() {

    private lateinit var datePicker: DatePickerDialog
    private lateinit var mActivity : MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity = (activity as MainActivity)
        listener()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    private fun listener() {
        initCalendarDialog()

        textDateLayout.setOnClickListener {
            datePicker.show()
        }

        buttonSave.setOnClickListener {
            doValidate()
        }

    }

    private fun doValidate() {
        if(textName.text.toString().isEmpty() && textDate.text.toString().isEmpty()) {
            setError("Name and date value cannot be empty")
        }else if(textDate.text.toString().isEmpty()) {
            setError("Please select the date")
        }else if(textName.text.toString().isEmpty()) {
            setError("Please fill in name value")
        }else{
            doCreateEvent()
        }
    }

    private fun doCreateEvent() {
        mActivity.showProgress()
        setError("")
        mActivity.realm.executeTransaction { _ ->
            val id = mActivity.getLastPrimaryOfEvent()
            val event = mActivity.realm.createObject<Event>(id)

            event.name = textName.text.toString()
            event.description = textDescription.text.toString()
            event.date = textDate.text.toString()

            doSuccess(id)
        }

        mActivity.hideProgress()
    }

    @SuppressLint("SimpleDateFormat")
    private fun doSuccess(id: Int) {
        val sdf = SimpleDateFormat("d/M/yyyy hh:mm:ss")
        val today = Date()
        val date = sdf.parse(textDate.text.toString() + " ${today.hours}:${today.minutes}:${today.seconds}" )

        mActivity.scheduleNotification(mActivity.getNotification(textName.text.toString(), textDescription.text.toString()), (date.time - 1800000), id)

        textName.setText("")
        textDescription.setText("")
        textDate.text = ""

        setError("Data is saved!")
    }

    private fun initCalendarDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        datePicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, y, m, d ->
            textDate.text =  "$d/$m/$y"
        }, year, month, day)

        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
    }

    private fun setError(text: String) {
        textError.text  = text
        Texts().shakeTextView(textError, context!!)
    }



}