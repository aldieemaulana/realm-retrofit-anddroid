package com.ismealdi.lagu.utils

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.ismealdi.lagu.R
import android.view.animation.Animation
import android.animation.ValueAnimator
import com.ismealdi.lagu.view.AmTextView


/**
 * Created by Al on 24/06/2018 for Kolekta
 */

open class Texts {

    fun shakeTextView(mTextView: TextView, context: Context) {
        val animShake = AnimationUtils.loadAnimation(context, R.anim.shake)
        mTextView.startAnimation(animShake)
    }

    fun fadeOutTextView(mTextView: TextView, context: Context, amEditText: View) {
        val animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)

        animFadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                val animator = ValueAnimator.ofInt(context.resources.getDimension(R.dimen.component_super).toInt(), context.resources.getDimension(R.dimen.component_big).toInt())
                animator.addUpdateListener { valueAnimator -> amEditText.setPadding(context.resources.getDimension(R.dimen.component_big).toInt(), context.resources.getDimension(R.dimen.component_small).toInt(), context.resources.getDimension(R.dimen.component_big).toInt(), valueAnimator.animatedValue as Int) }
                animator.duration = 1000
                animator.start()

                mTextView.text = ""
                mTextView.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        mTextView.startAnimation(animFadeOut)
    }

    fun fadeInTextView(error: String, context: Context, mTextView: AmTextView) {
        mTextView.text = error
        val animFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)

        animFadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                mTextView.visibility = View.VISIBLE
            }
            override fun onAnimationEnd(animation: Animation) {
                shakeTextView(mTextView, context)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        mTextView.startAnimation(animFadeIn)

    }


}