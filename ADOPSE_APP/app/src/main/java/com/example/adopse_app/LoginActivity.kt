package com.example.adopse_app

import android.content.Context
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
        val btnSignUp = findViewById<LinearLayout>(R.id.signup)

        btnLogin.setOnClickListener{
            val usernameText = username.text.toString().trim()
            val passwordText = password.text.toString().trim()

            if(usernameText.isEmpty() || passwordText.isEmpty()){
                Toast.makeText(this, "Please fill the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val Items = JSONObject()
            Items.put("username", usernameText)
            Items.put("password", passwordText)
            Items.put("email","x@x.x")
            val queue = Volley.newRequestQueue(this)
            val url = "http://10.0.2.2:5051/Authentication/login"

            val request = JsonObjectRequest(Request.Method.POST, url, Items,
                {response ->
                    Toast.makeText(this,"Welcome back ${usernameText}", Toast.LENGTH_SHORT).show()
                    val sharedPreferences = getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("username", usernameText)
                    editor.putString("password", passwordText)
                    editor.putString("isLogged", "true")
                    editor.apply()
                    android.os.Handler().postDelayed({
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    },2000)
                },
                {error ->
                    Toast.makeText(this,"User not found", Toast.LENGTH_SHORT).show()
                })

            queue.add(request)
        }



        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
   }


}
