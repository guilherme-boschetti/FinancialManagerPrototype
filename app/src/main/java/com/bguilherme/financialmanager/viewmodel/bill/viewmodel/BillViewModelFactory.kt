package com.bguilherme.financialmanager.viewmodel.bill.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bguilherme.financialmanager.model.bill.dao.BillDao

class BillViewModelFactory(dataSource: BillDao): ViewModelProvider.Factory {

    private val dataSource: BillDao

    init {
        this.dataSource = dataSource
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BillViewModel::class.java))
            return BillViewModel(dataSource) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}