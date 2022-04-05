package com.bguilherme.financialmanager.view.login.actitity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bguilherme.financialmanager.FinancialManagerApplication
import com.bguilherme.financialmanager.R
import com.bguilherme.financialmanager.model.database.FinancialManagerDB
import com.bguilherme.financialmanager.view.AbstractActivity
import com.bguilherme.financialmanager.view.util.showToast
import com.bguilherme.financialmanager.view.bill.activity.BillsActivity
import com.bguilherme.financialmanager.view.util.SharedPreferencesUtil
import com.bguilherme.financialmanager.view.util.ThemeUtil
import com.bguilherme.financialmanager.viewmodel.login.pojo.UserPojo
import com.bguilherme.financialmanager.viewmodel.login.viewmodel.LoginViewModel
import com.bguilherme.financialmanager.viewmodel.login.viewmodel.LoginViewModelFactory

class LoginActivity : AbstractActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ThemeUtil.setViewBackgroundColorPrimaryTransp(this, findViewById(R.id.bkg_parent))

        val email = findViewById<EditText>(R.id.edt_email)
        val password = findViewById<EditText>(R.id.edt_password)
        val login = findViewById<Button>(R.id.login)
        val register = findViewById<Button>(R.id.register)
        val loading = findViewById<ProgressBar>(R.id.loading)

        // Set the email in shared preferences
        val savedEmail =
            SharedPreferencesUtil.getInstance(applicationContext)?.getValue("email", null)

        if (savedEmail != null)
            email.setText(savedEmail)

        val application = requireNotNull(this@LoginActivity).application
        val dataSource = FinancialManagerDB.getInstance(application).userDao

        loginViewModel = ViewModelProvider(this@LoginActivity, LoginViewModelFactory(dataSource))
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both email / password is valid
            login.isEnabled = loginState.isDataValid
            register.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                email.error = getString(loginState.emailError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.INVISIBLE
            if (loginResult.error != null) {
                showToast(applicationContext, loginResult.error, true)
                return@Observer
            }
            if (loginResult.success != null) {
                loginSucessful(loginResult.success)
                return@Observer
            }
        })

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        if (loginViewModel.loginDataChanged(
                                email.text.toString(),
                                password.text.toString()
                            )
                        ) {
                            loading.visibility = View.VISIBLE
                            loginViewModel.login(email.text.toString(), password.text.toString())
                        }
                    }
                }
                false
            }

            login.setOnClickListener {
                if (loginViewModel.loginDataChanged(
                        email.text.toString(),
                        password.text.toString()
                    )
                ) {
                    loading.visibility = View.VISIBLE
                    loginViewModel.login(email.text.toString(), password.text.toString())
                }
            }

            register.setOnClickListener {
                if (loginViewModel.loginDataChanged(
                        email.text.toString(),
                        password.text.toString()
                    )
                ) {
                    loading.visibility = View.VISIBLE
                    loginViewModel.register(email.text.toString(), password.text.toString())
                }
            }
        }
    }

    private fun loginSucessful(user: UserPojo) {
        val welcome = getString(R.string.welcome)
        val email = user.email
        showToast(applicationContext, welcome, true) // "$welcome $email"

        // Set the current user in Application
        FinancialManagerApplication.getInstance()?.currentUser = user

        // Ser the email in shared preferences
        SharedPreferencesUtil.getInstance(applicationContext)?.setValue("email", email)

        // Start next Activity
        val intent = Intent(this, BillsActivity::class.java)
        startActivity(intent)

        // Complete and destroy Login Activity once successful
        setResult(Activity.RESULT_OK)
        finish()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
