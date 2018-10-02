package com.ismealdi.lagu.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Al on 28/09/2018
 */

open class WishList(@PrimaryKey var id: String = "", var mbId: String = "", var wishList: Boolean = false,
                    var artistName: String = "",
                    var artistRating: Int = 0) : RealmObject() {

}
