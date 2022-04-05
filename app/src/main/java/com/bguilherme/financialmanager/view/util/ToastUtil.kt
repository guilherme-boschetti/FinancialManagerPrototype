package com.bguilherme.financialmanager.view.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Extension function to simply show a Toast.
 */
fun showToast(context: Context, @StringRes messageResId: Int, durationLong: Boolean) {
    if (durationLong)
        Toast.makeText(context, messageResId, Toast.LENGTH_LONG).show()
    else
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show()
}

/**
 * Extension function to simply show a Toast.
 */
fun showToast(context: Context, message: String, durationLong: Boolean) {
    if (durationLong)
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    else
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}