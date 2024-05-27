package com.example.adopse_app

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.SeekBar
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

class ModuleActivity : AppCompatActivity() {
    private var currentPage = 0
    private var priceRange = floatArrayOf(0f, 100f)
    private var selectedType = 0
    private var selectedDifficulty = 0
    private var selectedRatings = 1

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

        val viewActivity = BuildModules()
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
                search.performSearch(this, searchTerm, true)
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
                viewActivity.modulesPerPage(this, currentPage)
            } else {
                search.nextPage(this, searchTerm, true)
            }
        }

        val previousPage = findViewById<ImageButton>(R.id.previousPage)
        previousPage.setOnClickListener {
            val searchTerm = searchEditText.text.toString()
            if (searchTerm.isEmpty() || currentPage == 0) {
                if (currentPage != 0) currentPage -= 1
                viewActivity.modulesPerPage(this, currentPage)
            } else {
                search.previousPage(this, searchTerm, true)
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

        val minValueEditText = view.findViewById<EditText>(R.id.min_price)
        val maxValueEditText = view.findViewById<EditText>(R.id.max_price)
        val seekBar = view.findViewById<SeekBar>(R.id.price_seekbar)
        val typeRadioGroup = view.findViewById<RadioGroup>(R.id.type_radio_group)
        val difficultyRadioGroup = view.findViewById<RadioGroup>(R.id.difficulty_radio_group)
        val rating5CheckBox = view.findViewById<CheckBox>(R.id.rating_5)
        val rating4CheckBox = view.findViewById<CheckBox>(R.id.rating_4)
        val rating3CheckBox = view.findViewById<CheckBox>(R.id.rating_3)
        val rating2CheckBox = view.findViewById<CheckBox>(R.id.rating_2)
        val ratingAllCheckBox = view.findViewById<CheckBox>(R.id.rating_all)

        minValueEditText.setText(priceRange[0].toString())
        maxValueEditText.setText(priceRange[1].toString())
        seekBar.progress = ((priceRange[0] + priceRange[1]) / 2).toInt()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val minValue = (progress - seekBar!!.max / 2).coerceAtLeast(0)
                val maxValue = progress + seekBar.max / 2
                minValueEditText.setText(minValue.toString())
                maxValueEditText.setText(maxValue.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                priceRange[0] = minValueEditText.text.toString().toFloat()
                priceRange[1] = maxValueEditText.text.toString().toFloat()
            }
        })

        minValueEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val value = s?.toString()?.toFloatOrNull()
                value?.let {
                    if (it <= priceRange[1]) {
                        priceRange[0] = it
                        seekBar.progress = ((priceRange[0] + priceRange[1]) / 2).toInt()
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        maxValueEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val value = s?.toString()?.toFloatOrNull()
                value?.let {
                    if (it >= priceRange[0]) {
                        priceRange[1] = it
                        seekBar.progress = ((priceRange[0] + priceRange[1]) / 2).toInt()
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

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
        ratingAllCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedRatings = 1
        }

        val applyButton = view.findViewById<Button>(R.id.apply_filters_button)
        applyButton.setOnClickListener {
            dialog.dismiss()

            // Συγκέντρωση των επιλεγμένων φίλτρων
            val filters = mutableMapOf<String, String>()

            // Προσθήκη της τιμής του φίλτρου τύπου (type)
            filters["ModuleTypeId"] = when (typeRadioGroup.checkedRadioButtonId) {
                R.id.type_all -> "0"
                R.id.type_lab -> "1"
                R.id.type_theory -> "2"
                R.id.type_mix -> "3"
                else -> ""
            }

            // Προσθήκη της τιμής του φίλτρου δυσκολίας (difficulty)
            filters["DifficultyId"] = when (difficultyRadioGroup.checkedRadioButtonId) {
                R.id.difficulty_all -> "0"
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
                ratingAllCheckBox.isChecked -> "1"
                else -> ""
            }

            filteredModulesPerPage(this, 0, filters)
        }

    }

    private fun filteredModulesPerPage(activity: Activity, numberOfPage: Int, filters: Map<String, String>) {
        val parentLayout: ConstraintLayout = activity.findViewById(R.id.LinearModules)
        parentLayout.removeAllViews()

        val moduleOfPage = numberOfPage * 10

        val url = "http://10.0.2.2:5051/Module/filtered/10/$moduleOfPage?" +
                "ModuleTypeId=${filters["ModuleTypeId"]}&" +
                "DifficultyId=${filters["DifficultyId"]}&" +
                "Rating=${filters["Rating"]}"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val modules = response.getJSONArray("modules")
                val search = Search()
                search.showResults(this, modules, false, true)
            },
            { error ->

                if (error is NoConnectionError || error is TimeoutError) {
                    Toast.makeText(activity, "No internet connection or server unavailable", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Failed to retrieve module data", Toast.LENGTH_SHORT).show()
                }
            }
        )
        Volley.newRequestQueue(activity).add(request)
    }
}
