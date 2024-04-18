package com.example.adopse_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}

