package com.bguilherme.financialmanager.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bguilherme.financialmanager.model.bill.dao.BillDao
import com.bguilherme.financialmanager.model.bill.entity.Bill
import com.bguilherme.financialmanager.model.login.dao.UserDao
import com.bguilherme.financialmanager.model.login.entity.User

@Database(entities = [User::class, Bill::class], version = 1, exportSchema = false)
abstract class FinancialManagerDB: RoomDatabase() {

    abstract val userDao: UserDao
    abstract val billDao: BillDao

    companion object {
        @Volatile
        private var INSTANCE: FinancialManagerDB? = null

        fun getInstance(context: Context): FinancialManagerDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FinancialManagerDB::class.java,
                        "financial_manager_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}