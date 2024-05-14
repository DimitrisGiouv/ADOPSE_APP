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

        val usernameField = findViewById<EditText>(R.id.username)
        val emailField = findViewById<EditText>(R.id.email)
        val newPwdField = findViewById<EditText>(R.id.newPassword)
        val submitBtn = findViewById<TextView>(R.id.submitBtn)

        submitBtn.setOnClickListener{
            val usernameText = usernameField.text.toString().trim()
            val emailText = emailField.text.toString().trim()
            val newPwdText = newPwdField.text.toString().trim()

            if(usernameText.isEmpty() || newPwdText.isEmpty() || emailText.isEmpty()){
                Toast.makeText(this, "Please fill the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(newPwdText.length < 8){
                Toast.makeText(this, "Password has to be more than 8 characters.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!hasSpecialCharAndUpper(newPwdText)){
                Toast.makeText(this, "Password has to contain special characters and uppers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPreferences = getSharedPreferences("myAppPref", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("username", "")
            val email = sharedPreferences.getString("email", "")
            val id = sharedPreferences.getInt("id", -1)

            if(usernameText != username || emailText != email){
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val Items = JSONObject()
            Items.put("id", id)
            Items.put("username", usernameText)
            Items.put("email", emailText)
            Items.put("password", newPwdText)


            val queue = Volley.newRequestQueue(this)
            val updateUrl = "http://10.0.2.2:5051/Authentication/updateUser"
            val request = JsonObjectRequest(Request.Method.POST, updateUrl, Items,
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