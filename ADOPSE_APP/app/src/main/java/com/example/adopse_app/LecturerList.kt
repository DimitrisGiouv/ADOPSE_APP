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
import com.android.volley.toolbox.JsonArrayRequest

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

        //val navigationCode = NavigationBar()
        //navigationCode.NavigationCode(this)

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
        val queue = Volley.newRequestQueue(this)
        val url = "http://185.234.52.109/api/Lecturer"
        val request = JsonArrayRequest(Request.Method.GET, url, null,
            {response ->
                var data = response.getJSONObject(0)
                val list = mutableListOf(Lecturer(data.getString("name"),data.getString("email"),R.drawable.lecturer_image_placeholder))
                for (i in 1 until response.length()){
                    data = response.getJSONObject(i)
                    list += (Lecturer(data.getString("name"),data.getString("email"),R.drawable.lecturer_image_placeholder))
                    //Toast.makeText(this, list.toString(), Toast.LENGTH_SHORT).show()
                }

                val adapter = LecturerAdapter(list)
                recyclerView.adapter = adapter


        },{error -> {}
        })

        queue.add(request)

    }
}