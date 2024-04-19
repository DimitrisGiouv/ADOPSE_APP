package com.example.adopse_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Καθορισμός των περιοχών των system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Κουμπί που μεταφέρει στην οθόνη πλοήγησης
        val navigationButton = findViewById<ImageButton>(R.id.navigation_button)
           navigationButton.setOnClickListener {
           val intent = Intent(this, NavigationActivity::class.java)
           startActivity(intent)
        }
        
        // Κουμπί που μεταφέρει στην οθόνη εισόδου
//        val loginPage = findViewById<ImageButton>(R.id.User)
//        loginPage.setOnClickListener {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }

        val profilePage =findViewById<ImageButton>(R.id.User)
        profilePage.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        // Κουμπί που μεταφέρει στην οθόνη μια λιστας
        val gridModules = findViewById<ImageButton>(R.id.gridViewButton)
        gridModules.setOnClickListener {
            twoModuleList()
        }
        // Κουμπί που μεταφέρει στην οθόνη δυο λίστας
        val listModules = findViewById<ImageButton>(R.id.listViewButton)
        listModules.setOnClickListener {
            singleModuleList()
            listModules.isPressed = true
        }

        // Οταν ανοιγη το app τοτε θα φορτωθει η μια λιστα
        twoModuleList()
        }

    fun singleModuleList() {
        val parentLayout: ConstraintLayout = findViewById(R.id.LinearModules)
        parentLayout.removeAllViews()
        repeat(10) { index ->
            val moduleCard1 = layoutInflater.inflate(R.layout.module_long, null) as ConstraintLayout
            moduleCard1.id = View.generateViewId()

            val moduleTextView = moduleCard1.findViewById<TextView>(R.id.module1)
            val disModuleTextView = moduleCard1.findViewById<TextView>(R.id.Dismodule1)
            val difficultyTextView = moduleCard1.findViewById<TextView>(R.id.difficulty_module1)
            val popularityTextView = moduleCard1.findViewById<TextView>(R.id.popularity_module1)
            val ratingTextView = moduleCard1.findViewById<TextView>(R.id.rating_module1)

            //    call api for values
            //val mauhma = api.mathma


            val queue = Volley.newRequestQueue(this)

            if (index %2 ==0)
            {
                val url = "http://10.0.2.2:5051/module/15139"
                val request = JsonObjectRequest (Request.Method.GET,url,null,
                    Response.Listener { response ->
                        moduleTextView.text = response.get("name").toString()
                        disModuleTextView.text = response.get("description").toString()
                        difficultyTextView.text = response.get("difficultyName").toString()
                        popularityTextView.text = response.get("price").toString()
                        ratingTextView.text = response.get("rating").toString()
                    }
                    , Response.ErrorListener{ error ->
                        Toast.makeText(this,"User not found", Toast.LENGTH_SHORT).show()
                    }
                )
                queue.add(request)
            }
            else {
                val url = "http://10.0.2.2:5051/module/15140"
                val request = JsonObjectRequest (Request.Method.GET,url,null,
                    Response.Listener { response ->
                        moduleTextView.text = response.get("name").toString()
                        disModuleTextView.text = response.get("description").toString()
                        difficultyTextView.text = response.get("difficultyName").toString()
                        popularityTextView.text = response.get("price").toString()
                        ratingTextView.text = response.get("rating").toString()
                    }
                    , Response.ErrorListener{ error ->
                        Toast.makeText(this,"User not found", Toast.LENGTH_SHORT).show()
                    }
                )
                queue.add(request)
            }

            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.PARENT_ID
            )

            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.topToBottom = if (index == 0) R.id.Recommend else parentLayout.getChildAt(index - 1).id

            layoutParams.setMargins(10, 10, 10, 10)
            parentLayout.addView(moduleCard1, layoutParams)
        }
    }

    fun twoModuleList(){
        val parentLayout: ConstraintLayout = findViewById(R.id.LinearModules)
        parentLayout.removeAllViews()

        repeat(10) { index ->
            val moduleCard1 = layoutInflater.inflate(R.layout.module, null) as ConstraintLayout
            moduleCard1.id = View.generateViewId() // Δημιουργία μοναδικού αναγνωριστικού για κάθε κάρτα ενότητας

            // Εύρεση TextViews μέσα στο ModuleCard1
            val moduleTextView = moduleCard1.findViewById<TextView>(R.id.module1)
            val disModuleTextView = moduleCard1.findViewById<TextView>(R.id.Dismodule1)
            val difficultyTextView = moduleCard1.findViewById<TextView>(R.id.difficulty_module1)
            val popularityTextView = moduleCard1.findViewById<TextView>(R.id.popularity_module1)
            val ratingTextView = moduleCard1.findViewById<TextView>(R.id.rating_module1)

            //val module = JsonOb


            val queue = Volley.newRequestQueue(this)

            if (index %2 ==0)
            {
                val url = "http://10.0.2.2:5051/module/15139"
                val request = JsonObjectRequest (Request.Method.GET,url,null,
                    Response.Listener { response ->
                        moduleTextView.text = response.get("name").toString()
                        disModuleTextView.text = response.get("description").toString()
                        difficultyTextView.text = response.get("difficultyName").toString()
                        popularityTextView.text = response.get("price").toString()
                        ratingTextView.text = response.get("rating").toString()
                    }
                    , Response.ErrorListener{ error ->
                        Toast.makeText(this,"User not found", Toast.LENGTH_SHORT).show()
                    }
                )
                queue.add(request)
            }
            else {
                val url = "http://10.0.2.2:5051/module/15140"
                val request = JsonObjectRequest (Request.Method.GET,url,null,
                    Response.Listener { response ->
                        moduleTextView.text = response.get("name").toString()
                        disModuleTextView.text = response.get("description").toString()
                        difficultyTextView.text = response.get("difficultyName").toString()
                        popularityTextView.text = response.get("price").toString()
                        ratingTextView.text = response.get("rating").toString()
                    }
                    , Response.ErrorListener{ error ->
                        Toast.makeText(this,"User not found", Toast.LENGTH_SHORT).show()
                    }
                )
                queue.add(request)
            }
            // Request a string response from the provided URL.

            // Ορισμός κειμένου για τα TextViews

            // Προσθήκη περιορισμών για τη θέση
            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )


            if (index % 2 == 0) {
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.topToBottom = if (index == 0) R.id.RecommendBlock else parentLayout.getChildAt(index - 1).id
            } else {
                layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.topToBottom = if (index == 1) R.id.RecommendBlock else parentLayout.getChildAt(index - 2).id
            }

            layoutParams.setMargins(10, 30, 10, 10)
            parentLayout.addView(moduleCard1, layoutParams)

        }

    }

}