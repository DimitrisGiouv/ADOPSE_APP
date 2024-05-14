package com.example.adopse_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import com.android.volley.toolbox.JsonArrayRequest



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

                    // Find user's details
                    val usersUrl = "http://10.0.2.2:5051/Authentication/getUsers"
                    val usersRequest = JsonArrayRequest(Request.Method.GET, usersUrl, null,
                        { response ->
                            var userFound = false
                            var userJsonObject: JSONObject? = null

                            // Iterate through users to find the user with the same username
                            for (i in 0 until response.length()) {
                                val user = response.getJSONObject(i)
                                val userUsername = user.getString("username")

                                if (userUsername == usernameText) {
                                    // User found
                                    userFound = true
                                    userJsonObject = user
                                    break
                                }
                            }

                            if (userFound) {
                                val sharedPreferences = getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
                                with(sharedPreferences.edit()){
                                    putString("username", userJsonObject?.getString("username"))
                                    putString("email", userJsonObject?.getString("email"))
                                    putInt("id", userJsonObject?.getInt("id")?: -1)
                                    putBoolean("isLogIn", true)
                                    apply()
                                }

                            } else {
                                // User not found
                                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                            }
                        },
                        { error ->
                            // Handle error
                            Toast.makeText(this,"Failed find user's details", Toast.LENGTH_SHORT).show()
                        })

                    Volley.newRequestQueue(this).add(usersRequest)

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
