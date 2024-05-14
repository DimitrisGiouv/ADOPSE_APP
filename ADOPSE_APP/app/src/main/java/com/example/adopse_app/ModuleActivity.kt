package com.example.adopse_app

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ModuleActivity: AppCompatActivity(){
    private var currentPage = 0;

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

        val navigationCode = NavigationBar()
        navigationCode.NavigationCode(this)

        var search = Search()

        val viewActivity = BuildModules()
        viewActivity.modulesPerPage(this,currentPage)

        val gridModules = findViewById<ImageButton>(R.id.gridViewButton)
        gridModules.setOnClickListener {
            if (viewActivity.isSingleView)
                gridModules.setBackgroundResource(R.drawable.button_background_twolist)
            else
                gridModules.setBackgroundResource(R.drawable.button_background_singlelist)
            viewActivity.onToggleViewButtonClick()
            viewActivity.modulesPerPage(this,currentPage)
        }

        val searchEditText = findViewById<EditText>(R.id.searchBarModule)
        searchEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Καλέστε τη λειτουργία αναζήτησης με το νέο κείμενο που εισήχθη
                val searchTerm = searchEditText.text.toString()
                search.performSearch(this, searchTerm)
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
                search.nextPage(this, searchTerm)
            }
        }

        val previousPage = findViewById<ImageButton>(R.id.previousPage)
        previousPage.setOnClickListener {
            val searchTerm = searchEditText.text.toString()
            if (searchTerm.isEmpty() || currentPage == 0) {
                if (currentPage != 0) currentPage -= 1
                viewActivity.modulesPerPage(this, currentPage)
            } else {
                search.previousPage(this, searchTerm)
            }
        }

    }
}

