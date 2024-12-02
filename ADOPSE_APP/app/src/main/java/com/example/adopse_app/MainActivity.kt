package com.example.adopse_app

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var currentPage = 0
    private val viewActivity = BuildModules()

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

        val sharedPreferences = getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
        val isLogged = sharedPreferences.getBoolean("isLogged", false)

        println("The value of isLogged: $isLogged")
        var navigationBar = NavigationBar()
        navigationBar.NavigationCode(this)

        viewActivity.modulesPerPage(this,currentPage)

        var search = Search()


        val gridModules = findViewById<ImageButton>(R.id.gridViewButton)
        gridModules.setOnClickListener {
            if (viewActivity.isSingleView)
                gridModules.setBackgroundResource(R.drawable.button_background_twolist)
            else
                gridModules.setBackgroundResource(R.drawable.button_background_singlelist)
            viewActivity.onToggleViewButtonClick()
            viewActivity.modulesPerPage(this, currentPage)
        }

        val searchEditText = findViewById<EditText>(R.id.SearchBar)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Καλέστε τη λειτουργία αναζήτησης με το νέο κείμενο που εισήχθη
                val searchTerm = searchEditText.text.toString()

                search.performSearch(this, searchTerm, false, viewActivity.isSingleView)
                true
            } else {
                false
            }
        }
        val loadMoreButton = findViewById<Button>(R.id.loadMoreButton)
        loadMoreButton.setOnClickListener {
            val searchTerm = searchEditText.text.toString()
            if (searchTerm.isEmpty()) {
                currentPage += 1
                viewActivity.modulesPerPage(this, currentPage)
            } else {
                search.nextPage(this, searchTerm, false, viewActivity.isSingleView)
            }
        }
    }

}
