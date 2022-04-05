package com.bguilherme.financialmanager

import android.app.Application
import com.bguilherme.financialmanager.viewmodel.login.pojo.UserPojo

class FinancialManagerApplication: Application() {

    var currentUser: UserPojo? = null

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        @Volatile
        private var INSTANCE: FinancialManagerApplication? = null

        @Synchronized
        fun getInstance(): FinancialManagerApplication? {
            if (INSTANCE == null)
                throw IllegalStateException("Configure the application on AndroidManifest.xml")
            return INSTANCE
        }
    }
}