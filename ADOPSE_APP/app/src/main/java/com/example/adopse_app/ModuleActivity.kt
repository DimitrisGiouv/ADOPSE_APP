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
    private var currentPage = 0;
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

        val navigationCode = NavigationBar()
        navigationCode.NavigationCode(this)

        val viewActivity = BuildModules()
        viewActivity.modulesPerPage(this,currentPage)

        val nextPage = findViewById<ImageButton>(R.id.nextPage)
        nextPage.setOnClickListener {
            currentPage += 1;
            viewActivity.modulesPerPage(this,currentPage)
        }

        val previousPage = findViewById<ImageButton>(R.id.previousPage)
        previousPage.setOnClickListener {
            if (currentPage != 0){currentPage -= 1}else {}
            viewActivity.modulesPerPage(this,currentPage)
        }

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
                val search = Search()
                search.performSearch(this, searchTerm)
                true
            } else {
                false
            }
        }

    }
}

