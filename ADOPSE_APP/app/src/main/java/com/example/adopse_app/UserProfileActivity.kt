package com.example.adopse_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
        val textViewUsername = findViewById<TextView>(R.id.profile_welcome)
        textViewUsername.text = username
    }

    private fun populateUserCourses(){
        val parentLayout: ConstraintLayout = findViewById(R.id.user_courses_section)

        val yourCoursesPill = findViewById<TextView>(R.id.your_courses_pill);
        val seeMoreButton = findViewById<TextView>(R.id.moreModules_button);

        for (i in parentLayout.childCount - 1 downTo 0){
            val child = parentLayout.getChildAt(i)
            if (child.id != yourCoursesPill.id && child.id != seeMoreButton.id){
                parentLayout.removeView(child)
            }
        }


        val moduleCount = 2 // Assuming you're adding two modules in this example
        val moduleIds = arrayOf(R.id.ModuleCard1, R.id.ModuleCard2)
        seeMoreButton.visibility = if (moduleCount > 1) View.VISIBLE else View.GONE


        // Add module cards
        for (index in 0 until moduleCount) {
            val moduleCard = layoutInflater.inflate(R.layout.module, null) as ConstraintLayout
            moduleCard.id = moduleIds[index]

            val moduleName = intent.getStringExtra("moduleName")
            val moduleTextView = moduleCard.findViewById<TextView>(R.id.module1)
            val disModuleTextView = moduleCard.findViewById<TextView>(R.id.Dismodule1)
            val difficultyTextView = moduleCard.findViewById<TextView>(R.id.difficulty_module1)
            val popularityTextView = moduleCard.findViewById<TextView>(R.id.popularity_module1)
            val ratingTextView = moduleCard.findViewById<TextView>(R.id.rating_module1)

            moduleTextView.text = moduleName
            disModuleTextView.text = "Description of module ${index + 1}"
            difficultyTextView.text = "Easy"
            popularityTextView.text = "50"
            ratingTextView.text = "4"

            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )


            layoutParams.topToBottom = yourCoursesPill.id
            if (index == 0) {
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            } else {
                layoutParams.startToEnd = moduleIds[index - 1]
            }

            val moduleCardMarginStart = resources.getDimensionPixelSize(R.dimen.profile_modulecard_margin_start)
            val moduleCardMarginTop = resources.getDimensionPixelSize(R.dimen.profile_modulecard_margin_top)

            layoutParams.marginStart = moduleCardMarginStart
            layoutParams.topMargin = moduleCardMarginTop

            parentLayout.addView(moduleCard, layoutParams)
        }

    }
    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
        return sharedPreferences.contains("isLogged")
    }

}