package com.bguilherme.financialmanager.view.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.bguilherme.financialmanager.R

internal object ThemeUtil {
    // https://medium.com/mindorks/mastering-android-themes-chapter-4-591e03320182

    const val THEME_NIGHT = "ThemeNight" // On Night Mode
    const val THEME_FINANCIAL_MANAGER = "ThemeFinancialManager"
    const val THEME_RED = "ThemeRed"
    const val THEME_GREEN = "ThemeGreen"
    const val THEME_BLUE = "ThemeBlue"
    const val THEME_YELLOW = "ThemeYellow"
    const val THEME_ORANGE = "ThemeOrange"
    const val THEME_PINK = "ThemePink"
    const val THEME_PURPLE = "ThemePurple"
    const val THEME_GRAY = "ThemeGray"
    const val THEME_BLACK = "ThemeBlack"

    fun isSystemNightMode(context: Context): Boolean {
        val currentNightMode: Int =
            context.resources.getConfiguration().uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO ->                 // Night mode is not active, we're using the light theme
                return false
            Configuration.UI_MODE_NIGHT_YES ->                 // Night mode is active, we're using dark theme
                return true
        }
        return false
    }

    private fun getPrimaryColor(context: Context): Int {
        val ta: TypedArray = context.theme.obtainStyledAttributes(R.styleable.ThemeStyleColors)
        return ta.getColor(
            R.styleable.ThemeStyleColors_themePrimary,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
    }

    fun setViewBackgroundColorPrimaryTransp(context: Context, view: View?) {
        if (view != null)
            view.setBackgroundColor(ColorUtils.setAlphaComponent(getPrimaryColor(context), 63)) // between 0 - 255
    }
}