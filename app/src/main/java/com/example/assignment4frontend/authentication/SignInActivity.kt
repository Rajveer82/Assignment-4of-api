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
import com.example.assingment4.CRUD.RecipesActivity
import com.example.assingment4.apis.LoginRequest
import com.example.assingment4.apis.LoginResponse
import com.example.assingment4.apis.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        // Initialize views
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerLink = findViewById(R.id.registerLink)

        // Set up button listeners
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                performLogin(username, password)
            }
        }
        registerLink.setOnClickListener {
            Log.d("LoginActivity", "Navigating to RegisterActivity")
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        registerLink.setOnClickListener {
            Toast.makeText(this, "Navigate to Register Screen", Toast.LENGTH_SHORT).show()
            // Navigate to the RegisterActivity (if implemented)
            // val intent = Intent(this, RegisterActivity::class.java)
            // startActivity(intent)
        }
    }

    private fun performLogin(username: String, password: String) {
        val request = LoginRequest(username, password)
        Log.d("LoginActivity", "Initiating login with data: $request")

        RetrofitClient.authApi.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("LoginActivity", "Login successful: ${it.message}")
                        saveToken(it.token)
                        Toast.makeText(this@SignInActivity, it.message, Toast.LENGTH_SHORT).show()

                        // Navigate to RecipesActivity
                        navigateToRecipesScreen()
                    } ?: run {
                        Log.e("LoginActivity", "Login response body is null")
                        Toast.makeText(this@SignInActivity, "Unexpected error occurred", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("LoginActivity", "Login failed: ${response.errorBody()?.string()}")
                    Toast.makeText(this@SignInActivity, "Login failed. Try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "Login error: ${t.message}")
                Toast.makeText(this@SignInActivity, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveToken(token: String) {
        Log.d("LoginActivity", "Saving token: $token")
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        sharedPreferences.edit().putString("authToken", token).apply()
        Log.d("LoginActivity", "Token saved successfully")
    }

    private fun navigateToRecipesScreen() {
        Log.d("LoginActivity", "Navigating to RecipesActivity")
        val intent = Intent(this@SignInActivity, RecipesActivity::class.java)
        startActivity(intent)
        finish() // Close the LoginActivity so it’s not in the back stack
    }
}
