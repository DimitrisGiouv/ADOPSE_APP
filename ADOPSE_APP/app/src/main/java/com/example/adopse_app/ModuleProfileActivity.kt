package com.example.adopse_app

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ModuleProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_moduleprofile)
        // Καθορισμός των περιοχών των system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.moduleProfile)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dataModuleName = intent.getStringExtra("moduleName")
        val textModuleView = findViewById<TextView>(R.id.textModuleName)
        textModuleView.text = dataModuleName
        val dataModuleDescription = intent.getStringExtra("moduleDescription")
        val textModuleDescription = findViewById<TextView>(R.id.textModuleDescription)
        textModuleDescription.text = dataModuleDescription
        val dataModuleDifficulty = intent.getStringExtra("moduleDifficulty")
        val textModuleDifficulty = findViewById<TextView>(R.id.textBarDifficulty)
        textModuleDifficulty.text = dataModuleDifficulty
        val dataModulePopularity = intent.getStringExtra("modulePopularity")
        val textModulePopularity = findViewById<TextView>(R.id.textBarPopularity)
        textModulePopularity.text = dataModulePopularity
        val dataModuleRating = intent.getStringExtra("moduleRating")
        val textModuleRating = findViewById<TextView>(R.id.textBarRating)
        textModuleRating.text = dataModuleRating
    }
}