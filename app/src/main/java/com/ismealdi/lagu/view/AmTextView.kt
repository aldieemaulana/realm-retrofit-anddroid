package com.ismealdi.lagu.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import com.ismealdi.lagu.R
import com.ismealdi.lagu.logger.AlLog
import com.ismealdi.lagu.utils.Texts
import com.ismealdi.lagu.utils.Utils

/**
 * Created by Al on 26/06/2018
 */

class AmTextView : android.support.v7.widget.AppCompatTextView {

    private var mFont: String = "R"
    private var mPath: String = "fonts/Ubuntu-"
    private var mType: String = ".ttf"
    private var mEditRes: Int = 0
    private lateinit var mEdit: View
    private lateinit var mError: AmTextView
    private val mAutoHide = 5000

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setValues(attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setOnClickFocus()
    }

    private fun init() {
        setFont(mFont)
        setNewTypeFace()
    }

    fun init(component: AmEditText) {
        setComponent(component)
    }

    private fun setOnClickFocus() {
        if(mEditRes > 0 && mEditRes != -1) {
            mEdit = (parent as View).findViewById(mEditRes)!!

            setOnClickListener{
                mEdit.requestFocus()

                val editText = mEdit as AmEditText

                if(editText.getErrorRes())
                    editText.hideError()

                Utils().showKeyboard(context)
            }

        }
    }

    fun setFont(font: String) {
        mFont = font
    }

    fun getFont(): String {
        return mFont
    }

    fun setComponent(editText: AmEditText) {
        mEdit = editText
        setOnClickListener{
            mEdit.requestFocus()
            Utils().showKeyboard(context)
            hideError()
        }
    }

    fun setEditText(editText: Int) {
        mEditRes = editText
    }

    fun getEditText(): Int {
        return mEditRes
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
                    R.styleable.AmView_Amedit -> mEditRes = attr.getResourceId(R.styleable.AmView_Amedit, -1)
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
        hideError()
    }

    private fun handlerErrorHide(duration: Long) {
        val mHandler = Handler()
        val mRunnable = Runnable {
            hideError()
        }

        mHandler.postDelayed(mRunnable, duration)
    }

}

