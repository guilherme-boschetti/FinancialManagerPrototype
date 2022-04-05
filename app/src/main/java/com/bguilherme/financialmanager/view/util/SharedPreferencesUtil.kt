package com.bguilherme.financialmanager.view.util

import android.content.Context
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class SharedPreferencesUtil(context: Context) {

    private var instance: SharedPreferencesUtil? = null
    private var preferences: SharedPreferences? = null
    private var editor: Editor? = null

    init {
        preferences = context.getSharedPreferences("prefs", MODE_PRIVATE)
    }

    companion object {
        @Volatile
        private var INSTANCE: SharedPreferencesUtil? = null

        @Synchronized
        fun getInstance(context: Context): SharedPreferencesUtil? {
            var instance = INSTANCE
            if (instance == null) {
                instance = SharedPreferencesUtil(context)
            }
            return instance
        }
    }

    @Synchronized
    fun getInstance(context: Context): SharedPreferencesUtil? {
        if (instance == null) {
            instance = SharedPreferencesUtil(context)
        }
        return instance
    }

    /**
     * Return String value of map
     *
     * @param key the key
     * @param returnOnNull value returned in case the not exist in map
     * @return string value
     */
    fun getValue(key: String?, returnOnNull: String?): String? {
        return preferences!!.getString(key, returnOnNull)
    }

    /**
     * Return  boolean value of map
     *
     * @param key the key
     * @param returnOnNull value returned in case the not exist in map
     * @return boolean value
     */
    fun getValue(key: String?, returnOnNull: Boolean): Boolean {
        return preferences!!.getBoolean(key, returnOnNull)
    }

    /**
     * Return int value of map
     *
     * @param key the key
     * @param returnOnNull value returned in case the not exist in map
     * @return int value
     */
    fun getValue(key: String?, returnOnNull: Int): Int {
        return preferences!!.getInt(key, returnOnNull)
    }

    /**
     * Set String value in map
     *
     * @param key  the key
     * @param value the string value
     */
    fun setValue(key: String?, value: String?) {
        editor = preferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }


    /**
     * Set boolean value in map
     *
     * @param key  the key
     * @param value the boolean value
     */
    fun setValue(key: String?, value: Boolean) {
        editor = preferences?.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    /**
     * Set int value in map
     *
     * @param key  the key
     * @param value the int value
     */
    fun setValue(key: String?, value: Int) {
        editor = preferences?.edit()
        editor?.putInt(key, value)
        editor?.apply()
    }

    /**
     * Remove a key of map
     *
     * @param key the key
     */
    fun remove(key: String?) {
        editor = preferences?.edit()
        editor?.remove(key)
        editor?.apply()
    }
}