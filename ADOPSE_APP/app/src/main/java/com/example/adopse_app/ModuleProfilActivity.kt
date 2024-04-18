package com.example.adopse_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ModuleProfilActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private  lateinit var  adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moduleprofil)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create a list of dummy items
        val items = listOf(
            Item("20", "Introduction to UI/UX design"),
            Item("30", "Android Development Basics"),
            Item("25", "Data Structures and Algorithms"),
        )

        adapter = MyAdapter(items)

        recyclerView.adapter = adapter

    }
}