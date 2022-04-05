package com.bguilherme.financialmanager.model.login.dao

import androidx.room.*
import com.bguilherme.financialmanager.model.login.entity.User

@Dao
interface UserDao {

    @Insert
    fun insert(user: User): Long

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM USER WHERE ID = :id")
    fun getById(id: Long): User?

    @Query("SELECT * FROM USER WHERE EMAIL = :email")
    fun getByEmail(email: String): User?

    @Query("SELECT * FROM USER ORDER BY ID DESC")
    fun getAllUsers(): List<User>
}