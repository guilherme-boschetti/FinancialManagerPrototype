package com.bguilherme.financialmanager.viewmodel.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bguilherme.financialmanager.R
import com.bguilherme.financialmanager.model.login.dao.UserDao
import com.bguilherme.financialmanager.model.login.entity.User
import com.bguilherme.financialmanager.viewmodel.login.pojo.UserPojo
import com.bguilherme.financialmanager.viewmodel.login.result.LoginResult
import com.bguilherme.financialmanager.viewmodel.login.result.Result
import kotlinx.coroutines.*
import java.security.MessageDigest

class LoginViewModel(userDao: UserDao) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private var dataSourceJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + dataSourceJob)

    private val userDao: UserDao

    init {
        this.userDao = userDao
    }

    override fun onCleared() {
        super.onCleared()
        dataSourceJob.cancel()
    }

    fun login(email: String, password: String) {
        var error = false
        try {
            val user = User()
            user.email = email
            user.password = password

            coroutineScope.launch {
                getUserToLogin(user, false)
            }
        } catch (e: Throwable) {
            error = true
        }

        if (error) {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    private suspend fun getUserToLogin(user: User, register: Boolean) {
        var userCheck: User? = null
        withContext(Dispatchers.IO) {
            userCheck = userDao.getByEmail(user.email ?: "")
        }
        if (register) {
            if (userCheck == null) {
                coroutineScope.launch {
                    insert(user)
                }
            } else {
                _loginResult.value = LoginResult(error = R.string.email_already_registered)
            }
        } else {
            checkLogin(user, userCheck)
        }
    }

    private fun checkLogin(user: User, userCheck: User?) {
        lateinit var result: Result<UserPojo>
        var success = false
        if (userCheck != null && userCheck.password.equals(md5(user.password))) {
            val userPojo = UserPojo(userCheck.id ?: 0, userCheck.email ?: "")
            result = Result.Success(userPojo)
            success = true
        }

        if (!success) {
            result = Result.Error(Exception("Error logging in"))
        }

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = UserPojo(id = result.data.id, email = result.data.email))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun register(email: String, password: String) {
        var error = false
        try {
            val newUser = User()
            newUser.email = email
            newUser.password = md5(password)

            coroutineScope.launch {
                getUserToLogin(newUser, true)
            }
        } catch (e: Throwable) {
            error = true
        }

        if (error) {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    private suspend fun insert(user: User) {
        var id = -1L
        withContext(Dispatchers.IO) {
            id = userDao.insert(user)
        }
        registered(user, id)
    }

    private fun registered(user: User, id: Long) {
        lateinit var result: Result<UserPojo>
        try {
            val newUserPojo = UserPojo(id, user.email ?: "")
            result = Result.Success(newUserPojo)
        } catch (e: Throwable) {
            result = Result.Error(Exception("Error logging in", e))
        }

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = UserPojo(id = result.data.id, email = result.data.email))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(email: String, password: String): Boolean {
        if (!isEmailValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
            return true
        }
        return false
    }

    // A placeholder email validation check
    private fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 0
    }

    private fun md5(s: String?): String? {
        val MD5 = "MD5"
        try {
            // Create MD5 Hash
            val digest = MessageDigest
                .getInstance(MD5)
            digest.update((s ?: "").toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
