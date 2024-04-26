package com.example.adopse_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var page = 0
    private val pageSize = 10 // Μέγεθος σελίδας

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

        var navigationCode = NavigationBar()
        navigationCode.NavigationCode(this)

        var viewActivity = BuildModules()
        viewActivity.toggleModuleList(this)


        val gridModules = findViewById<ImageButton>(R.id.gridViewButton)
        gridModules.setOnClickListener {
            if (viewActivity.isSingleView)
                gridModules.setBackgroundResource(R.drawable.button_background_twolist)
            else
                gridModules.setBackgroundResource(R.drawable.button_background_singlelist)
            viewActivity.onToggleViewButtonClick()
            viewActivity.toggleModuleList(this)
        }
    }

}

/*  val searchEditText = findViewById<EditText>(R.id.SearchBar)
     // Εισαγωγή ακροατή γεγονότος στο EditText του search bar
     val startIndex = 18000
     val endIndex = 0
     searchEditText.setOnEditorActionListener { _, actionId, _ ->
         if (actionId == EditorInfo.IME_ACTION_DONE) {
             // Καλέστε τη λειτουργία αναζήτησης με το νέο κείμενο που εισήχθη
             val searchTerm = searchEditText.text.toString()
             performSearchByCategory(searchTerm,startIndex,endIndex)
             true
         } else {
             false
         }
     }

     // Προσθήκη λειτουργικότητας στο κουμπί "Φόρτωση Περισσότερων"
     val loadMoreButton: Button = findViewById(R.id.loadMoreButton)
     loadMoreButton.setOnClickListener {
         val parentLayout: ConstraintLayout = findViewById(R.id.LinearModules)
         var startIndex: Int  // Υπολογισμός του startIndex βάσει του αριθμού των προηγούμενων αποτελεσμάτων
         startIndex = 100
        performSearchByCategory(searchEditText.toString(), startIndex,endIndex)
         endIndex+10
     }*/

/*
    fun performSearchByCategory(searchTerm: String, startIndex: Int, endIndex: Int) {
        val parentLayout: ConstraintLayout = findViewById(R.id.LinearModules)
        parentLayout.removeAllViews()

        if (searchTerm.isBlank()) {
            Toast.makeText(this, "Η αναζήτηση είναι κενή", Toast.LENGTH_SHORT).show()
            ViewActivity().toggleModuleList()
            return
        }

        val url = "http://10.0.2.2:5051/module/stack/${startIndex}/${endIndex}/"  // Ολόκληρο URL για το αίτημα
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                var foundModules = false
                val modulesArray = response.getJSONArray("modules") // Ανάκτηση του πίνακα modules από την απόκριση JSON

                // Αρχικοποίηση του rowIndex και columnIndex
                var rowIndex = 0
                var columnIndex = 0

                val MAX_RESULTS_DISPLAYED = 10 // Μέγιστος αριθμός αποτελεσμάτων που εμφανίζονται αρχικά
                val resultsPerPage = 5 // Αριθμός αποτελεσμάτων ανά σελίδα

                for (i in 0 until modulesArray.length()) {
                    if (parentLayout.childCount >= MAX_RESULTS_DISPLAYED) {
                        // Εάν υπερβαίνει τον μέγιστο αριθμό, εμφανίζουμε το κουμπί "Φόρτωση Περισσότερων"
                        findViewById<Button>(R.id.loadMoreButton).visibility = View.VISIBLE
                        break // Βγαίνουμε από την επανάληψη γιατί δεν χρειάζεται να επεξεργαστούμε περισσότερα αποτελέσματα
                    }
                    val moduleObject = modulesArray.getJSONObject(i) // Ανάκτηση του κάθε module από τον πίνακα modules
                    val subCategory = moduleObject.getInt("subCategoryId")
                    var subCategoryName: String

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
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
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
                    ViewActivity().toggleModuleList()
                    findViewById<Button>(R.id.loadMoreButton).visibility = View.INVISIBLE;
                }
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }
*/
