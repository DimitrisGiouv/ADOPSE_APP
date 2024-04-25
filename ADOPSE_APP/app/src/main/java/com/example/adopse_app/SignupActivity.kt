package com.example.adopse_app

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

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


            val Items = JSONObject();
            Items.put("username", usernameText)
            Items.put("password",passwordText)
            Items.put("email",emailText)
            val queue = Volley.newRequestQueue(this)
            val url = "http://10.0.2.2:5051/Authentication/register"

            // Request a string response from the provided URL.
            val request = JsonObjectRequest (Request.Method.POST,url,Items,
                { response ->
                    // Handle response from server
                    // For example, show a toast message indicating successful registration
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    // Handle error response from server
                    // For example, show a toast message indicating registration failed
                    Toast.makeText(this, "Registration failed: ${error.message}", Toast.LENGTH_SHORT).show()
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