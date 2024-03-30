package com.example.adopse_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
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

// Δημιουργία 10 περιπτώσεων του μοντέλου ModuleCard1
        repeat(10) { index ->
            val moduleCard1 = layoutInflater.inflate(R.layout.module, null) as ConstraintLayout
            moduleCard1.id = View.generateViewId() // Δημιουργία μοναδικού αναγνωριστικού για κάθε κάρτα ενότητας

            // Εύρεση TextViews μέσα στο ModuleCard1
            val moduleTextView = moduleCard1.findViewById<TextView>(R.id.module1)
            val disModuleTextView = moduleCard1.findViewById<TextView>(R.id.Dismodule1)
            val difficultyTextView = moduleCard1.findViewById<TextView>(R.id.difficulty_module1)
            val popularityTextView = moduleCard1.findViewById<TextView>(R.id.popularity_module1)
            val ratingTextView = moduleCard1.findViewById<TextView>(R.id.rating_module1)

            // Ορισμός κειμένου για τα TextViews
            moduleTextView.text = "Μονάδα ${index + 1}"
            disModuleTextView.text = "Περιγραφή της μονάδας ${index + 1}"
            difficultyTextView.text = "Εύκολο"
            popularityTextView.text = "50"
            ratingTextView.text = "4"

            // Προσθήκη περιορισμών για τη θέση
            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            if (index % 2 == 0) {
                layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.topToBottom = if (index == 0) R.id.Recommend else parentLayout.getChildAt(index - 1).id
            } else {
                layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.topToBottom = if (index == 1) R.id.Recommend else parentLayout.getChildAt(index - 2).id
            }

            layoutParams.setMargins(10, 10, 10, 10)
            parentLayout.addView(moduleCard1, layoutParams)
        }
    }
    fun setupButton() {
        val btnList = findViewById<Button>(R.id.listViewButton)

        btnList.setOnClickListener {
            // Ορίζουμε τη συνάρτηση singleModuleList εδώ
            fun singleModuleList() {
                val parentLayout: ConstraintLayout = findViewById(R.id.LinearModules)
                repeat(10) { index ->
                    val moduleCard1 = layoutInflater.inflate(R.layout.module, null) as ConstraintLayout
                    moduleCard1.id = View.generateViewId() // Δημιουργία μοναδικού αναγνωριστικού για κάθε κάρτα ενότητας

                    // Εύρεση TextViews μέσα στο ModuleCard1
                    val moduleTextView = moduleCard1.findViewById<TextView>(R.id.module1)
                    val disModuleTextView = moduleCard1.findViewById<TextView>(R.id.Dismodule1)
                    val difficultyTextView = moduleCard1.findViewById<TextView>(R.id.difficulty_module1)
                    val popularityTextView = moduleCard1.findViewById<TextView>(R.id.popularity_module1)
                    val ratingTextView = moduleCard1.findViewById<TextView>(R.id.rating_module1)

                    // Ορισμός κειμένου για τα TextViews
                    moduleTextView.text = "Μονάδα ${index + 1}"
                    disModuleTextView.text = "Περιγραφή της μονάδας ${index + 1}"
                    difficultyTextView.text = "Εύκολο"
                    popularityTextView.text = "50"
                    ratingTextView.text = "4"

                    // Προσθήκη περιορισμών για τη θέση
                    val layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )

                    layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    layoutParams.topToBottom = if (index == 0) R.id.Recommend else parentLayout.getChildAt(index - 1).id

                    layoutParams.setMargins(10, 10, 10, 10)
                    parentLayout.addView(moduleCard1, layoutParams)
                }
            }

            // Καλούμε τη συνάρτηση που ορίσαμε
            singleModuleList()
        }
    }

    fun twoModuleList(){
        val parentLayout: ConstraintLayout = findViewById(R.id.LinearModules)
        repeat(10) { index ->
                    val moduleCard1 = layoutInflater.inflate(R.layout.module, null) as ConstraintLayout
                    moduleCard1.id = View.generateViewId() // Δημιουργία μοναδικού αναγνωριστικού για κάθε κάρτα ενότητας

                    // Εύρεση TextViews μέσα στο ModuleCard1
                    val moduleTextView = moduleCard1.findViewById<TextView>(R.id.module1)
                    val disModuleTextView = moduleCard1.findViewById<TextView>(R.id.Dismodule1)
                    val difficultyTextView = moduleCard1.findViewById<TextView>(R.id.difficulty_module1)
                    val popularityTextView = moduleCard1.findViewById<TextView>(R.id.popularity_module1)
                    val ratingTextView = moduleCard1.findViewById<TextView>(R.id.rating_module1)

                    // Ορισμός κειμένου για τα TextViews
                    moduleTextView.text = "Μονάδα ${index + 1}"
                    disModuleTextView.text = "Περιγραφή της μονάδας ${index + 1}"
                    difficultyTextView.text = "Εύκολο"
                    popularityTextView.text = "50"
                    ratingTextView.text = "4"

                    // Προσθήκη περιορισμών για τη θέση
                    val layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )

                    if (index % 2 == 0) {
                        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                        layoutParams.topToBottom = if (index == 0) R.id.Recommend else parentLayout.getChildAt(index - 1).id
                    } else {
                        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                        layoutParams.topToBottom = if (index == 1) R.id.Recommend else parentLayout.getChildAt(index - 2).id
                    }

                    layoutParams.setMargins(10, 10, 10, 10)
                    parentLayout.addView(moduleCard1, layoutParams)
                }

    }


}
