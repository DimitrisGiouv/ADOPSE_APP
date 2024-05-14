package com.example.adopse_app

import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class Search {
    private var currentPage = 0


    fun performSearch(activity: AppCompatActivity, searchBar: String) {
        currentPage = 0
        if (searchBar.isBlank()) {
            Toast.makeText(activity, "Το πεδίο αναζήτησης είναι κενό", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        } else {
            requestModules(activity, searchBar)
        }
    }


    private fun requestModules(activity: AppCompatActivity, searchBar: String) {
        val parentLayout: ConstraintLayout = activity.findViewById(R.id.LinearModules)
        parentLayout.removeAllViews()

        val url = "http://10.0.2.2:5051/Module/filtered/10/$currentPage/?SearchQuery=$searchBar"
        val queue = Volley.newRequestQueue(activity)

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val modulesArray = response.getJSONArray("modules")
                if (modulesArray.length() > 0) {
                    showResults(activity, modulesArray, false)
                } else {
                    Toast.makeText(activity, "No modules found with ID $searchBar", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(activity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    private fun showResults(activity: AppCompatActivity, modulesArray: JSONArray, isSingleView: Boolean) {
        val parentLayout: ConstraintLayout = activity.findViewById(R.id.LinearModules)

        for (i in 0 until modulesArray.length()) {
            val module = modulesArray.getJSONObject(i)
            val moduleCard = activity.layoutInflater.inflate(
                if (isSingleView) R.layout.module_long else R.layout.module,
                null
            ) as ConstraintLayout
            moduleCard.id = View.generateViewId()

            val moduleTextView = moduleCard.findViewById<TextView>(R.id.module1)
            val difficultyTextView = moduleCard.findViewById<TextView>(R.id.difficulty_module1)
            var descriptorTextView = moduleCard.findViewById<TextView>(R.id.Dismodule1)
            val popularityTextView = moduleCard.findViewById<TextView>(R.id.popularity_module1)
            val ratingTextView = moduleCard.findViewById<TextView>(R.id.rating_module1)

            moduleTextView.text = module.getString("name")
            difficultyTextView.text = module.getString("difficultyName")
            val fullDescription = module.getString("description")
            descriptorTextView.text = module.get("description").toString()
            popularityTextView.text = module.getInt("price").toString()
            ratingTextView.text = module.getInt("rating").toString()
            // Περιορίζουμε την περιγραφή σε 30 χαρακτήρες και προσθέτουμε "..." στο τέλος αν είναι απαραίτητο
            val sizeLimitation = module.getString("description")
            val truncatedDescription = if (sizeLimitation.length > 30) {
                sizeLimitation.substring(0, 30) + "..."
            } else {
                sizeLimitation
            }

            descriptorTextView.text = truncatedDescription

            moduleCard?.setOnClickListener {
                val moduleName = moduleTextView?.text?.toString()
                val moduleDifficulty = difficultyTextView?.text?.toString()
                val modulePopularity = popularityTextView?.text?.toString()
                val moduleRating = ratingTextView?.text?.toString()

                val intent = Intent(activity, ModuleProfileActivity::class.java).apply {
                    putExtra("moduleName", moduleName)
                    putExtra("moduleDescription", fullDescription)
                    putExtra("moduleDifficulty", moduleDifficulty)
                    putExtra("modulePopularity", modulePopularity)
                    putExtra("moduleRating", moduleRating)
                }
                activity.startActivity(intent)
            }

            val moduleCardHeight = 550 // Ύψος του moduleCard

            if (isSingleView) {
                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    moduleCardHeight
                )
                layoutParams.setMargins(10, 10, 10, 10)
                parentLayout.addView(moduleCard, layoutParams)
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.topToBottom =
                    if (i == 0) R.id.RecommendBlock else parentLayout.getChildAt(i - 1).id
            } else {
                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    moduleCardHeight
                )
                layoutParams.setMargins(10, 10, 10, 10)
                parentLayout.addView(moduleCard, layoutParams)
                if (i % 2 == 0) {
                    layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    layoutParams.topToBottom =
                        if (i == 0) R.id.RecommendBlock else parentLayout.getChildAt(i - 1).id
                } else {
                    layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    layoutParams.topToBottom =
                        if (i == 1) R.id.RecommendBlock else parentLayout.getChildAt(i - 2).id
                }
            }
        }
    }

    fun nextPage(activity: AppCompatActivity, searchTerm: String) {
        currentPage+=10
        requestModules(activity, searchTerm)
    }


    fun previousPage(activity: AppCompatActivity, searchTerm: String) {
        if (currentPage > 0) {
            currentPage-=10
            requestModules(activity, searchTerm)
        }
    }
}
