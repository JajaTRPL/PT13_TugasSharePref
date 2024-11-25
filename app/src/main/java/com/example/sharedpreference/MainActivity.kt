package com.example.sharedpreference

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.sharedpreference.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var prefManager: PrefManager
    private val channelID = "TES_NOTIF"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()

        with(binding) {
            // Tombol login
            btnLogin.setOnClickListener {
                val usernameUntukLogin = "admin"
                val passwordUntukLogin = "12345"
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Username dan password harus diisi",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (username == usernameUntukLogin && password == passwordUntukLogin) {
                    prefManager.saveUsername(username)
                    checkLoginStatus()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Username atau password salah",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // Tombol logout
            btnLogout.setOnClickListener {
                prefManager.clearUsername()
                Log.d("pp", prefManager.getUsername())
                checkLoginStatus()
            }

            // Tombol clear
            btnClear.setOnClickListener {
                prefManager.clearUsername()
                checkLoginStatus()
            }

            // Tombol kirim notifikasi
            btnNotif.setOnClickListener {
                val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE
                } else {
                    0
                }

                // Intent untuk tombol "Baca Notif"
                val intentBacaNotif = Intent(this@MainActivity, NotifReceiver::class.java)
                    .putExtra("MESSAGE", "Baca selengkapnya ...")
                val pendingIntentBacaNotif = PendingIntent.getBroadcast(
                    this@MainActivity,
                    0,
                    intentBacaNotif,
                    flag
                )

                // Intent untuk tombol "Logout"
                val intentLogout = Intent(this@MainActivity, LogoutReceiver::class.java)
                val pendingIntentLogout = PendingIntent.getBroadcast(
                    this@MainActivity,
                    1,
                    intentLogout,
                    flag
                )

                // Membangun notifikasi
                val builder = NotificationCompat.Builder(this@MainActivity, channelID)
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Notifikasi PPPB")
                    .setContentText("Hello from the other side")
                    .setAutoCancel(true)
                    .addAction(0, "Baca Notif", pendingIntentBacaNotif)
                    .addAction(0, "Logout", pendingIntentLogout)

                // Kirim notifikasi
                val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val notificationChannel = NotificationChannel(
                        channelID,
                        "Notif PPPB",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    with(notificationManager) {
                        createNotificationChannel(notificationChannel)
                        notify(0, builder.build())
                    }
                } else {
                    notificationManager.notify(0, builder.build())
                }
            }
        }
    }

    // Cek status login pengguna
    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.getUsername()
        Log.d("islogin", isLoggedIn)
        if (isLoggedIn.isEmpty()) {
            // Jika tidak login
            binding.llLogin.visibility = View.VISIBLE
            binding.llLogged.visibility = View.GONE
        } else {
            // Jika login
            binding.llLogin.visibility = View.GONE
            binding.llLogged.visibility = View.VISIBLE
            binding.txtUsername.text = "Hello, $isLoggedIn"
        }
    }
}
