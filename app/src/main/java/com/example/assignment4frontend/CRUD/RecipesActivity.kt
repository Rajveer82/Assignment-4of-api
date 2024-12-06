package com.example.assignment4frontend.CRUD


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.assignment4frontend.R
import com.example.assignment4frontend.apis.Recipe
import com.example.assignment4frontend.apis.RetrofitClient
import com.example.assignment4frontend.authentication.SignInActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipesActivity : AppCompatActivity() {

    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_list)

        Log.d("RecipesActivity", "Navigated to RecipesActivity")

        // Initialize RecyclerView
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView)
        recipesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            fetchTop20Recipes() // Fetch recipes when pulled down
        }

        // Initialize Logout Button
        logoutButton = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }

        // Fetch recipes
        fetchTop20Recipes()
    }

    private fun fetchTop20Recipes() {
        // Retrieve token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val token = sharedPreferences.getString("authToken", null)

        if (token.isNullOrEmpty()) {
            Log.e("RecipesActivity", "No token found. Cannot fetch recipes.")
            Toast.makeText(this, "You are not logged in. Please login first.", Toast.LENGTH_SHORT).show()
            swipeRefreshLayout.isRefreshing = false
            return
        }

        val authHeader = "Bearer $token"
        Log.d("RecipesActivity", "Fetching recipes with token: $authHeader")

        // Show the refresh indicator
        swipeRefreshLayout.isRefreshing = true

        // Make API call to fetch recipes
        RetrofitClient.authApi.getTop20Recipes(authHeader).enqueue(object : Callback<List<Recipe>> {
            override fun onResponse(call: Call<List<Recipe>>, response: Response<List<Recipe>>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val recipes = response.body() ?: emptyList()
                    Log.d("RecipesActivity", "Successfully fetched recipes: $recipes")

                    if (recipes.isEmpty()) {
                        Toast.makeText(this@RecipesActivity, "No recipes found.", Toast.LENGTH_SHORT).show()
                    } else {
                        val adapter = RecipesAdapter(this@RecipesActivity, recipes)
                        recipesRecyclerView.adapter = adapter
                    }
                } else {
                    Log.e("RecipesActivity", "Failed to fetch recipes: ${response.errorBody()?.string()}")
                    Toast.makeText(this@RecipesActivity, "Failed to fetch recipes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Recipe>>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Log.e("RecipesActivity", "Error fetching recipes: ${t.message}", t)
                Toast.makeText(this@RecipesActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun logout() {
        // Clear the token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        sharedPreferences.edit().remove("authToken").apply()

        // Navigate to LoginActivity
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
