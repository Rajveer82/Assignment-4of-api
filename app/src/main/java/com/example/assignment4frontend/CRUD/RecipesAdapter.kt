package com.example.assignment4frontend.CRUD



import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment4frontend.R
import com.example.assignment4frontend.apis.Recipe

class RecipesAdapter(private val context: Context, private val recipes: List<Recipe>) :
    RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeImageView: ImageView = view.findViewById(R.id.recipeImageView)
        val recipeTitle: TextView = view.findViewById(R.id.recipeTitle)
        val recipeStudio: TextView = view.findViewById(R.id.recipeStudio)
        val recipeRating: TextView = view.findViewById(R.id.recipeRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_list_recipie, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]

        // Load recipe image
        Glide.with(context)
            .load(recipe.photoLink)
            .placeholder(R.drawable.placeholder) // Add a placeholder image
            .into(holder.recipeImageView)

        // Bind data to views
        holder.recipeTitle.text = recipe.recipeName
        holder.recipeStudio.text = recipe.cuisine
        holder.recipeRating.text = "Rating: ${recipe.averageRating}"

        // Handle item click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra("recipe", recipe) // Pass the recipe object
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = recipes.size
}
