package com.example.adopse_app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.adopse_app.R
import org.json.JSONObject

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val searchBar = findViewById<EditText>(R.id.SearchBar).text.toString()
        performSearch(searchBar)
    }

    private fun performSearch(searchBar: String) {

        val parentLayout: ConstraintLayout = findViewById(R.id.LinearModules)
        parentLayout.removeAllViews()

        val url = "http://10.0.2.2:7014/module?id=$searchBar"
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val moduleId = response.getInt("id")

                if (moduleId == searchBar.toInt()) {
                    val moduleCard1 = layoutInflater.inflate(R.layout.module_long, null) as ConstraintLayout
                    moduleCard1.id = View.generateViewId()

                    val moduleTextView = moduleCard1.findViewById<TextView>(R.id.module1)
                    val difficultyTextView = moduleCard1.findViewById<TextView>(R.id.difficulty_module1)
                    val popularityTextView = moduleCard1.findViewById<TextView>(R.id.popularity_module1)
                    val ratingTextView = moduleCard1.findViewById<TextView>(R.id.rating_module1)

                    moduleTextView.text = response.getString("name")
                    difficultyTextView.text = response.getString("difficultyName")
                    popularityTextView.text = response.getInt("price").toString()
                    ratingTextView.text = response.getInt("rating").toString()

                    parentLayout.addView(moduleCard1)
                } else {
                    Toast.makeText(this, "No modules found with ID $searchBar", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

}

