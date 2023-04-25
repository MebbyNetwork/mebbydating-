package com.example.mebby.app.listeners

import android.view.KeyEvent
import android.view.View
import android.widget.EditText

interface BirthdayKeyListenerCallback {
    fun keyEventEnter(view: View)
    fun keyEventDel(view: View)
}

class BirthdayKeyListener(
    private val callbacks: BirthdayKeyListenerCallback
    ): View.OnKeyListener {

    override fun onKey(v: View, keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action != KeyEvent.ACTION_DOWN) return false

        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            callbacks.keyEventEnter(v)
            return false
        }

        else if (keyCode == KeyEvent.KEYCODE_DEL) {
            callbacks.keyEventDel(v)
            return false
        }
        return false
    }
}