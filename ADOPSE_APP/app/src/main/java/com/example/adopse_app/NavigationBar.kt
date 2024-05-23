package com.example.adopse_app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class NavigationBar: AppCompatActivity() {

    fun NavigationCode(activity: Activity) {
        val navigationButton = activity.findViewById<ImageButton>(R.id.navigation_button)
        navigationButton.setOnClickListener {
            val intent = Intent(activity, NavigationActivity::class.java)
            activity.startActivity(intent)
        }


        // Κουμπί που μεταφέρει στην οθόνη εισόδου
        if (isLoggedIn(activity)) {
            val loginPage = activity.findViewById<ImageButton>(R.id.user_button)
            loginPage.setOnClickListener {
                val intent = Intent(activity, UserProfileActivity::class.java)
                activity.startActivity(intent)
            }
        } else {
            val loginPage = activity.findViewById<ImageButton>(R.id.user_button)
            loginPage.setOnClickListener {
                val intent = Intent(activity, LoginActivity::class.java)
                activity.startActivity(intent)
            }
        }

        val logobutton = activity.findViewById<ImageButton>(R.id.logo_button)
        logobutton.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }
    private fun isLoggedIn(activity: Activity): Boolean {
        val sharedPreferences = activity.getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
        return sharedPreferences.contains("isLogged")
    }
}

