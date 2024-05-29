package com.example.adopse_app

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.TimeoutError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class Search {
    private var currentPage = 0

    fun performSearch( activity: AppCompatActivity, searchBar: String, isModuleActivity: Boolean = false, isSingleView: Boolean, filters: MutableMap<String, String>? = null) {
        currentPage = 0
        if (searchBar.isBlank()) {
            Toast.makeText(activity, "Το πεδίο αναζήτησης είναι κενό", Toast.LENGTH_SHORT).show()
            val loadMoreButton = activity.findViewById<Button>(R.id.loadMoreButton)
            loadMoreButton?.let {
                it.visibility =  View.INVISIBLE
            }
            val build = BuildModules()
            build.toggleModuleList(activity)
        } else {
            requestModules(activity, searchBar, isModuleActivity, isSingleView, filters)
        }
    }

    private fun requestModules(activity: AppCompatActivity, searchBar: String, isModuleActivity: Boolean, isSingleView: Boolean, filters: Map<String, String>?) {
        val parentLayout: ConstraintLayout = activity.findViewById(R.id.LinearModules)
        parentLayout.removeAllViews()

        val url = if (filters.isNullOrEmpty()) {
            "http://10.0.2.2:5051/Module/filtered/10/$currentPage/?SearchQuery=$searchBar"
        } else {
            "http://10.0.2.2:5051/Module/filtered/10/$currentPage?" +
                    "ModuleTypeId=${filters["ModuleTypeId"]}&" +
                    "DifficultyId=${filters["DifficultyId"]}&" +
                    "Rating=${filters["Rating"]}&" +
                    "SearchQuery=$searchBar"
        }

        val queue = Volley.newRequestQueue(activity)

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val modulesArray = response.getJSONArray("modules")
                if (modulesArray.length() > 0) {
                    showResults(activity, modulesArray, isSingleView, isModuleActivity)
                } else {
                    Toast.makeText(activity, "Δεν βρέθηκαν άλλα μαθήματα που να ταιριάζουν με τον όρο αναζήτησης: $searchBar", Toast.LENGTH_SHORT).show()
                    val loadMoreButton = activity.findViewById<Button>(R.id.loadMoreButton)
                    loadMoreButton?.let {
                        it.visibility =  View.INVISIBLE
                    }
                    val build = BuildModules()
                    build.toggleModuleList(activity)
                }
            },
            { error ->
                Toast.makeText(activity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }
    fun filteredModulesPerPage(activity: AppCompatActivity, numberOfPage: Int, isSingleView: Boolean, filters: Map<String, String>) {
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
                val modulesArray = response.getJSONArray("modules")
                if (modulesArray.length() > 0) {
                    showResults(activity, modulesArray, isSingleView, true)
                } else {
                    Toast.makeText(activity, "Δεν βρέθηκαν άλλα μαθήματα ", Toast.LENGTH_SHORT).show()
                    val build = BuildModules()
                    build.toggleModuleList(activity)
                }
            },
            { error ->
                if (error is NoConnectionError || error is TimeoutError) {
                    Toast.makeText(activity, "Δεν υπάρχει σύνδεση στο διαδίκτυο ή ο διακομιστής δεν είναι διαθέσιμος", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Αποτυχία ανάκτησης δεδομένων μαθημάτων", Toast.LENGTH_SHORT).show()
                }
            }
        )
        Volley.newRequestQueue(activity).add(request)
    }


    fun showResults(activity: AppCompatActivity, modulesArray: JSONArray, isSingleView: Boolean, isModuleActivity: Boolean) {
        val parentLayout: ConstraintLayout = activity.findViewById(R.id.LinearModules)

        for (i in 0 until modulesArray.length()) {
            val module = modulesArray.getJSONObject(i)
            val moduleCard = activity.layoutInflater.inflate(if (isSingleView) R.layout.module_long else R.layout.module, null) as ConstraintLayout
            moduleCard.id = View.generateViewId()

            val moduleTextView = moduleCard.findViewById<TextView>(R.id.module1)
            val difficultyTextView = moduleCard.findViewById<TextView>(R.id.difficulty_module1)
            val descriptorTextView = moduleCard.findViewById<TextView>(R.id.Dismodule1)
            val popularityTextView = moduleCard.findViewById<TextView>(R.id.popularity_module1)
            val ratingTextView = moduleCard.findViewById<TextView>(R.id.rating_module1)
            val priceTextView = moduleCard.findViewById<TextView>(R.id.textView)

            moduleTextView.text = module.getString("name")
            difficultyTextView.text = module.getString("difficultyName")
            val fullDescription = module.getString("description")
            descriptorTextView.text = module.get("description").toString()
            popularityTextView.text = module.getInt("price").toString()
            ratingTextView.text = module.getInt("rating").toString()
            priceTextView.text = module.getInt("price").toString()

            val sizeLimitation = module.getString("description")
            val truncatedDescription = if (sizeLimitation.length > 30) {
                sizeLimitation.substring(0, 30) + "..."
            } else {
                sizeLimitation
            }
            descriptorTextView.text = truncatedDescription

            moduleCard.setOnClickListener {
                val moduleName = moduleTextView.text.toString()
                val moduleDifficulty = difficultyTextView.text.toString()
                val modulePopularity = popularityTextView.text.toString()
                val moduleRating = ratingTextView.text.toString()
                val modulePrice = priceTextView.text.toString()

                val intent = Intent(activity, ModuleProfileActivity::class.java).apply {
                    putExtra("moduleName", moduleName)
                    putExtra("moduleDescription", fullDescription)
                    putExtra("moduleDifficulty", moduleDifficulty)
                    putExtra("modulePopularity", modulePopularity)
                    putExtra("moduleRating", moduleRating)
                    putExtra("modulePrice", modulePrice)
                }
                activity.startActivity(intent)
            }

            val moduleCardHeight = 550 // Ύψος του moduleCard
            val layoutParams = ConstraintLayout.LayoutParams(
                if (isSingleView) ConstraintLayout.LayoutParams.MATCH_PARENT else ConstraintLayout.LayoutParams.WRAP_CONTENT,
                moduleCardHeight
            )
            layoutParams.setMargins(10, 10, 10, 10)

            if (isSingleView) {
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.topToBottom = if (i == 0) R.id.RecommendBlock else parentLayout.getChildAt(i - 1).id
            } else {
                if (i % 2 == 0) {
                    layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    layoutParams.topToBottom = if (i == 0) R.id.RecommendBlock else parentLayout.getChildAt(i - 1).id
                } else {
                    layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    layoutParams.topToBottom = if (i == 1) R.id.RecommendBlock else parentLayout.getChildAt(i - 2).id
                }
            }

            parentLayout.addView(moduleCard, layoutParams)
        }

        if (!isModuleActivity && modulesArray.length() > 0) {
            val loadMoreButton = activity.findViewById<Button>(R.id.loadMoreButton)
            loadMoreButton?.let {
                it.visibility =  View.VISIBLE
            }
        }
    }


    fun nextPage(activity: AppCompatActivity, searchTerm: String, isModuleActivity: Boolean = false, isSingleView: Boolean, filters: Map<String, String>? = null) {
        currentPage += 10
        requestModules(activity, searchTerm, isModuleActivity, isSingleView, filters)
    }

    fun previousPage(activity: AppCompatActivity, searchTerm: String, isModuleActivity: Boolean = false, isSingleView: Boolean, filters: Map<String, String>? = null) {
        if (currentPage > 0) {
            currentPage -= 10
            requestModules(activity, searchTerm, isModuleActivity, isSingleView, filters)
        }
    }

}