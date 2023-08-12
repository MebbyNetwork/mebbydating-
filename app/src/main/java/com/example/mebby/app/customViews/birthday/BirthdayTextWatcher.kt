package com.example.mebby.app.customViews.birthday

import android.text.Editable
import android.text.TextWatcher
import com.example.domain.sealed.Birthday

interface BirthdayTextWatcherCallbacks {
    fun onTextChanged(value: String,type: Birthday)
}

class BirthdayTextWatcher(
    private val type: Birthday,
    private val callbacks: BirthdayTextWatcherCallbacks,
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        val value = s.toString()
        callbacks.onTextChanged(value, type)
    }
}