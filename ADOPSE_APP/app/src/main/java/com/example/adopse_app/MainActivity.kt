package com.example.adopse_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginPage = findViewById<ImageView>(R.id.User)

        loginPage.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val parentLayout: ConstraintLayout = findViewById(R.id.LinearModules)

        // Create 6 instances of ModuleCard1 layout
        repeat(10) { index ->
            val moduleCard1 = layoutInflater.inflate(R.layout.module, null) as ConstraintLayout
            moduleCard1.id = View.generateViewId() // Generate unique ID for each module card

            // Find TextViews inside ModuleCard1
            val moduleTextView = moduleCard1.findViewById<TextView>(R.id.module1)
            val disModuleTextView = moduleCard1.findViewById<TextView>(R.id.Dismodule1)
            val difficultyTextView = moduleCard1.findViewById<TextView>(R.id.difficulty_module1)
            val popularityTextView = moduleCard1.findViewById<TextView>(R.id.popularity_module1)
            val ratingTextView = moduleCard1.findViewById<TextView>(R.id.rating_module1)

            // Set text for TextViews
            moduleTextView.text = "Module ${index + 1}"
            disModuleTextView.text = "Description of module ${index + 1}"
            difficultyTextView.text = "Easy"
            popularityTextView.text = "50"
            ratingTextView.text = "4"

            // Add constraints for positioning
            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            if(index%2 == 0)
            {
                if (index == 0) {
                    layoutParams.topToBottom = R.id.Recommend
                } else {
                    layoutParams.topToBottom = parentLayout.getChildAt((index - 1) / 2) .id // Each ModuleCard1 has 2 children
                }
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.setMargins(10, 10, 10, 10)
                parentLayout.addView(moduleCard1, layoutParams);
            }
            else {
                if (index == 1) {
                      layoutParams.topToBottom = R.id.Recommend
                } else {
                      layoutParams.topToBottom = parentLayout.getChildAt((index - 1) / 2) .id // Each ModuleCard1 has 2 children
                }
                      layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                      layoutParams.setMargins(10, 10, 10, 10)
                      parentLayout.addView(moduleCard1, layoutParams);
            }
        }
    }
}
