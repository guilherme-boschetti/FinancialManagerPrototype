package com.bguilherme.financialmanager.model.login.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "USER")
data class User (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var id: Long? = null,

    @ColumnInfo(name = "EMAIL")
    var email: String? = null,

    @ColumnInfo(name = "PASSWORD")
    var password: String? = null
)