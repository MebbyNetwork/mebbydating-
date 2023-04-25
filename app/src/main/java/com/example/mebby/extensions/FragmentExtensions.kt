package com.example.mebby.extensions

import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    requireContext().hideKeyboard(requireView())
}