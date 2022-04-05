package com.bguilherme.financialmanager

import android.content.res.Resources
import android.os.Build
import com.bguilherme.financialmanager.view.util.SharedPreferencesUtil

class Res(original: Resources) : Resources(original.getAssets(), original.getDisplayMetrics(), original.getConfiguration()) {

    @Throws(NotFoundException::class)
    override fun getColor(id: Int): Int {
        return getColor(id, null)
    }

    @Throws(NotFoundException::class)
    override fun getColor(id: Int, theme: Theme?): Int {

        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.getColor(id, theme)
        } else super.getColor(id)

        val preferences = SharedPreferencesUtil.getInstance(FinancialManagerApplication.getInstance()!!.applicationContext)

        return when (getResourceEntryName(id)) {
            "colorPrimary" -> preferences?.getValue("themePrimary", color)!!
            "colorPrimaryDark" -> preferences?.getValue("themePrimaryDark", color)!!
            "colorAccent" -> preferences?.getValue("themeAccent", color)!!
            else -> color
        }
    }
}