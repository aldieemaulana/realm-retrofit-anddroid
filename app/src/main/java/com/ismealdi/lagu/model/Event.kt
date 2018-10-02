package com.ismealdi.lagu.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Al on 28/09/2018
 */

open class Event(@PrimaryKey var id: Int = 0, var date: String = "", var name: String = "", var description: String = ""
                 , var reminder: Boolean = false) : RealmObject() {

}
