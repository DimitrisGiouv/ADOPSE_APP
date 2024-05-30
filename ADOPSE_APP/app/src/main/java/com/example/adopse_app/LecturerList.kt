package com.example.adopse_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.TimeoutError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
class LecturerList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lecturer_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_student)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navigationCode = NavigationBar()
        navigationCode.NavigationCode(this)

        val logoutButton = findViewById<Button>(R.id.logout_button)
        logoutButton.setOnClickListener {
            // Perform logout actions
            val sharedPreferences = getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            // Redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.lecturers_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sample data
        val lecturers = listOf(
            Lecturer("Dr. Smith", "Professor of Mathematics", R.drawable.lecturer_image_placeholder),
            Lecturer("Dr. Johnson", "Associate Professor of Physics", R.drawable.lecturer_image_placeholder),
            Lecturer("Dr. Williams", "Senior Lecturer in Chemistry", R.drawable.lecturer_image_placeholder),
            Lecturer("Dr. Brown", "Lecturer in Biology", R.drawable.lecturer_image_placeholder),
            Lecturer("Dr. Jones", "Assistant Professor of Computer Science", R.drawable.lecturer_image_placeholder)
        )

        val adapter = LecturerAdapter(lecturers)
        recyclerView.adapter = adapter
    }
}