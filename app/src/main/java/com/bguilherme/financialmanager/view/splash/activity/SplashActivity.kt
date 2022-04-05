package com.bguilherme.financialmanager.view.splash.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.bguilherme.financialmanager.R
import com.bguilherme.financialmanager.view.AbstractActivity
import com.bguilherme.financialmanager.view.login.actitity.LoginActivity

class SplashActivity : AbstractActivity() {

    private val SPLASH_DELAY = 2000.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        getSupportActionBar()?.hide()

        Handler().postDelayed(
            Runnable {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, SPLASH_DELAY
        )
    }
}
