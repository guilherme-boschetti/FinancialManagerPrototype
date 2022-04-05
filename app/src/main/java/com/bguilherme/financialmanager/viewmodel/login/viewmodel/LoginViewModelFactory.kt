package com.bguilherme.financialmanager.viewmodel.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bguilherme.financialmanager.model.login.dao.UserDao

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel
 */
class LoginViewModelFactory(dataSource: UserDao) : ViewModelProvider.Factory {

    private val dataSource: UserDao

    init {
        this.dataSource = dataSource
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
