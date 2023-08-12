package com.example.mebby.app.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText: AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER) {
            clearFocus()
        }

        return super.onKeyPreIme(keyCode, event)
    }
}