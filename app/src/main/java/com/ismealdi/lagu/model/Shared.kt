package com.ismealdi.lagu.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Al on 28/09/2018
 */

open class Shared(@PrimaryKey var id: Long = 0, var token: String = "", var uniqueId
: String = "", var isLogin: Boolean = false, var name: String = "", var email: String = "", var photoUrl: String = "") : RealmObject() {

}