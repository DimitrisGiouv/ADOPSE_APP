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

        // Εισαγωγή ακροατή γεγονότος στο EditText του search bar
        val searchEditText = findViewById<EditText>(R.id.SearchBar)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Καλέστε τη λειτουργία αναζήτησης με το νέο κείμενο που εισήχθη
                val searchTerm = searchEditText.text.toString()
                performSearchByCategory(searchTerm)
                true
            } else {
                false
            }
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

        if (searchTerm.isBlank()) {
            Toast.makeText(this, "Η αναζήτηση είναι κενή", Toast.LENGTH_SHORT).show()
            twoModuleList()
            return
        }

        val url = "http://10.0.2.2:5051/module/stack/1000/0/"  // Ολόκληρο URL για το αίτημα
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                var foundModules = false
                val modulesArray = response.getJSONArray("modules") // Ανάκτηση του πίνακα modules από την απόκριση JSON

                // Αρχικοποίηση του rowIndex και columnIndex
                var rowIndex = 0
                var columnIndex = 0

                for (i in 0 until modulesArray.length()) {
                    val moduleObject = modulesArray.getJSONObject(i) // Ανάκτηση του κάθε module από τον πίνακα modules
                    val subCategory = moduleObject.getInt("subCategoryId")
                    var subCategoryName = ""

                    // Έλεγχος αν ο όρος αναζήτησης ταιριάζει με το όνομα της υποκατηγορίας
                    when (subCategory) {
                        // Κατηγορία: Μηχανική
                        in listOf(3, 12, 19, 20, 21, 22, 37, 39, 44, 45, 51, 56, 60, 62, 66, 67, 86, 104, 112, 115, 121, 122, 125, 131, 132, 134, 136, 140, 144, 147) -> subCategoryName = "Μηχανική μηχανικη ΜΗΧΑΝΙΚΗ"
                        // Κατηγορία: Πληροφορική
                        in listOf(1, 6, 38, 92, 100, 103, 111, 114, 117, 118, 119, 124, 143, 153) -> subCategoryName = "Πληροφορική πληροφορικη ΠΛΗΡΟΦΟΡΙΚΗ"
                        // Κατηγορία: Διοίκηση επιχειρήσεων - Οικονομικά
                        in listOf(2, 5, 7, 8, 9, 13, 23, 24, 27, 29, 31, 41, 42, 49, 53, 58, 59, 69, 91, 98, 105, 120, 128, 130, 135, 137, 142, 150) -> subCategoryName = "Διοίκηση επιχειρήσεων - Οικονομικά ΔΙΟΙΚΗΣΗ ΕΠΙΧΙΡΗΣΕΩΝ ΟΙΚΟΝΟΜΙΚΑ διοικηση επιχειρησεων οικονομικα"
                        // Κατηγορία: Περιβάλλον και αειφορία
                        in listOf(4, 11, 16, 54, 61, 63, 65, 81, 83, 84, 85, 90, 93, 106, 107, 108, 109, 110, 126, 127, 149, 151, 154) -> subCategoryName = "Περιβάλλον και αειφορία περιβαλλον και αειφορια ΠΕΡΙΒΑΛΛΟΝ ΚΑΙ ΑΕΙΦΟΡΙΑ"
                        // Κατηγορία: Παραιατρικά
                        in listOf(10, 14, 32, 33, 36, 46, 48, 64, 68, 70, 71, 72, 75, 76, 77, 80, 97, 141) -> subCategoryName = "Παραιατρικά ΠΑΡΑΙΑΤΡΙΚΑ παραιατρικα"
                        // Κατηγορία: Τουρισμός και φιλοξενία
                        in listOf(26, 50, 55, 89, 139) -> subCategoryName = "Τουρισμός και φιλοξενία ΤΟΥΡΙΣΜΟΣ ΚΑΙ ΦΙΛΟΞΕΝΙΑ τουρισμος και φιλοξενια"
                        // Κατηγορία: Βιβλιοθηκονομία και συστήματα πληροφόρησης
                        in listOf(15, 18, 73, 74, 94, 145) -> subCategoryName = "Βιβλιοθηκονομία και συστήματα πληροφόρησης βιβλιοθηκονομια και συστηματα πληροφορησης ΒΙΒΛΙΟΘΗΚΟΝΟΜΙΑ ΚΑΙ ΣΥΣΤΗΜΑΤΑ ΠΛΗΡΟΦΟΡΗΣΗΣ"
                        // Κατηγορία: Εφοδιαστική αλυσίδα και διαχείριση παραγωγής
                        in listOf(17, 25, 35, 43, 57, 88, 95, 148, 155) -> subCategoryName = "Εφοδιαστική αλυσίδα και διαχείριση παραγωγής ΕΦΟΔΙΑΣΤΙΚΗ ΑΛΥΣΙΔΑ ΚΑΙ ΔΙΑΧΕΙΡΙΣΗ ΠΑΡΑΓΩΓΗΣ εφοδιαστικη αλυσιδα και διαχειριση παραγωγης"
                        // Κατηγορία: Σχεδίαση προϊόντων και αισθητική
                        in listOf(28, 30, 40, 99, 101) -> subCategoryName = "Σχεδίαση προϊόντων και αισθητική σχεδιαση προιοντων και αισθητικη ΣΧΕΔΙΑΣΗ ΠΡΟΪΟΝΤΩΝ ΚΑΙ ΑΙΣΘΗΤΙΚΗ"
                        // Κατηγορία: Παιδαγωγικά
                        in listOf(34, 47, 79, 102, 113, 129, 138) -> subCategoryName = "Παιδαγωγικά παιδαγωγικα ΠΑΙΔΑΓΩΓΙΚΑ"
                        // Κατηγορία: Νομική - θεωρητικά
                        in listOf(52, 78, 82, 87, 116, 123) -> subCategoryName = "Νομική - θεωρητικά νομικη - θεωρητικα"
                        // Κατηγορία: θετικές επιστήμες
                        in listOf(96, 133, 146) -> subCategoryName = "θετικές επιστήμες θετικες επιστημες ΘΕΤΙΚΕΣ ΕΠΙΣΤΗΜΕΣ"
                        else -> subCategoryName = "" // Εκχώρηση κενού string στην περίπτωση που η υποκατηγορία δεν ανήκει σε καμία από τις παραπάνω κατηγορίες
                    }



                    // Έλεγχος αν ο όρος αναζήτησης ταιριάζει με το όνομα της υποκατηγορίας
                    if (!subCategoryName.isNullOrEmpty() && subCategoryName.contains(searchTerm, ignoreCase = true)) {
                        val moduleName = moduleObject.getString("name")
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

                        // Ορισμός του moduleCard στο grid layout
                        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                        layoutParams.topToBottom = if (columnIndex == 0 && rowIndex == 0) R.id.RecommendBlock else parentLayout.getChildAt(parentLayout.childCount - 1).id

                        // Προσθήκη του moduleCard στο parentLayout
                        parentLayout.addView(moduleCard, layoutParams)

                        // Αύξηση του columnIndex
                        columnIndex++

                        // Έλεγχος για να δούμε αν πρέπει να αυξήσουμε το rowIndex και να επαναφέρουμε το columnIndex στο 0
                        if (columnIndex == 2) {
                            columnIndex = 0
                            rowIndex++
                        }

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