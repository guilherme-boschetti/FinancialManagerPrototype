package com.bguilherme.financialmanager.model.bill.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.bguilherme.financialmanager.model.login.entity.User

@Entity(tableName = "BILL")
data class Bill (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var id: Long? = null,

    @ForeignKey(entity = User::class, parentColumns = ["ID"], childColumns = ["USER_ID"])
    @ColumnInfo(name = "USER_ID")
    var userId: Long? = null,

    @ColumnInfo(name = "DESCRIPTION")
    var description: String? = null,

    @ColumnInfo(name = "VALUE")
    var value: Double? = null,

    @ColumnInfo(name = "PAID")
    var paid: Boolean? = null,

    @ColumnInfo(name = "FIXED")
    var fixed: Boolean? = null,

    @ColumnInfo(name = "EXPIRATION_DATE")
    var expirationDate: String? = null,

    @ColumnInfo(name = "INCLUSION_DATE")
    var inclusionDate: String? = null,

    @ColumnInfo(name = "LIST_ORDER")
    var listOrder: Int? = null
)