package com.ismealdi.lagu.utils

import android.content.Context
import com.kaopiz.kprogresshud.KProgressHUD

class Dialogs {

    fun initProgressDialog(context: Context): KProgressHUD {
        return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setDimAmount(0.5f)
                .setAnimationSpeed(2)
    }

}
