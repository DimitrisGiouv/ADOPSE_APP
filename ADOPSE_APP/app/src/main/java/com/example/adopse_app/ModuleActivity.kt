package com.example.adopse_app

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class ModuleActivity: AppCompatActivity(){
    var isSingleView = true
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_module)

        // Καθορισμός των περιοχών των system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.modulepage)) { v, insets ->
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

        val logobutton = findViewById<ImageButton>(R.id.logo_button)
        logobutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Κουμπί που μεταφέρει στην οθόνη εισόδου
        val loginPage = findViewById<ImageButton>(R.id.User)
        loginPage.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val searchEditText = findViewById<EditText>(R.id.searchBarModule)
        // Εισαγωγή ακροατή γεγονότος στο EditText του search bar
        val startIndex = 18000
        val endIndex = 0
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Καλέστε τη λειτουργία αναζήτησης με το νέο κείμενο που εισήχθη
                val searchTerm = searchEditText.text.toString()
                true
            } else {
                false
            }
        }

        val gridModules = findViewById<ImageButton>(R.id.gridViewButton)
        gridModules.setOnClickListener {
            if(isSingleView)
                gridModules.setBackgroundResource(R.drawable.button_background_twolist)
            else
                gridModules.setBackgroundResource(R.drawable.button_background_singlelist)
            toggleModuleList()
            onToggleViewButtonClick()
        }

        // Οταν ανοιγη το app τοτε θα φορτωθει η μια λιστα
        toggleModuleList()

    }
    fun toggleModuleList() {
        val parentLayout: ConstraintLayout = findViewById(R.id.LinearModules)
        parentLayout.removeAllViews()

        repeat(30) { index ->
            val moduleCard = if (isSingleView) {
                layoutInflater.inflate(R.layout.module_long, null) as ConstraintLayout
            } else {
                layoutInflater.inflate(R.layout.module, null) as ConstraintLayout
            }
            moduleCard.id = View.generateViewId()

            val moduleTextView = moduleCard.findViewById<TextView>(R.id.module1)
            var descriptorTextView = moduleCard.findViewById<TextView>(R.id.Dismodule1)
            val difficultyTextView = moduleCard.findViewById<TextView>(R.id.difficulty_module1)
            val popularityTextView = moduleCard.findViewById<TextView>(R.id.popularity_module1)
            val ratingTextView = moduleCard.findViewById<TextView>(R.id.rating_module1)

            val queue = Volley.newRequestQueue(this)

            val url = "http://10.0.2.2:5051/module/" + (15139 + index)
            val request = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    moduleTextView.text = response.get("name").toString()
                    descriptorTextView.text = response.get("description").toString()
                    difficultyTextView.text = response.get("difficultyName").toString()
                    popularityTextView.text = response.get("price").toString()
                    ratingTextView.text = response.get("rating").toString()

                    val sizeLimitation = response.get("description").toString()

                    // Truncate the description text if it exceeds 30 characters
                    val truncatedDescription = if (sizeLimitation.length > 30) {
                        sizeLimitation.substring(0, 30) + "..." // Add ellipsis if truncated
                    } else {
                        sizeLimitation // Otherwise, use the original text
                    }

                    descriptorTextView.text = truncatedDescription
                },
                { error ->
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                }
            )
            queue.add(request)



            val moduleCardHeight =550 // Ύψος του moduleCard

            if (isSingleView) {
                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    moduleCardHeight
                )
                layoutParams.setMargins(10, 10, 10, 10)
                parentLayout.addView(moduleCard, layoutParams)
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.topToBottom = if (index == 0) R.id.RecommendBlock else parentLayout.getChildAt(index - 1).id
            } else {
                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    moduleCardHeight
                )
                layoutParams.setMargins(10, 10, 10, 10)
                parentLayout.addView(moduleCard, layoutParams)
                if (index % 2 == 0) {
                    layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    layoutParams.topToBottom = if (index == 0) R.id.RecommendBlock else parentLayout.getChildAt(index - 1).id
                } else {
                    layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    layoutParams.topToBottom = if (index == 1) R.id.RecommendBlock else parentLayout.getChildAt(index - 2).id
                }
            }
        }
    }

    fun onToggleViewButtonClick() {
        isSingleView = !isSingleView // Toggle the boolean flag
        toggleModuleList() // Refresh the view
    }

}

