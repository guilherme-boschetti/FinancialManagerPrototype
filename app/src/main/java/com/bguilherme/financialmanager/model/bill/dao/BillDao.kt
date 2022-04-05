package com.bguilherme.financialmanager.model.bill.dao

import androidx.room.*
import com.bguilherme.financialmanager.model.bill.entity.Bill

@Dao
interface BillDao {

    @Insert
    fun insert(bill: Bill): Long

    @Update
    fun update(bill: Bill)

    @Delete
    fun delete(bill: Bill)

    @Query("SELECT * FROM BILL WHERE ID = :id")
    fun getById(id: Long): Bill?
    
    @Query("SELECT * FROM BILL WHERE USER_ID = :userId ORDER BY LIST_ORDER ASC, ID DESC")
    fun getAllBills(userId: Long): List<Bill>
}