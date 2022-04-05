package com.bguilherme.financialmanager.view.about.activity

import android.os.Bundle
import android.view.MenuItem
import com.bguilherme.financialmanager.R
import com.bguilherme.financialmanager.view.AbstractActivity
import com.bguilherme.financialmanager.view.util.ThemeUtil

class AboutActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setTitle(R.string.about)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        ThemeUtil.setViewBackgroundColorPrimaryTransp(this, findViewById(R.id.bkg_parent))
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