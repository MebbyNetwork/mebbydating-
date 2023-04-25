package com.example.mebby.app.customViews

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import com.example.mebby.R
import com.example.mebby.app.listeners.BirthdayKeyListener
import com.example.mebby.app.listeners.BirthdayKeyListenerCallback
import com.example.mebby.app.textWatchers.BirthdayTextWatcher
import com.example.mebby.app.textWatchers.BirthdayTextWatcherCallbacks
import com.example.mebby.enums.BirthdayValue
import com.example.mebby.extensions.hideKeyboard

class BirthdayEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttrs: Int = 0
): LinearLayout(context, attrs, defStyleAttrs) {
    private var editTextDay: EditText
    private var editTextMonth: EditText
    private var editTextYear: EditText

    var editTextArray: Array<EditText>

    var birthday = MutableLiveData("dd/mm/yyyy")

    init {
        LayoutInflater.from(context).inflate(R.layout.birthday_layout, this, true)

        // Init editText
        editTextDay = findViewById(R.id.birthday_day_edit_text)
        editTextMonth = findViewById(R.id.birthday_month_edit_text)
        editTextYear = findViewById(R.id.birthday_year_edit_text)

        editTextArray = arrayOf(editTextDay, editTextMonth, editTextYear)

        // KeyListener
        val birthdayKeyListenersCallback = object : BirthdayKeyListenerCallback {
            override fun keyEventEnter(view: View) {
                view.clearFocus()
                context.hideKeyboard(view)
            }

            override fun keyEventDel(view: View) {
                val index = editTextArray.indexOf(view)
                val editText = editTextArray[index]

                if  (index > 0 && editText.text.toString().isEmpty()) {
                    val editText = editTextArray[editTextArray.indexOf(view) - 1]
                    editText.setSelection(editText.text.toString().length)
                    editText.requestFocus()
                }
            }
        }
        editTextArray.forEach { it.setOnKeyListener(BirthdayKeyListener(birthdayKeyListenersCallback)) }

        // TextWatcher
        val birthdayTextWatcherCallback = object : BirthdayTextWatcherCallbacks {
            override fun onTextChanged(value: String, type: BirthdayValue) {
                when (type) {
                    BirthdayValue.DAY -> {
                        if (value.length == 2) editTextMonth.requestFocus()

                        val string = "dd".replaceRange(0, value.length, value)
                        birthday.value = birthday.value!!.replaceRange(0, 2, string)
                    }
                    BirthdayValue.MONTH -> {
                        if (value.length == 2) editTextYear.requestFocus()

                        val string = "mm".replaceRange(0, value.length, value)
                        birthday.value = birthday.value!!.replaceRange(3, 5, string)
                    }
                    BirthdayValue.YEAR -> {
                        if (value.length == 4) {
                            editTextYear.clearFocus()
                            context.hideKeyboard(rootView)
                        }

                        val day = "yyyy".replaceRange(0, value.length, value)
                        birthday.value = birthday.value!!.replaceRange(6, 10, day)
                    }
                }
            }
        }

        editTextDay.addTextChangedListener(BirthdayTextWatcher(type = BirthdayValue.DAY, callbacks = birthdayTextWatcherCallback))

        editTextMonth.addTextChangedListener(BirthdayTextWatcher(type = BirthdayValue.MONTH, callbacks = birthdayTextWatcherCallback))

        editTextYear.addTextChangedListener(BirthdayTextWatcher(type = BirthdayValue.YEAR, callbacks = birthdayTextWatcherCallback))

    }

    fun setBirthday(string: String) {
        try {
            birthday.value = string
            val date = string.split("/")
            editTextDay.setText(date[0])
            editTextMonth.setText(date[1])
            editTextYear.setText(date[2])
        } catch (e: Exception) {
            Log.d("setBirthday", e.message ?: "Something went wrong")
        }
    }
}