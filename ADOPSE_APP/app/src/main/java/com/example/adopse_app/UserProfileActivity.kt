package com.example.adopse_app

import android.content.Intent
import android.os.Bundle
import android.view.View
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

        /* Redirection to Sign up if user does not have an account */
        val tempRedirect = findViewById<ImageButton>(R.id.user_profile_picture)
        tempRedirect.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        /* Navigation Hamburger */
        val navigationButton = findViewById<ImageButton>(R.id.navigation_button)
        navigationButton.setOnClickListener {
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        }

        val logoButton = findViewById<ImageButton>(R.id.logo_button)
        logoButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        populateUserCourses();
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

            val moduleTextView = moduleCard.findViewById<TextView>(R.id.module1)
            val disModuleTextView = moduleCard.findViewById<TextView>(R.id.Dismodule1)
            val difficultyTextView = moduleCard.findViewById<TextView>(R.id.difficulty_module1)
            val popularityTextView = moduleCard.findViewById<TextView>(R.id.popularity_module1)
            val ratingTextView = moduleCard.findViewById<TextView>(R.id.rating_module1)

            moduleTextView.text = "Module ${index + 1}"
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
}