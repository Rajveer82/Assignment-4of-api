package com.example.assingment4.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assingment4.R
import com.example.assingment4.apis.RegisterRequest
import com.example.assingment4.apis.RegisterResponse
import com.example.assingment4.apis.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var contactEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText)
        usernameEditText = findViewById(R.id.usernameEditText)
        contactEditText = findViewById(R.id.contactEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        loginLink = findViewById(R.id.loginLink)

        // Set up button listeners
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val contactStr = contactEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (name.isEmpty() || username.isEmpty() || contactStr.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val contact = contactStr.toIntOrNull()
                if (contact == null) {
                    Toast.makeText(this, "Contact must be a number", Toast.LENGTH_SHORT).show()
                } else {
                    registerUser(name, username, contact, email, password)
                }
            }
        }

        loginLink.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser(name: String, username: String, contact: Int, email: String, password: String) {
        val request = RegisterRequest(name, username, contact, email, password)
        Log.d("RegisterActivity", "Registering user with data: $request")

        RetrofitClient.authApi.registerUser(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("RegisterActivity", "Registration successful: ${response.body()}")
                    Toast.makeText(this@SignUpActivity, "Registration successful!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("RegisterActivity", "Registration failed: ${response.errorBody()?.string()}")
                    Toast.makeText(this@SignUpActivity, "Registration failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("RegisterActivity", "Error occurred: ${t.message}")
                Toast.makeText(this@SignUpActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
