package com.ismealdi.lagu.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
        val name = textName.text.toString()
        val date = textDate.text.toString()

        if(name.isEmpty() && date.isEmpty()) {
            setError("Name and date value cannot be empty")
        }else if(date.isEmpty()) {
            setError("Please select the date")
        }else if(name.isEmpty()) {
            setError("Please fill in name value")
        }else{
            val sdf = SimpleDateFormat("d/M/y k:m")
            val dateTime = sdf.parse(date)
            val currentDateTime = sdf.parse(sdf.format(Calendar.getInstance().time))

            if (dateTime.compareTo(currentDateTime) >= 0) {
                doCreateEvent()
            }else{
                setError("Date time cannot be lesser than current date time")
            }
        }
    }

    private fun doCreateEvent() {
        mActivity.showProgress()
        setError("")
        mActivity.realm.executeTransaction { _ ->
            val id = mActivity.getLastPrimaryOfEvent()
            val event = mActivity.realm.createObject<Event>(id)
            val datetime = textDate.text.toString().split(" ")

            event.name = textName.text.toString()
            event.description = textDescription.text.toString()
            event.date = datetime[0]
            event.time = datetime[1]

            doSuccess(id)
        }

        mActivity.hideProgress()
    }

    @SuppressLint("SimpleDateFormat")
    private fun doSuccess(id: Int) {
        val sdf = SimpleDateFormat("d/M/y k:m")
        val date = sdf.parse(textDate.text.toString())

        mActivity.scheduleNotification(mActivity.getNotification(textName.text.toString(), textDescription.text.toString(), id), (date.time - 1800000), id)

        textName.setText("")
        textDescription.setText("")
        textDate.text = ""

        setError("Data is saved!", false)
    }

    @SuppressLint("SetTextI18n")
    private fun initCalendarDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val timePickerDialog = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    textDate.text = textDate.text.toString() + " $hourOfDay:$minute"
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true)

        timePickerDialog.setOnCancelListener {
            textDate.text = ""
        }

        datePicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, y, m, d ->
            val mm = m + 1
            textDate.text =  "$d/$mm/$y"
            timePickerDialog.show()
        }, year, month, day)

        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000

        datePicker.setOnCancelListener {
            textDate.text = ""
        }
    }

    private fun setError(text: String, shake: Boolean = true) {
        textError.text  = text
        if(shake) {
            textError.setTextColor(resources.getColor(R.color.colorError))
            Texts().shakeTextView(textError, context!!)
        }else{
            textError.setTextColor(resources.getColor(R.color.colorBlack))
        }
    }



}