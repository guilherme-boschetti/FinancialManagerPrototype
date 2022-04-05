package com.bguilherme.financialmanager.view.test.activity

import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.bguilherme.financialmanager.R
import com.bguilherme.financialmanager.Res
import com.bguilherme.financialmanager.view.AbstractActivity
import com.bguilherme.financialmanager.view.about.activity.AboutActivity
import com.bguilherme.financialmanager.view.settings.activity.SettingsActivity
import com.bguilherme.financialmanager.view.util.SharedPreferencesUtil
import com.bguilherme.financialmanager.view.widget.ColorPickerDialog

class TestActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        setTitle(R.string.test)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
            }
        }

        findViewById<View>(R.id.bkg_parent).setBackgroundColor(
            ColorUtils.setAlphaComponent(ContextCompat.getColor(this, R.color.colorPrimary), 63)
        )

        val preferences = SharedPreferencesUtil.getInstance(applicationContext)

        val btnTestPrimary: Button = findViewById<View>(R.id.btn_colorPicker_test_primary) as Button
        btnTestPrimary.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        btnTestPrimary.setOnClickListener({
            ColorPickerDialog(this@TestActivity,
                ColorPickerDialog.OnColorChangedListener {
                    preferences!!.setValue("themePrimary", it)
                    //btnTestPrimary.setBackgroundColor(it)
                },
                0
            ).show()
        })

        val btnTestPrimaryDark: Button = findViewById<View>(R.id.btn_colorPicker_test_primary_dark) as Button
        btnTestPrimaryDark.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        btnTestPrimaryDark.setOnClickListener({
            ColorPickerDialog(this@TestActivity,
                ColorPickerDialog.OnColorChangedListener {
                    preferences!!.setValue("themePrimaryDark", it)
                    //btnTestPrimaryDark.setBackgroundColor(it)
                },
                0
            ).show()
        })

        val btnTestAccent: Button = findViewById<View>(R.id.btn_colorPicker_test_accent) as Button
        btnTestAccent.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        btnTestAccent.setOnClickListener({
            ColorPickerDialog(this@TestActivity,
                ColorPickerDialog.OnColorChangedListener {
                    preferences!!.setValue("themeAccent", it)
                    //btnTestAccent.setBackgroundColor(it)
                    recreate()
                },
                0
            ).show()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private var res: Res? = null

    override fun getResources(): Resources? {
        if (res == null) {
            res = Res(super.getResources())
        }
        return res
    }
}