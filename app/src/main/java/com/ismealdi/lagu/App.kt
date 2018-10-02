package com.ismealdi.lagu

import android.app.Application
import io.realm.Realm;
import io.realm.RealmConfiguration

/**
 * Created by Al on 20/05/17
 */

class App : Application() {

    companion object {
        var TOKEN  = "dd4be2f7a7a256a69354fb8afcf02184"
        var API = "http://api.musixmatch.com/ws/1.1/"
        var URL = "http://192.168.60.150/kolekta/public/"
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("lagu.realm").build()
        Realm.setDefaultConfiguration(config)
    }

}