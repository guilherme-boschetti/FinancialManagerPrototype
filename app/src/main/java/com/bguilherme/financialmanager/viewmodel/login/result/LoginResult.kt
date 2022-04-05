package com.bguilherme.financialmanager.viewmodel.login.result

import com.bguilherme.financialmanager.viewmodel.login.pojo.UserPojo

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: UserPojo? = null,
    val error: Int? = null
)
