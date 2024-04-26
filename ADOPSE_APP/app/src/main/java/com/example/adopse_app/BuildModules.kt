package com.example.adopse_app

import android.app.Activity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class BuildModules : AppCompatActivity() {
    var isSingleView = false

    fun toggleModuleList(activity: Activity) {
        val parentLayout: ConstraintLayout = activity.findViewById(R.id.LinearModules)
        parentLayout.removeAllViews()

        repeat(6) { index ->
            val moduleCard = if (isSingleView) {
                activity.layoutInflater.inflate(R.layout.module_long, null) as ConstraintLayout
            } else {
                activity.layoutInflater.inflate(R.layout.module, null) as ConstraintLayout
            }
            moduleCard.id = View.generateViewId()

            val moduleTextView = moduleCard.findViewById<TextView>(R.id.module1)
            var descriptorTextView = moduleCard.findViewById<TextView>(R.id.Dismodule1)
            val difficultyTextView = moduleCard.findViewById<TextView>(R.id.difficulty_module1)
            val popularityTextView = moduleCard.findViewById<TextView>(R.id.popularity_module1)
            val ratingTextView = moduleCard.findViewById<TextView>(R.id.rating_module1)

            val queue = Volley.newRequestQueue(activity)

            val url = "http://10.0.2.2:5051/module/" + (15139 + index)
            val request = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    moduleTextView.text = response.get("name").toString()
                    descriptorTextView.text = response.get("description").toString()
                    difficultyTextView.text = response.get("difficultyName").toString()
                    popularityTextView.text = response.get("price").toString()
                    ratingTextView.text = response.get("rating").toString()

                    val sizeLimitation = response.get("description").toString()
                    // Truncate the description text if it exceeds 30 characters
                    val truncatedDescription = if (sizeLimitation.length > 30) {
                        sizeLimitation.substring(0, 30) + "..." // Add ellipsis if truncated
                    } else {
                        sizeLimitation // Otherwise, use the original text
                    }

                    descriptorTextView.text = truncatedDescription
                },
                { error ->
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                }
            )
            queue.add(request)

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
                    if (index == 0) R.id.RecommendBlock else parentLayout.getChildAt(index - 1).id
            } else {
                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    moduleCardHeight
                )
                layoutParams.setMargins(10, 10, 10, 10)
                parentLayout.addView(moduleCard, layoutParams)
                if (index % 2 == 0) {
                    layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    layoutParams.topToBottom =
                        if (index == 0) R.id.RecommendBlock else parentLayout.getChildAt(index - 1).id
                } else {
                    layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    layoutParams.topToBottom =
                        if (index == 1) R.id.RecommendBlock else parentLayout.getChildAt(index - 2).id
                }
            }
        }
    }

    fun onToggleViewButtonClick() {
        isSingleView = !isSingleView // Toggle the boolean flag
    }
}