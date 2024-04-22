package com.example.adopse_app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
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
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Search bar να γινει μια παραπανω υλοποιηση
        val searchBar = findViewById<EditText>(R.id.SearchBar)

        val logo = findViewById<Button>(R.id.searchButton)
        logo.setOnClickListener{
            performSearchByCategory(searchBar.text.toString())
        }



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
        val loginPage = findViewById<ImageButton>(R.id.User)
        loginPage.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

/*        val profilePage =findViewById<ImageButton>(R.id.User)
        profilePage.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
         }*/

        val gridModules = findViewById<ImageButton>(R.id.gridViewButton)
        val listModules = findViewById<ImageButton>(R.id.listViewButton)
        // Κωδικας για την αλλαγη της λιστας απο δυο σε μια και αντιστροφα

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
            //val disModuleTextView = moduleCard1.findViewById<TextView>(R.id.Dismodule1)
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
                        //disModuleTextView.text = response.get("description").toString()
                        difficultyTextView.text = response.get("difficultyName").toString()
                        popularityTextView.text = response.get("price").toString()
                        ratingTextView.text = response.get("rating").toString()
                    }
                    ,{ error ->
                        Toast.makeText(this,"User not found", Toast.LENGTH_SHORT).show()
                    }
                )
                queue.add(request)
            }
            else {
                val url = "http://10.0.2.2:5051/module/"+(15139+index)
                val request = JsonObjectRequest (Request.Method.GET,url,null,
                    { response ->
                        moduleTextView.text = response.get("name").toString()
                        //disModuleTextView.text = response.get("description").toString()
                        difficultyTextView.text = response.get("difficultyName").toString()
                        popularityTextView.text = response.get("price").toString()
                        ratingTextView.text = response.get("rating").toString()
                    },

                    { error ->
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
            //val disModuleTextView = moduleCard1.findViewById<TextView>(R.id.Dismodule1)
            val difficultyTextView = moduleCard1.findViewById<TextView>(R.id.difficulty_module1)
            val popularityTextView = moduleCard1.findViewById<TextView>(R.id.popularity_module1)
            val ratingTextView = moduleCard1.findViewById<TextView>(R.id.rating_module1)

            //val module = JsonOb


            val queue = Volley.newRequestQueue(this)

            if (index %2 ==0)
            {
                val url = "http://10.0.2.2:5051/module/"+(15139+index)
                val request = JsonObjectRequest (Request.Method.GET,url,null,
                    { response ->
                        moduleTextView.text = response.get("name").toString()
                        //disModuleTextView.text = response.get("description").toString()
                        difficultyTextView.text = response.get("difficultyName").toString()
                        popularityTextView.text = response.get("price").toString()
                        ratingTextView.text = response.get("rating").toString()
                    }
                    ,{ error ->
                        Toast.makeText(this,"User not found", Toast.LENGTH_SHORT).show()
                    }
                )
                queue.add(request)
            }
            else {
                val url = "http://10.0.2.2:5051/module/"+(15139+index)
                val request = JsonObjectRequest (Request.Method.GET,url,null,
                    { response ->
                        moduleTextView.text = response.get("name").toString()
                        //disModuleTextView.text = response.get("description").toString()
                        difficultyTextView.text = response.get("difficultyName").toString()
                        popularityTextView.text = response.get("price").toString()
                        ratingTextView.text = response.get("rating").toString()
                    }
                    ,{ error ->
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

    fun performSearchByCategory(searchTerm: String) {
        val parentLayout: ConstraintLayout = findViewById(R.id.LinearModules)
        parentLayout.removeAllViews()

        val url = "http://10.0.2.2:5051/module/stack/18000/0/"  // Ολόκληρο URL για το αίτημα
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                var foundModules = false
                val modulesArray = response.getJSONArray("modules") // Ανάκτηση του πίνακα modules από την απόκριση JSON

                var prevModuleId = ConstraintLayout.LayoutParams.PARENT_ID

                for (i in 0 until modulesArray.length()) {
                    val moduleObject = modulesArray.getJSONObject(i) // Ανάκτηση του κάθε module από τον πίνακα modules
                    val subCategory = moduleObject.getInt("subCategoryId")
                    var subCategoryName = ""

                    // Αντιστοίχιση του subCategoryId με το όνομα της κατηγορίας
                    when (subCategory) {
                        1 -> subCategoryName = "ΜηχανικήμηχανικηΜΗΧΑΝΙΚΗ"
                        2 -> subCategoryName = "Διοίκηση επιχειρήσεων - Οικονομικά"
                        3 -> subCategoryName = "Πληροφορική"
                        4 -> subCategoryName = "Περιβάλλον και αειφορία"
                        5 -> subCategoryName = "Παραιατρικά"
                        6 -> subCategoryName = "Τουρισμός και φιλοξενία"
                        7 -> subCategoryName = "Βιβλιοθηκονομία και συστήματα πληροφόρησης"
                        8 -> subCategoryName = "Εφοδιαστική αλυσίδα και διαχείριση παραγωγής"
                        9 -> subCategoryName = "Σχεδίαση προϊόντων και αισθητική"
                        10 -> subCategoryName = "Παιδαγωγικά"
                        11 -> subCategoryName = "Νομική - θεωρητικά"
                        12 -> subCategoryName = "θετικές επιστήμες"
                        else -> subCategoryName = "Unknown"
                    }

                    // Έλεγχος αν ο όρος αναζήτησης ταιριάζει με το όνομα της υποκατηγορίας
                    if (subCategoryName.contains(searchTerm, ignoreCase = true)) {
                        val moduleName = moduleObject.getString("name")
                        //val description = moduleObject.getString("description")
                        val created = moduleObject.getString("created")

                        val moduleCard = layoutInflater.inflate(R.layout.module_long, null) as ConstraintLayout
                        moduleCard.id = View.generateViewId()

                        val moduleTextView = moduleCard.findViewById<TextView>(R.id.module1)
                        val difficultyTextView = moduleCard.findViewById<TextView>(R.id.difficulty_module1)
                        val popularityTextView = moduleCard.findViewById<TextView>(R.id.popularity_module1)
                        val ratingTextView = moduleCard.findViewById<TextView>(R.id.rating_module1)

                        moduleTextView.text = moduleName
                        difficultyTextView.text = moduleObject.getString("difficultyName")
                        popularityTextView.text = moduleObject.getInt("price").toString()
                        ratingTextView.text = moduleObject.getInt("rating").toString()

                        val layoutParams = ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )

                        layoutParams.setMargins(10, 30, 10, 10)

                        if (prevModuleId != ConstraintLayout.LayoutParams.PARENT_ID) {
                            layoutParams.topToBottom = prevModuleId
                            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                        }

                        parentLayout.addView(moduleCard, layoutParams)
                        prevModuleId = moduleCard.id

                        foundModules = true
                    }
                }
                if (!foundModules) {
                    Toast.makeText(this, "Δεν υπάρχουν μαθήματα στην κατηγορία που αναζητήσατε!", Toast.LENGTH_SHORT).show()
                    twoModuleList()
                }
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

}