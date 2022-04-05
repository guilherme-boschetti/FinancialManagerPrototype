package com.bguilherme.financialmanager.view.settings.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.bguilherme.financialmanager.R
import com.bguilherme.financialmanager.view.AbstractActivity
import com.bguilherme.financialmanager.view.util.ThemeUtil

class SettingsActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setTitle(R.string.settings)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        ThemeUtil.setViewBackgroundColorPrimaryTransp(this, findViewById(R.id.bkg_parent))

        val btnThemes: Button = findViewById<View>(R.id.btn_themes) as Button
        btnThemes.setOnClickListener({
            val intent = Intent(this, ThemesActivity::class.java)
            startActivity(intent)
        })
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val txtWarningThemes: TextView = findViewById<View>(R.id.txt_warning_themes) as TextView
            txtWarningThemes.visibility = View.VISIBLE;
            btnThemes.isEnabled = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}