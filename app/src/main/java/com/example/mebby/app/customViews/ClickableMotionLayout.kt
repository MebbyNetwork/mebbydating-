package com.example.mebby.app.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.constraintlayout.motion.widget.MotionLayout
import java.lang.Math.abs

class ClickableMotionLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): MotionLayout(context, attrs, defStyleAttr) {
    private var mInitX = 0f
    private var mInitY = 0f

    private var mTouchSlop = 10

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mInitX = ev.x
                mInitY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = kotlin.math.abs(x - mInitX)
                val moveY = kotlin.math.abs(y - mInitY)

                if (moveX > mTouchSlop || moveY > mTouchSlop){

                    val obtain = MotionEvent.obtain(ev)
                    obtain.action = MotionEvent.ACTION_DOWN
                    dispatchTouchEvent(obtain)
                    onTouchEvent(obtain)
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return false
    }
}