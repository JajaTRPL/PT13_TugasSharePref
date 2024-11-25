package com.example.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    companion object {
        private const val PREF_NAME = "user_pref"
        private var instance: PrefManager? = null

        fun getInstance(context: Context): PrefManager {
            if (instance == null) {
                instance = PrefManager(context.applicationContext)
            }
            return instance!!
        }
    }

    // Fungsi untuk menyimpan username
    fun saveUsername(username: String) {
        editor.putString("USERNAME", username).apply()
    }

    // Fungsi untuk mendapatkan username
    fun getUsername(): String {
        return sharedPreferences.getString("USERNAME", "") ?: ""
    }

    // Fungsi untuk menghapus username
    fun clearUsername() {
        editor.remove("USERNAME").apply()
    }

    // Fungsi untuk menghapus semua data sesi
    fun clearAll() {
        editor.clear().apply()
    }
}
