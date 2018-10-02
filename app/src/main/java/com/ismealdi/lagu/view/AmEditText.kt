package com.ismealdi.lagu.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.util.AttributeSet
import com.ismealdi.lagu.logger.AlLog
import com.ismealdi.lagu.utils.Texts
import com.ismealdi.lagu.utils.Utils
import com.ismealdi.lagu.R

/**
 * Created by Al on 26/06/2018
 */

class AmEditText : android.support.v7.widget.AppCompatEditText {

    private var mFont: String = "R"
    private var mPath: String = "fonts/Ubuntu-"
    private var mType: String = ".ttf"
    private var mErrorRes = false
    private lateinit var mError: AmTextView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setValues(attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        setFont(mFont)
        setNewTypeFace()
    }

    fun setFont(font: String) {
        mFont = font
    }


    fun clearError() {
        error = ""
    }

    fun getFont(): String {
        return mFont
    }

    fun getErrorRes(): Boolean {
        return mErrorRes
    }

    private fun setNewTypeFace() {
        val font = Typeface.createFromAsset(context.assets, mPath + mFont + mType)
        setTypeface(font, Typeface.NORMAL)
    }

    @SuppressLint("CustomViewStyleable")
    private fun setValues(attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.AmView)
        try {
            val n = attr.indexCount
            for (i in 0 until n) {
                val attribute = attr.getIndex(i)
                when (attribute) {
                    R.styleable.AmView_Amfont -> mFont = attr.getString(attribute)
                    else -> AlLog.d("Unknown attribute for " + javaClass.toString() + ": " + attribute)
                }
            }
        } finally {
            attr.recycle()
        }
    }

    fun showError(string: String) {
        Texts().fadeInTextView(string, context, mError)
        setPadding(context.resources.getDimension(R.dimen.component_big).toInt(), context.resources.getDimension(R.dimen.component_small).toInt(), context.resources.getDimension(R.dimen.component_big).toInt(), context.resources.getDimension(R.dimen.component_extra_small).toInt())
        //handlerErrorHide(Constants.AUTO_HIDE_ERROR.toLong())
    }

    fun hideError() {
        if(mError.text.isNotEmpty())
            Texts().fadeOutTextView(mError, context, this)
    }

    fun setErrorEdit(textViewError: AmTextView) {
        mError = textViewError
        mErrorRes = true
        hideError()

        setOnFocusChangeListener { v, hasFocus ->
            hideError()
            if(hasFocus)
                Utils().showKeyboard(context)
        }
    }

    private fun handlerErrorHide(duration: Long) {
        val mHandler = Handler()
        val mRunnable = Runnable {
            hideError()
        }

        mHandler.postDelayed(mRunnable, duration)
    }
}

