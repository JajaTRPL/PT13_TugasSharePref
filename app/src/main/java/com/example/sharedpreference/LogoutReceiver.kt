package com.example.sharedpreference

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class LogoutReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Mendapatkan instance PrefManager
        val prefManager = PrefManager.getInstance(context)

        // Menghapus data sesi pengguna dari SharedPreferences
        prefManager.clearAll()

        // Menampilkan pesan logout berhasil
        Toast.makeText(context, "Logout berhasil. Data sesi telah dihapus.", Toast.LENGTH_SHORT).show()

        // Restart aplikasi untuk memastikan tampilan diperbarui
        val restartIntent = Intent(context, MainActivity::class.java)
        restartIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(restartIntent)
    }
}
