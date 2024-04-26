package com.example.adopse_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_navigation)

        // Καθορισμός των περιοχών των system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.navigation)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
            // Κουμπί που μεταφέρει στην αρχική οθόνη
            val closeWindows = findViewById<ImageButton>(R.id.close_button)
            closeWindows.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        val profilePage = findViewById<Button>(R.id.btn_profile)
        profilePage.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
        // Κουμπί που μεταφέρει στην οθόνη με τις ενότητες
        val modulePage = findViewById<Button>(R.id.btn_modules)
        modulePage.setOnClickListener {
            val intent = Intent(this, ModuleActivity::class.java)
            startActivity(intent)
        }

    }
}