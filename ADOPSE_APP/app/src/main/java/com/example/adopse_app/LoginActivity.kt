package com.example.adopse_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.Response


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.pwd)
        val btnLogin = findViewById<LinearLayout>(R.id.login_button)

        btnLogin.setOnClickListener{
            val Items = JSONObject()
            Items.put("username", username.text.toString())
            Items.put("password", password.text.toString())
            Items.put("email","x@x.x")

            val queue = Volley.newRequestQueue(this)
            val url = "http://10.0.2.2:5051/Authentication/login"

            val request = JsonObjectRequest(Request.Method.POST, url, Items,
                { response ->
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                },
                { error ->
                    Toast.makeText(this,"User not found", Toast.LENGTH_SHORT).show()
                })

            queue.add(request)
        }

   }


}
