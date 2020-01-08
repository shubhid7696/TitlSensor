package com.example.mytitlsensor2

import android.animation.AnimatorSet
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.hardware.SensorEvent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : SensorBaseActivity() {

    private val proxy = BehaviorSubject.create<SensorEvent>()
    lateinit var replaceAnimation : AnimationSet

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPitchVal = object : MyInterface {
            override fun mPichVal(mP: Float) {
                Log.d("### ", "mY pITCH vAL = $mP")
                moveArrow(mP)
            }
        }

    }

    fun moveArrow(pitch: Float) {
        var ptch = pitch
        pitchTv.text = ptch.toString()
        if(pitch<0){
            ptch = 0.0f
        }
        Log.d("### pitch val", "" + ptch)
        //(mainActivity as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        val params = dummy_vv.layoutParams as ConstraintLayout.LayoutParams
        params.verticalBias = "%.3f".format(ptch).toFloat()//0.2f // here is one modification for example. modify anything else you want :)
        dummy_vv.layoutParams = params

        replace()
    }

    fun replace() {
        // create set of animations
        replaceAnimation = AnimationSet(false)
        // animations should be applied on the finish line
        replaceAnimation.fillAfter = true

        //val transAnim = TranslateAnimation(arrowIv.x, arrowIv.x, arrowIv.y, dummy_vv.y-arrowIv.y-arrowIv.height)
        val transAnim = TranslateAnimation(0f, 0f, arrowIv.y, dummy_vv.y-arrowIv.y-arrowIv.height)
        //val transAnim = TranslateAnimation(0f, 0f, arrowIv.y, dummy_vv.y-arrowIv.y-arrowIv.height)"
        transAnim.duration = 1000

        // add new animations to the set
        //        replaceAnimation.addAnimation(scale)
        replaceAnimation.addAnimation(transAnim)

        // start our animation
        replaceAnimation.interpolator = LinearInterpolator()
        arrowIv.startAnimation(replaceAnimation)

    }

}
