package com.ismealdi.lagu.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.ismealdi.lagu.R
import com.ismealdi.lagu.activity.auth.LoginActivity
import com.ismealdi.lagu.base.BaseActivity
import com.ismealdi.lagu.model.Shared
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

/**
 * Created by Al on 27/09/2018
 */

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        setContentView(R.layout.activity_splash)
        handler(500) //TODO CHANGE TO 5S
    }

    private fun handler(duration: Long) {
        val mHandler = Handler()
        val mRunnable = Runnable {
            checkLogin()
        }

        mHandler.postDelayed(mRunnable, duration)
    }

    private fun checkLogin() {

        if ((realm.where<Shared>().count()) > 0) {
            val shared = realm.where<Shared>().findFirst()!!

            if(shared.isLogin)
                startActivity(Intent(context, MainActivity::class.java))
            else
                startActivity(Intent(context, MainActivity::class.java))


        }else{
            realm.executeTransaction { _ ->
                val shared= realm.createObject<Shared>(1)
                shared.isLogin = false
                shared.token = ""
                shared.uniqueId = ""
                shared.email = ""
                shared.name = ""
                shared.photoUrl = ""
            }

            startActivity(Intent(context, LoginActivity::class.java))
        }

        finish()
    }

}
