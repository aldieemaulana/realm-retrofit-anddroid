package com.ismealdi.lagu.activity.auth

import android.content.Intent
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.ismealdi.lagu.R
import com.ismealdi.lagu.activity.MainActivity
import com.ismealdi.lagu.base.BaseActivity
import com.ismealdi.lagu.model.Shared
import com.ismealdi.lagu.utils.Constants
import com.ismealdi.lagu.utils.Texts
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_login.*


/**
 * Created by Al on 27/09/2018
 */

class LoginActivity : BaseActivity() {

    private lateinit var account : GoogleSignInAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {
        setListener()
    }

    private fun setListener() {
        buttonSignIn.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, Constants.INTENT.GOOGLE_LOGIN)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.INTENT.GOOGLE_LOGIN) {
            showProgress()
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        hideProgress()

        try {
            account = completedTask.getResult(ApiException::class.java)
            val shared : Shared? = realm.where<Shared>().findFirst()
            if (shared != null) {

                realm.executeTransaction {

                    shared.isLogin = true
                    shared.token = account.idToken.toString()
                    shared.uniqueId = account.id.toString()
                    shared.name = account.displayName.toString()
                    shared.email = account.email.toString()
                    shared.photoUrl = account.photoUrl.toString()

                }

                startActivity(Intent(context, MainActivity::class.java))
                finish()
            }


        } catch (e: ApiException) {

            textError.text = e.localizedMessage
            Texts().shakeTextView(textError, context)
        }

    }

}