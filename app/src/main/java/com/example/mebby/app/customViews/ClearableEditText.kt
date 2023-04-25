package com.example.mebby.app.customViews

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import com.example.mebby.R
import com.example.mebby.extensions.hideKeyboard
import java.lang.reflect.TypeVariable

class ClearableEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): RelativeLayout(context, attrs, defStyleAttr) {
    var editText: CustomEditText
    var clearButton: ImageButton

    init {
        LayoutInflater.from(context).inflate(R.layout.clearable_edit_text_layout, this, true)

        editText = findViewById(R.id.clearable_custom_edit_text)
        clearButton = findViewById(R.id.clearable_custom_clear)

        clearButton.setOnClickListener {
            editText.setText("")
        }


        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.ClearableEditText, 0, 0)
        try {
            val maxLength = a.getInt(R.styleable.ClearableEditText_maxLength, 0)
            if (maxLength > 0) {
                editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
            }

            val text = a.getString(R.styleable.ClearableEditText_text)
            editText.setText(text)

            val textColor = a.getColor(R.styleable.ClearableEditText_textColor, resources.getColor(R.color.dark_grey_normal))
            editText.setTextColor(textColor)

            val textSize = a.getDimension(R.styleable.ClearableEditText_textSize, 14f)
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)

//            val fontFamily = a.getString(R.styleable.ClearableEditText_fontFamily)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val font = Typeface.createFromAsset(context.assets, fontFamily)
//                editText.typeface = font
//            }

            editText.setCompoundDrawablesWithIntrinsicBounds(
                a.getDrawable(R.styleable.ClearableEditText_leftDrawable), null,
                null, null
            )

            val hint = a.getString(R.styleable.ClearableEditText_hint)
            editText.setHint(hint)

            // TODO

        } finally {
            a.recycle()
        }


        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editText.setOnKeyListener { v, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                v.clearFocus()
                context.hideKeyboard(v)
            }
            true
        }
    }

    fun setText(text: CharSequence?) {
        editText.setText(text)
    }

    fun getText(): CharSequence {
        return editText.text.toString()
    }
}