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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_module)
        val searchEditText = findViewById<EditText>(R.id.SearchBarModule)
        // Εισαγωγή ακροατή γεγονότος στο EditText του search bar
        val startIndex = 18000
        val endIndex = 0
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Καλέστε τη λειτουργία αναζήτησης με το νέο κείμενο που εισήχθη
                val searchTerm = searchEditText.text.toString()
                MainActivity.performSearchByCategory(searchTerm,startIndex,endIndex)
                true
            } else {
                false
            }
        }
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

        // Κουμπί που μεταφέρει στην οθόνη εισόδου
        val loginPage = findViewById<ImageButton>(R.id.User)
        loginPage.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Καλεί την συνάρτηση twoModuleList() για να δημιουργήσει την αρχική λίστα με δύο μονάδες
        twoModuleList()

        // Κωδικας για την αλλαγη της λιστας απο δυο σε μια και αντιστροφα
        val gridModules = findViewById<ImageButton>(R.id.gridViewButton)
        val listModules = findViewById<ImageButton>(R.id.listViewButton)

        gridModules.setOnClickListener {
            gridModules.isEnabled = false// Απενεργοποίηση του κουμπιού
            listModules.isEnabled = true// Ενεργοποίηση του κουμπιού
            twoModuleList()
            gridModules.setBackgroundResource(R.drawable.module_button_twolist_active)
            listModules.setBackgroundResource(R.drawable.module_button_singlelist)
        }

        listModules.setOnClickListener {
            gridModules.isEnabled = true// Ενεργοποίηση του κουμπιού
            listModules.isEnabled = false// Απενεργοποίηση του κουμπιού
            singleModuleList()
            listModules.setBackgroundResource(R.drawable.module_button_singlelist_active)
            gridModules.setBackgroundResource(R.drawable.module_button_twolist)
        }
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

                val queue = Volley.newRequestQueue(this)

                if (index %2 ==0)
                {
                    val url = "http://10.0.2.2:5051/module/"+(15139+index)
                    val request = JsonObjectRequest (Request.Method.GET,url,null,
                        { response ->
                            moduleTextView.text = response.get("name").toString()
                            disModuleTextView.text = response.get("description").toString()
                            difficultyTextView.text = response.get("difficultyName").toString()
                            popularityTextView.text = response.get("price").toString()
                            ratingTextView.text = response.get("rating").toString()
                        }
                        ,{ error ->
                            Toast.makeText(this,error.toString(), Toast.LENGTH_SHORT).show()
                        }
                    )
                    queue.add(request)
                }
                else {
                    val url = "http://10.0.2.2:5051/module/"+(15139+index)
                    val request = JsonObjectRequest (Request.Method.GET,url,null,
                        { response ->
                            moduleTextView.text = response.get("name").toString()
                            disModuleTextView.text = response.get("description").toString()
                            difficultyTextView.text = response.get("difficultyName").toString()
                            popularityTextView.text = response.get("price").toString()
                            ratingTextView.text = response.get("rating").toString()
                        }
                        , { error ->
                            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
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

    fun twoModuleList() {
        val parentLayout: ConstraintLayout = findViewById(R.id.LinearModules)
        parentLayout.removeAllViews()

        repeat(10) { index ->
            val moduleCard1 = layoutInflater.inflate(R.layout.module, null) as ConstraintLayout
            moduleCard1.id =
                View.generateViewId() // Δημιουργία μοναδικού αναγνωριστικού για κάθε κάρτα ενότητας

            // Εύρεση TextViews μέσα στο ModuleCard1
            val moduleTextView = moduleCard1.findViewById<TextView>(R.id.module1)
            val disModuleTextView = moduleCard1.findViewById<TextView>(R.id.Dismodule1)
            val difficultyTextView = moduleCard1.findViewById<TextView>(R.id.difficulty_module1)
            val popularityTextView = moduleCard1.findViewById<TextView>(R.id.popularity_module1)
            val ratingTextView = moduleCard1.findViewById<TextView>(R.id.rating_module1)

            // Ορισμός κειμένου για τα TextViews
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

            // Προσθήκη περιορισμών για τη θέση
            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            if (index % 2 == 0) {
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.topToBottom =
                    if (index == 0) R.id.RecommendBlock else parentLayout.getChildAt(index - 1).id
            } else {
                layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.topToBottom =
                    if (index == 1) R.id.RecommendBlock else parentLayout.getChildAt(index - 2).id
            }

            layoutParams.setMargins(10, 30, 10, 10)
            parentLayout.addView(moduleCard1, layoutParams)

        }
    }


}

private fun MainActivity.Companion.performSearchByCategory(searchTerm: String, startIndex: Int, endIndex: Int) {

}
