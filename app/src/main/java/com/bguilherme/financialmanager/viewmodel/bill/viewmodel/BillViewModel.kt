package com.bguilherme.financialmanager.viewmodel.bill.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bguilherme.financialmanager.model.bill.dao.BillDao
import com.bguilherme.financialmanager.model.bill.entity.Bill
import com.bguilherme.financialmanager.view.bill.adapter.DataItem
import kotlinx.coroutines.*

class BillViewModel(billDao: BillDao) : ViewModel() {

    private val _billsUpdated = MutableLiveData<Boolean>()
    val billsUpdated: LiveData<Boolean> get() = _billsUpdated

    var bills: List<Bill>? = null

    private var dataSourceJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + dataSourceJob)

    private val billDao: BillDao

    init {
        this.billDao = billDao
        _billsUpdated.value = false
        Log.i("BillViewModel", "BillViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("BillViewModel", "BillViewModel destroyed!")
    }

    fun addBill(bill: Bill, reAdd: Boolean) {
        coroutineScope.launch {
            insert(bill, reAdd)
        }
    }

    private suspend fun insert(bill: Bill, reAdd: Boolean) {
        withContext(Dispatchers.IO) {
            billDao.insert(bill)
        }
        if (!reAdd)
            getBills(bill.userId!!)
    }

    fun updateBill(bill: Bill) {
        coroutineScope.launch {
            update(bill)
        }
    }

    private suspend fun update(bill: Bill) {
        withContext(Dispatchers.IO) {
            billDao.update(bill)
        }
        getBills(bill.userId!!)
    }

    fun deleteBill(bill: Bill) {
        coroutineScope.launch {
            delete(bill)
        }
    }

    private suspend fun delete(bill: Bill) {
        withContext(Dispatchers.IO) {
            billDao.delete(bill)
        }
    }

    fun updateBillsOrder(listBills: List<DataItem>?) {
        if (listBills != null) {
            coroutineScope.launch {
                updateOrders(listBills)
            }
        }
    }

    private suspend fun updateOrders(listBills: List<DataItem>) {
        withContext(Dispatchers.IO) {
            for (i in 0..listBills.size - 1) {
                val bill = listBills[i].bill
                if (bill != null) {
                    bill.listOrder = i
                    billDao.update(bill)
                }
            }
        }
    }

    fun getBills(userId: Long) {
        coroutineScope.launch {
            getAllBills(userId)
        }
    }

    private suspend fun getAllBills(userId: Long) {
        withContext(Dispatchers.IO) {
            bills = billDao.getAllBills(userId)
        }
        _billsUpdated.value = true
    }
}