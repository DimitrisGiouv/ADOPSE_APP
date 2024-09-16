package com.example.adopse_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
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

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_student)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_student)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navigationCode = NavigationBar()
        navigationCode.NavigationCode(this)

        val logoutButton = findViewById<Button>(R.id.logout_button)
        logoutButton.setOnClickListener {

            // Perform logout actions
            val sharedPreferences = getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            // Redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val editProfileButton = findViewById<Button>(R.id.edit_button)
        editProfileButton.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

        populateUserCourses();

        val sharedPreferences = getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        val textViewUsername = findViewById<TextView>(R.id.profile_name)
        textViewUsername.text = username
    }

    private fun populateUserCourses() {
        val parentLayout: ConstraintLayout = findViewById(R.id.user_courses_section)

        val yourCoursesPill = findViewById<TextView>(R.id.your_courses_pill);
        val seeMoreButton = findViewById<TextView>(R.id.moreModules_button);

        for (i in parentLayout.childCount - 1 downTo 0) {
            val child = parentLayout.getChildAt(i)
            if (child.id != yourCoursesPill.id && child.id != seeMoreButton.id) {
                parentLayout.removeView(child)
            }
        }

        val moduleCount = 2 // Assuming you're adding two modules in this example

        seeMoreButton.visibility = if (moduleCount > 1) View.VISIBLE else View.GONE

        val moduleIds = mutableListOf<Int>() // List to store module IDs

        // Add module cards
        for (index in 0 until moduleCount) {
            val moduleCard = layoutInflater.inflate(R.layout.module, null) as ConstraintLayout
            moduleCard.id = View.generateViewId()
            moduleIds.add(moduleCard.id) // Add module ID to the list

            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            layoutParams.topToBottom = yourCoursesPill.id
            if (index == 0) {
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            } else {
                layoutParams.startToEnd = moduleIds[index - 1] // Use previously added module ID
            }

            val moduleCardMarginStart = resources.getDimensionPixelSize(R.dimen.profile_modulecard_margin_start)
            val moduleCardMarginTop = resources.getDimensionPixelSize(R.dimen.profile_modulecard_margin_top)

            layoutParams.marginStart = moduleCardMarginStart
            layoutParams.topMargin = moduleCardMarginTop

            parentLayout.addView(moduleCard, layoutParams)

            // Call method to populate module data
            populateUserCourseData(moduleCard, index)
        }
    }


    private fun populateUserCourseData(moduleCard: ConstraintLayout, index: Int) {
        val moduleTextView = moduleCard.findViewById<TextView>(R.id.module1)
        val descriptorTextView = moduleCard.findViewById<TextView>(R.id.Dismodule1)
        val difficultyTextView = moduleCard.findViewById<TextView>(R.id.difficulty_module1)
        val popularityTextView = moduleCard.findViewById<TextView>(R.id.popularity_module1)
        val ratingTextView = moduleCard.findViewById<TextView>(R.id.rating_module1)

        val queue = Volley.newRequestQueue(this)

        val moduleOfPage = 15139 + (index * 10)
        val url = "http://185.234.52.109/api/module/$moduleOfPage"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                moduleTextView.text = response.getString("name")
                val fullDescription = response.getString("description")
                descriptorTextView.text = response.getString("description")
                difficultyTextView.text = response.getString("difficultyName")
                popularityTextView.text = response.getString("price")
                ratingTextView.text = response.getString("rating")

                val sizeLimitation = response.get("description").toString()
                // Truncate the description text if it exceeds 30 characters
                val truncatedDescription = if (sizeLimitation.length > 30) {
                    sizeLimitation.substring(0, 30) + "..." // Add ellipsis if truncated
                } else {
                    sizeLimitation // Otherwise, use the original text
                }

                descriptorTextView.text = truncatedDescription

                moduleCard.setOnClickListener {
                    val moduleName = moduleTextView.text.toString()
                    val moduleDescription = descriptorTextView.text.toString()
                    val moduleDifficulty = difficultyTextView.text.toString()
                    val modulePopularity = popularityTextView.text.toString()
                    val moduleRating = ratingTextView.text.toString()

                    val intent = Intent(this, ModuleProfileActivity::class.java).apply {
                        putExtra("moduleName", moduleName)
                        putExtra("moduleDescription", fullDescription)
                        putExtra("moduleDifficulty", moduleDifficulty)
                        putExtra("modulePopularity", modulePopularity)
                        putExtra("moduleRating", moduleRating)
                    }
                    startActivity(intent)
                }
            },
            { error ->
                if (error is NoConnectionError || error is TimeoutError) {
                    Toast.makeText(this, "No internet connection or server unavailable", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to retrieve module data", Toast.LENGTH_SHORT).show()
                }
            }
        )
        queue.add(request)


    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
        return sharedPreferences.contains("isLogged")
    }

}