package com.bguilherme.financialmanager.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bguilherme.financialmanager.R
import com.bguilherme.financialmanager.view.util.SharedPreferencesUtil
import com.bguilherme.financialmanager.view.util.ThemeUtil

abstract class AbstractActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getSavedTheme())
    }

    private fun getSavedTheme(): Int {
        if (ThemeUtil.isSystemNightMode(this)) {
            return R.style.AppTheme_Night
        } else {
            val savedTheme = getSavedThemePrefs()
            when (savedTheme) {
                ThemeUtil.THEME_NIGHT -> return R.style.AppTheme_Night
                ThemeUtil.THEME_FINANCIAL_MANAGER -> return R.style.AppTheme_FinancialManager
                ThemeUtil.THEME_RED -> return R.style.AppTheme_Red
                ThemeUtil.THEME_GREEN -> return R.style.AppTheme_Green
                ThemeUtil.THEME_BLUE -> return R.style.AppTheme_Blue
                ThemeUtil.THEME_YELLOW -> return R.style.AppTheme_Yellow
                ThemeUtil.THEME_ORANGE -> return R.style.AppTheme_Orange
                ThemeUtil.THEME_PINK -> return R.style.AppTheme_Pink
                ThemeUtil.THEME_PURPLE -> return R.style.AppTheme_Purple
                ThemeUtil.THEME_GRAY -> return R.style.AppTheme_Gray
                ThemeUtil.THEME_BLACK -> return R.style.AppTheme_Black
                else -> return R.style.AppTheme_FinancialManager
            }
        }
    }

    protected fun getSavedThemePrefs(): String {
        val preferences = SharedPreferencesUtil.getInstance(applicationContext)
        return preferences?.getValue("theme", ThemeUtil.THEME_FINANCIAL_MANAGER)!!
    }
}