package com.example.adopse_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


    class SecondActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_second)
            var btnSecondActivity = findViewById<Button>(R.id.btnMainPage)

            btnSecondActivity.setOnClickListener {
                val intent = Intent(this, MainPage::class.java)
                startActivity(intent)
            }


        }
    }