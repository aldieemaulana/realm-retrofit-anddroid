package com.ismealdi.lagu.base

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.ismealdi.lagu.activity.auth.LoginActivity
import com.ismealdi.lagu.model.Shared
import com.ismealdi.lagu.model.WishList
import com.ismealdi.lagu.utils.Dialogs
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.disposables.Disposable
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.toolbar_primary.*

/**
 * Created by Al on 23/06/2018
 */
open class BaseActivity : AppCompatActivity() {

    open val context : Context = this
    open var progress: KProgressHUD? = null
    open var disposable : Disposable? = null

    open lateinit var realm: Realm
    open lateinit var mGoogleSignInClient : GoogleSignInClient
    open lateinit var mGoogleSignInOptions : GoogleSignInOptions

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    fun initData() {
        realm = Realm.getDefaultInstance()

        mGoogleSignInOptions = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(context, mGoogleSignInOptions)
        progress = Dialogs().initProgressDialog(context)
    }

    fun logOut() {
        showProgress()
        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            resetRealm()
            hideProgress()
            startActivity(Intent(context, LoginActivity::class.java))
            finish()
        }

    }

    private fun resetRealm() {
        val shared = realm.where<Shared>().findFirst()!!
        val WishLists = realm.where<WishList>().findAll()!!
        realm.executeTransaction { _ ->
            shared.isLogin = false
            shared.token = ""
            shared.uniqueId = ""
            shared.email = ""
            shared.name = ""
            shared.photoUrl = ""

            WishLists.deleteAllFromRealm()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun shared() : Shared {
        return realm.where<Shared>().findFirst()!!
    }

    fun showProgress() {
        progress!!.show()
    }

    fun hideProgress() {
        progress!!.dismiss()
    }


}