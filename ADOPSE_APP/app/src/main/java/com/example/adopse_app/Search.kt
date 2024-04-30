package com.example.adopse_app

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
    fun performSearch(activity: AppCompatActivity, searchBar: String) {
        val parentLayout: ConstraintLayout = activity.findViewById(R.id.LinearModules)
        parentLayout.removeAllViews()

        val url = "http://10.0.2.2:5051/Module/filtered/100/0/?SearchQuery=$searchBar"
        val queue = Volley.newRequestQueue(activity)

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val modulesArray = response.getJSONArray("modules")
                if (modulesArray.length() > 0) {
                    showResults(activity, modulesArray)
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

    fun showResults(activity: AppCompatActivity, modulesArray: JSONArray) {
        val parentLayout: ConstraintLayout = activity.findViewById(R.id.LinearModules)
        var rowIndex = 0
        var columnIndex = 0

        for (i in 0 until modulesArray.length()) {
            val module = modulesArray.getJSONObject(i)
            val moduleCard1 = activity.layoutInflater.inflate(R.layout.module_long, null) as ConstraintLayout
            moduleCard1.id = View.generateViewId()

            val moduleTextView = moduleCard1.findViewById<TextView>(R.id.module1)
            val difficultyTextView = moduleCard1.findViewById<TextView>(R.id.difficulty_module1)
            val popularityTextView = moduleCard1.findViewById<TextView>(R.id.popularity_module1)
            val ratingTextView = moduleCard1.findViewById<TextView>(R.id.rating_module1)

            moduleTextView.text = module.getString("name")
            difficultyTextView.text = module.getString("difficultyName")
            popularityTextView.text = module.getInt("price").toString()
            ratingTextView.text = module.getInt("rating").toString()

            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(10, 30, 10, 10)
            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.topToBottom = if (columnIndex == 0 && rowIndex == 0) R.id.RecommendBlock else parentLayout.getChildAt(parentLayout.childCount - 1).id

            parentLayout.addView(moduleCard1, layoutParams)

            columnIndex++
            if (columnIndex == 2) {
                columnIndex = 0
                rowIndex++
            }
        }
    }

}