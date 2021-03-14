package com.remlexworld.moviedemo.util

import android.content.Context
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.dismissKeyboard(view : View?) {

    view?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }

}


fun Context.showToast(message : String) {

    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

}