package com.example.mebby.app.customViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.example.mebby.R

class RangeSeekBar(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatSeekBar(context, attrs) {
    private var thumbLeft: Drawable? = null
    private var thumbRight: Drawable? = null

    private var thumbLeftValue = 0
    private var thumbRightValue = 100

    private var thumbLeftX = 0f
    private var thumbRightX = 0f

    init {
        thumbLeft = ContextCompat.getDrawable(context, R.drawable.seekbar_thumb)
        thumbRight = ContextCompat.getDrawable(context, R.drawable.seekbar_thumb)

        thumbLeft!!.setBounds(0, 0, thumbLeft!!.intrinsicWidth, thumbLeft!!.intrinsicHeight)
        thumbRight!!.setBounds(0, 0, thumbRight!!.intrinsicWidth, thumbRight!!.intrinsicHeight)

        thumbLeftX = thumbLeft!!.intrinsicWidth / 2f
        thumbRightX = width - thumbRight!!.intrinsicWidth / 2f

        thumb = null
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            thumbLeft?.let { leftThumb ->
                thumbRight?.let { rightThumb ->

                    val progressRatio = (thumbRightValue - thumbLeftValue) / max.toFloat()
                    val progressBarWidth = width - thumbLeft!!.intrinsicWidth - thumbRight!!.intrinsicWidth

                    val leftProgressX = thumbLeftX + progressRatio * progressBarWidth
                    val rightProgressX = thumbRightX - (1 - progressRatio) * progressBarWidth

                    leftThumb.setBounds(
                        leftProgressX.toInt() - leftThumb.intrinsicWidth / 2,
                        height / 2 - leftThumb.intrinsicHeight / 2,
                        leftProgressX.toInt() + leftThumb.intrinsicWidth / 2,
                        height / 2 + leftThumb.intrinsicHeight / 2
                    )

                    rightThumb.setBounds(
                        rightProgressX.toInt() - rightThumb.intrinsicWidth / 2,
                        height / 2 - rightThumb.intrinsicHeight / 2,
                        rightProgressX.toInt() + rightThumb.intrinsicWidth / 2,
                        height / 2 + rightThumb.intrinsicHeight / 2
                    )

                    leftThumb.draw(it)
                    rightThumb.draw(it)
                }
            }
        }
    }
}