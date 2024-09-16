package com.example.adopse_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.logging.Handler
import kotlin.concurrent.timerTask

class SignupActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge();
        setContentView(R.layout.activity_signup)

        val email = findViewById<EditText>(R.id.email)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        /* Temporary Until Full Development */
        val btnSignup = findViewById<LinearLayout>(R.id.register_button)

        btnSignup.setOnClickListener{
            val emailText = email.text.toString().trim()
            val usernameText = username.text.toString().trim()
            val passwordText = password.text.toString().trim()

            if(emailText.isEmpty() || usernameText.isEmpty() || passwordText.isEmpty()){
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Stop further execution
            }

            if(passwordText.length < 8){
                Toast.makeText(this, "Password has to be more than 8 characters.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!hasSpecialCharAndUpper(passwordText)){
                Toast.makeText(this, "Password has to contain special characters and uppers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val Items = JSONObject()
            Items.put("username", usernameText)
            Items.put("password",passwordText)
            Items.put("email",emailText)
            val queue = Volley.newRequestQueue(this)
            val url = "http://185.234.52.109/api/Authentication/register"

            // Request a string response from the provided URL.
            val request = JsonObjectRequest (Request.Method.POST,url,Items,
                { response ->
                    Toast.makeText(this, "Welcome ${usernameText}", Toast.LENGTH_SHORT).show()
                    // Find user's details
                    val usersUrl = "http://185.234.52.109/api/Authentication/getUsers"
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
                                val sharedPreferences = getSharedPreferences("myAppPref", MODE_PRIVATE)
                                with(sharedPreferences.edit()){
                                    putString("username", userJsonObject?.getString("username"))
                                    putString("password", userJsonObject?.getString("password"))
                                    putString("email", userJsonObject?.getString("email"))
                                    putInt("id", userJsonObject?.getInt("id")?: -1)
                                    apply()
                                }

                                val username1 = sharedPreferences.getString("username", "")
                                val email = sharedPreferences.getString("email", "")
                                val id = sharedPreferences.getInt("id", -1)

                                Log.d("SharedPreferences", "Username: $username1")
                                Log.d("SharedPreferences", "Email: $email")
                                Log.d("SharedPreferences", "ID: $id")
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
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    },2000)
                },
                { error ->
                    // Handle error response from server
                    // For example, show a toast message indicating registration failed
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_LONG).show()
                }
            )
            queue.add(request)
        }

        val btnLoginRegisterPage = findViewById<LinearLayout>(R.id.redirect_login)

        btnLoginRegisterPage.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
    fun hasSpecialCharAndUpper(password: String): Boolean{
        val specialCharRegex = Regex("[!@#$%^&*(){}:;><.,?]")
        val upperRegex = Regex("[A-Z]")

        val containsSpecialChar = specialCharRegex.containsMatchIn(password)
        val containsUpper = upperRegex.containsMatchIn(password)

        return containsSpecialChar && containsUpper
    }
}