package com.ismealdi.lagu.logger

import android.util.Log
import com.ismealdi.lagu.BuildConfig

/**
 * Created by Al on 01/02/2018.
 */

object AlLog {
    fun e(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e("Al error", "AlMsg: $msg")
        }
    }

    fun d(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d("Al debug", "AlMsg: $msg")
        }
    }

    fun v(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.v("Al verbose","AlMsg: $msg")
        }
    }

    fun i(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i("Al info","AlMsg: $msg")
        }
    }

    fun w(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.w("Al warn","AlMsg: $msg")
        }
    }

    fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e("Al","AlMsg: $msg")
        }
    }
}