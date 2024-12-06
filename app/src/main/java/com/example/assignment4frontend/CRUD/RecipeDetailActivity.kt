package com.example.assignment4frontend.CRUD


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.assignment4frontend.R
import com.example.assignment4frontend.apis.Recipe
import com.example.assignment4frontend.apis.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var recipeImage: ImageView
    private lateinit var recipeNameEditText: EditText
    private lateinit var ingredientsEditText: EditText
    private lateinit var cookingTimeEditText: EditText
    private lateinit var difficultyEditText: EditText
    private lateinit var cuisineEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var ratingEditText: EditText
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var addMoreButton: Button

    private var recipeId: String? = null // Store the recipe ID for PUT/DELETE operations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        // Initialize views
        recipeImage = findViewById(R.id.recipeImage)
        recipeNameEditText = findViewById(R.id.recipeNameEditText)
        ingredientsEditText = findViewById(R.id.ingredientsEditText)
        cookingTimeEditText = findViewById(R.id.cookingTimeEditText)
        difficultyEditText = findViewById(R.id.difficultyEditText)
        cuisineEditText = findViewById(R.id.cuisineEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        ratingEditText = findViewById(R.id.ratingEditText)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.deleteButton)
        addMoreButton = findViewById(R.id.addMoreButton)

        // Retrieve recipe object from Intent
        val recipe = intent.getSerializableExtra("recipe") as? Recipe
        recipeId = recipe?._id // Save the recipe ID

        if (recipe != null) {
            // Load image
            Glide.with(this)
                .load(recipe.photoLink)
                .placeholder(R.drawable.placeholder)
                .into(recipeImage)

            // Populate fields
            recipeNameEditText.setText(recipe.recipeName)
            ingredientsEditText.setText(recipe.ingredients.joinToString(", "))
            cookingTimeEditText.setText(recipe.cookingTime.toString())
            difficultyEditText.setText(recipe.difficulty)
            cuisineEditText.setText(recipe.cuisine)
            descriptionEditText.setText(recipe.description)
            ratingEditText.setText(recipe.averageRating.toString())
        }

        // Edit Button Click
        editButton.setOnClickListener {
            updateRecipe()
        }

        // Delete Button Click
        deleteButton.setOnClickListener {
            deleteRecipe()
        }

        // Add More Button Click
        addMoreButton.setOnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateRecipe() {
        if (recipeId == null) {
            Toast.makeText(this, "Recipe ID not found!", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrieve token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val token = sharedPreferences.getString("authToken", null)
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Authentication token not found. Please login again.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedRecipe = Recipe(
            _id = recipeId!!,
            recipeName = recipeNameEditText.text.toString(),
            ingredients = ingredientsEditText.text.toString().split(",").map { it.trim() },
            cookingTime = cookingTimeEditText.text.toString().toInt(),
            difficulty = difficultyEditText.text.toString(),
            cuisine = cuisineEditText.text.toString(),
            description = descriptionEditText.text.toString(),
            photoLink = "", // Assuming no photo update
            averageRating = ratingEditText.text.toString().toDouble()
        )

        // Make the PUT request
        RetrofitClient.authApi.updateRecipe("Bearer $token", recipeId!!, updatedRecipe).enqueue(object : Callback<Recipe> {
            override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RecipeDetailActivity, "Recipe updated successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("RecipeDetailActivity", "Update failed: ${response.errorBody()?.string()}")
                    Toast.makeText(this@RecipeDetailActivity, "Failed to update recipe!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Recipe>, t: Throwable) {
                Log.e("RecipeDetailActivity", "Error updating recipe: ${t.message}")
                Toast.makeText(this@RecipeDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteRecipe() {
        if (recipeId == null) {
            Toast.makeText(this, "Recipe ID not found!", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrieve token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val token = sharedPreferences.getString("authToken", null)
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Authentication token not found. Please login again.", Toast.LENGTH_SHORT).show()
            return
        }

        // Make the DELETE request
        RetrofitClient.authApi.deleteRecipe("Bearer $token", recipeId!!).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RecipeDetailActivity, "Recipe deleted successfully!", Toast.LENGTH_SHORT).show()
                    navigateBackToList()
                } else {
                    Log.e("RecipeDetailActivity", "Delete failed: ${response.errorBody()?.string()}")
                    Toast.makeText(this@RecipeDetailActivity, "Failed to delete recipe!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("RecipeDetailActivity", "Error deleting recipe: ${t.message}")
                Toast.makeText(this@RecipeDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
