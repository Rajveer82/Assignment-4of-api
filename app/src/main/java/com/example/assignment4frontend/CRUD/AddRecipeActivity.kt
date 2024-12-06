package com.example.assignment4frontend.CRUD



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment4frontend.R
import com.example.assignment4frontend.apis.Recipe
import com.example.assignment4frontend.apis.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var recipeNameEditText: EditText
    private lateinit var ingredientsEditText: EditText
    private lateinit var cookingTimeEditText: EditText
    private lateinit var difficultyEditText: EditText
    private lateinit var cuisineEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var photoLinkEditText: EditText
    private lateinit var averageRatingEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        // Set up back button functionality
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed() // Navigates back to the previous screen
        }

        // Initialize views
        recipeNameEditText = findViewById(R.id.recipeNameEditText)
        ingredientsEditText = findViewById(R.id.ingredientsEditText)
        cookingTimeEditText = findViewById(R.id.cookingTimeEditText)
        difficultyEditText = findViewById(R.id.difficultyEditText)
        cuisineEditText = findViewById(R.id.cuisineEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        photoLinkEditText = findViewById(R.id.photoLinkEditText)
        averageRatingEditText = findViewById(R.id.averageRatingEditText)
        submitButton = findViewById(R.id.submitButton)




        submitButton.setOnClickListener {
            addRecipe()
        }
    }

    private fun addRecipe() {
        // Retrieve token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val token = sharedPreferences.getString("authToken", null)
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Authentication token not found. Please login again.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create recipe object
        val recipe = Recipe(
            _id = "", // Not required for creation
            recipeName = recipeNameEditText.text.toString(),
            ingredients = ingredientsEditText.text.toString().split(",").map { it.trim() },
            cookingTime = cookingTimeEditText.text.toString().toInt(),
            difficulty = difficultyEditText.text.toString(),
            cuisine = cuisineEditText.text.toString(),
            description = descriptionEditText.text.toString(),
            photoLink = photoLinkEditText.text.toString(),
            averageRating = averageRatingEditText.text.toString().toDouble()
        )

        // Make the POST request
        RetrofitClient.authApi.createRecipe("Bearer $token", recipe).enqueue(object : Callback<Recipe> {
            override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddRecipeActivity, "Recipe added successfully!", Toast.LENGTH_SHORT).show()
                    navigateBackToList()
                } else {
                    Log.e("AddRecipeActivity", "Failed to add recipe: ${response.errorBody()?.string()}")
                    Toast.makeText(this@AddRecipeActivity, "Failed to add recipe!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Recipe>, t: Throwable) {
                Log.e("AddRecipeActivity", "Error adding recipe: ${t.message}")
                Toast.makeText(this@AddRecipeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun navigateBackToList() {
        val intent = Intent(this, RecipesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
