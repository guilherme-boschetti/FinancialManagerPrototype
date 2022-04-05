package com.bguilherme.financialmanager.view.settings.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.bguilherme.financialmanager.R
import com.bguilherme.financialmanager.view.AbstractActivity
import com.bguilherme.financialmanager.view.splash.activity.SplashActivity
import com.bguilherme.financialmanager.view.util.SharedPreferencesUtil
import com.bguilherme.financialmanager.view.util.ThemeUtil


class ThemesActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_themes)
        setTitle(R.string.themes)
        ThemeUtil.setViewBackgroundColorPrimaryTransp(this, findViewById(R.id.bkg_parent))
    }

    fun changeTheme(view: View) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Para trocar o tema é necessário reiniciar o aplicativo. Deseja continuar?")
            .setCancelable(false)
            .setPositiveButton("Sim",
                { _, _ ->
                    if (view.id == R.id.theme_night) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    when (view.id) {
                        R.id.theme_night -> saveTheme(ThemeUtil.THEME_NIGHT)
                        R.id.theme_financial_manager -> saveTheme(ThemeUtil.THEME_FINANCIAL_MANAGER)
                        R.id.theme_red -> saveTheme(ThemeUtil.THEME_RED)
                        R.id.theme_green -> saveTheme(ThemeUtil.THEME_GREEN)
                        R.id.theme_blue -> saveTheme(ThemeUtil.THEME_BLUE)
                        R.id.theme_yellow -> saveTheme(ThemeUtil.THEME_YELLOW)
                        R.id.theme_orange -> saveTheme(ThemeUtil.THEME_ORANGE)
                        R.id.theme_pink -> saveTheme(ThemeUtil.THEME_PINK)
                        R.id.theme_purple -> saveTheme(ThemeUtil.THEME_PURPLE)
                        R.id.theme_gray -> saveTheme(ThemeUtil.THEME_GRAY)
                        R.id.theme_black -> saveTheme(ThemeUtil.THEME_BLACK)
                        else -> saveTheme(ThemeUtil.THEME_FINANCIAL_MANAGER)
                    }
                })
            .setNegativeButton("Não", null)

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun saveTheme(value: String) {
        val preferences = SharedPreferencesUtil.getInstance(applicationContext)
        preferences!!.setValue("theme", value)
        //recreate();
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // close activities on stack
        startActivity(intent)
    }
}