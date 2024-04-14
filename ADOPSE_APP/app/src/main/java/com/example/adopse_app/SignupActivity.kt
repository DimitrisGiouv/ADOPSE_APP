package com.example.adopse_app

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
class SignupActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge();
        setContentView(R.layout.activity_signup)

        val btnSignup = findViewById<LinearLayout>(R.id.register_button)

        btnSignup.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}