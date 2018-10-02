package com.ismealdi.lagu.utils

/**
 * Created by Al on 24/06/2018
 */
open class Constants {

    object FRAGMENT {

        object FIND {
            const val NAME = "fragmentFind"
            const val ARTIST = "fragmentFindArtists"
        }
        const val CREATE = "fragmentCreate"
        const val WISH_LIST = "fragmentWishList"
        object CALENDAR {
            const val NAME = "fragmentCalendar"
            object INTENT {
                const val REQUEST_CODE = 101
                const val SELECTED_DATE = "fragmentCalendarIntentDataSelectedDate"
                const val SELECTED_ID = "fragmentCalendarIntentDataSelectedId"
            }
        }
        const val LOG_OUT = "fragmentLogOut"
    }

    object INTENT {
        const val GOOGLE_LOGIN = 1
        const val NOTIFICATION_ID = "notificationId"
        const val NOTIFICATION = "notification"
    }

}