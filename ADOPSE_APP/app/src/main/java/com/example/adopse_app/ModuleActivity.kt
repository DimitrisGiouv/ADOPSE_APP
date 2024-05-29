package com.example.adopse_app

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ModuleActivity : AppCompatActivity() {
    private var currentPage = 0
    private var selectedType = 0
    private var selectedDifficulty = 0
    private var selectedRatings = 1

    private var filters: MutableMap<String, String> = mutableMapOf()
    private val viewActivity = BuildModules()

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

        val navigationCode = NavigationBar()
        navigationCode.NavigationCode(this)

        var search = Search()


        viewActivity.modulesPerPage(this, currentPage)

        val gridModules = findViewById<ImageButton>(R.id.gridViewButton)
        gridModules.setOnClickListener {
            if (viewActivity.isSingleView)
                gridModules.setBackgroundResource(R.drawable.button_background_twolist)
            else
                gridModules.setBackgroundResource(R.drawable.button_background_singlelist)
            viewActivity.onToggleViewButtonClick()
            viewActivity.modulesPerPage(this, currentPage)
        }

        val searchEditText = findViewById<EditText>(R.id.searchBarModule)
        searchEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Καλέστε τη λειτουργία αναζήτησης με το νέο κείμενο που εισήχθη
                val searchTerm = searchEditText.text.toString()
                search.performSearch(this, searchTerm, true, viewActivity.isSingleView, filters)
                true
            } else {
                false
            }
        }

        val nextPage = findViewById<ImageButton>(R.id.nextPage)
        nextPage.setOnClickListener {
            val searchTerm = searchEditText.text.toString()
            if (searchTerm.isEmpty()) {
                currentPage += 1
                if (filters.isEmpty()) {
                    viewActivity.modulesPerPage(this, currentPage)
                } else {
                    search.filteredModulesPerPage(this, currentPage, viewActivity.isSingleView, filters)
                }
            } else {
                search.nextPage(this, searchTerm, true,viewActivity.isSingleView, filters)
            }
        }

        val previousPage = findViewById<ImageButton>(R.id.previousPage)
        previousPage.setOnClickListener {
            val searchTerm = searchEditText.text.toString()
            if (searchTerm.isEmpty() || currentPage == 0 || filters.isEmpty()) {
                if (currentPage != 0) currentPage -= 1
                if (filters.isEmpty()) {
                    viewActivity.modulesPerPage(this, currentPage)
                } else {
                    search.filteredModulesPerPage(this, currentPage, viewActivity.isSingleView, filters)
                }
            } else {
                search.previousPage(this, searchTerm, true,viewActivity.isSingleView, filters)
            }
        }

        // Προσθήκη του click listener για το κουμπί φίλτρων
        val filterButton = findViewById<ImageButton>(R.id.filterButton)
        filterButton.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun showFilterDialog() {
        val dialog = Dialog(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_filters, null)

        dialog.setContentView(view)
        dialog.show()

        val typeRadioGroup = view.findViewById<RadioGroup>(R.id.type_radio_group)
        val difficultyRadioGroup = view.findViewById<RadioGroup>(R.id.difficulty_radio_group)
        val rating5CheckBox = view.findViewById<CheckBox>(R.id.rating_5)
        val rating4CheckBox = view.findViewById<CheckBox>(R.id.rating_4)
        val rating3CheckBox = view.findViewById<CheckBox>(R.id.rating_3)
        val rating2CheckBox = view.findViewById<CheckBox>(R.id.rating_2)
        val ratingAllCheckBox = view.findViewById<CheckBox>(R.id.rating_all)

        typeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedType = when (checkedId) {
                R.id.type_all -> 0
                R.id.type_lab -> 1
                R.id.type_theory -> 2
                R.id.type_mix -> 3
                else -> 0
            }
        }

        difficultyRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedDifficulty = when (checkedId) {
                R.id.difficulty_all -> 0
                R.id.difficulty_easy -> 1
                R.id.difficulty_medium -> 2
                R.id.difficulty_hard -> 3
                else -> 0
            }
        }

        rating5CheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedRatings = 5
        }
        rating4CheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedRatings = 4
        }
        rating3CheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedRatings = 3
        }
        rating2CheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedRatings = 2
        }

        val applyButton = view.findViewById<Button>(R.id.apply_filters_button)
        applyButton.setOnClickListener {
            dialog.dismiss()

            filters.clear()

            // Προσθήκη της τιμής του φίλτρου τύπου (type)
            filters["ModuleTypeId"] = when (typeRadioGroup.checkedRadioButtonId) {
                R.id.type_all -> ""
                R.id.type_lab -> "1"
                R.id.type_theory -> "2"
                R.id.type_mix -> "3"
                else -> ""
            }

            // Προσθήκη της τιμής του φίλτρου δυσκολίας (difficulty)
            filters["DifficultyId"] = when (difficultyRadioGroup.checkedRadioButtonId) {
                R.id.difficulty_all -> ""
                R.id.difficulty_easy -> "1"
                R.id.difficulty_medium -> "2"
                R.id.difficulty_hard -> "3"
                else -> ""
            }

            // Προσθήκη της τιμής του φίλτρου αξιολόγησης (rating)
            filters["Rating"] = when {
                rating5CheckBox.isChecked -> "5"
                rating4CheckBox.isChecked -> "4"
                rating3CheckBox.isChecked -> "3"
                rating2CheckBox.isChecked -> "2"
                ratingAllCheckBox.isChecked -> ""
                else -> ""
            }


        // Χρήση της `modulesPerPage` με τα φίλτρα
            val search = Search()
            search.filteredModulesPerPage(this@ModuleActivity, 0, viewActivity.isSingleView, filters)
        }

    }
}
