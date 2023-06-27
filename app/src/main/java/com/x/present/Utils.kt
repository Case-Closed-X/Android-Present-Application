package com.x.present

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

object Utils {
    fun animateScaleXY(view:View, isOut:Boolean): AnimatorSet {
        val inX:ObjectAnimator
        val inY:ObjectAnimator
        val time:Long

        if (isOut){
            inX = ObjectAnimator.ofFloat(view, "scaleX", 0f)
            inY = ObjectAnimator.ofFloat(view, "scaleY", 0f)
            time = 400
        }
        else{
            inX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.1f, 1f)
            inY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1.1f, 1f)
            time = 800
        }
        val animSet = AnimatorSet()
        animSet.play(inX).with(inY)
        animSet.duration = time //if(isOut) 400 else 800
        animSet.start()

        return animSet
    }

    /**
     * 添加点击缩放效果
     */
    @SuppressLint("ClickableViewAccessibility")
    fun View.addClickScale(scale: Float = 0.9f, duration: Long = 150) {
        this.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    this.animate().scaleX(scale).scaleY(scale).setDuration(duration).start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    this.animate().scaleX(1.0f).scaleY(1.0f).setDuration(duration).start()
//                    this.animate().rotationY(0f).setDuration(duration).start()
                }
                /*MotionEvent.ACTION_MOVE ->{
                    this.animate().rotationY(-3f).setDuration(duration).start()
                }*/
            }
            // 点击事件处理，交给View自身
            this.onTouchEvent(event)
        }
    }
}