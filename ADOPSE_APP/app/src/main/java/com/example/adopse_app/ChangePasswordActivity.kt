package com.example.adopse_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_change_password)

        val username = findViewById<EditText>(R.id.username)
        val email = findViewById<EditText>(R.id.email)
        val newPwd = findViewById<EditText>(R.id.newPassword)
        val submitBtn = findViewById<TextView>(R.id.submitBtn)

        submitBtn.setOnClickListener{
            val usernameText = username.text.toString().trim()
            val emailText = email.text.toString().trim()
            val newPwdText = newPwd.text.toString().trim()

            if(usernameText.isEmpty() || newPwdText.isEmpty() || emailText.isEmpty()){
                Toast.makeText(this, "Please fill the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Make a request to get all users
            val usersUrl = "http://10.0.2.2:5051/Authentication/getUsers"
            val usersRequest = JsonArrayRequest(Request.Method.GET, usersUrl, null,
                { response ->
                    // Handle successful response
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
                        // User found, proceed with changing password
                        // Construct JSON object with new password and user details
                        val userPasswordUpdate = JSONObject()

                        // Add username, email, and id to userPasswordUpdate
                        userPasswordUpdate.put("username", userJsonObject?.getString("username"))
                        userPasswordUpdate.put("email", userJsonObject?.getString("email"))
                        userPasswordUpdate.put("id", userJsonObject?.getString("id"))

                        // Update password
                        userPasswordUpdate.put("password", newPwdText)

                        // Make request to update user's password
                        val queue = Volley.newRequestQueue(this)
                        val updateUrl = "http://10.0.2.2:5051/Authentication/updateUser"
                        val request = JsonObjectRequest(Request.Method.POST, updateUrl, userPasswordUpdate,
                            { response ->
                                // Password changed successfully
                                Toast.makeText(this,"Password changed successfully", Toast.LENGTH_SHORT).show()
                                Handler().postDelayed({
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                },2000)
                            },
                            { error ->
                                // Error updating password
                                Toast.makeText(this,"Failed to update password", Toast.LENGTH_SHORT).show()
                            })
                        queue.add(request)
                    } else {
                        // User not found
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    // Handle error
                    Toast.makeText(this,"Failed to retrieve users", Toast.LENGTH_SHORT).show()
                })

            // Add the request to the RequestQueue
            Volley.newRequestQueue(this).add(usersRequest)
        }



    }
}