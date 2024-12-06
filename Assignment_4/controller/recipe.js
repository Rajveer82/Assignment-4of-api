// filename: recipe.js
// Student name: Rajveer Kaur Gill
// Student Id: 200546782
// Date: 15 November 2024
var Recipe = require("../models/Recipe");
// Function to get the top 20 recipes sorted by average rating
var topRecipe = async function (req, res) {
    try {
        const list = await Recipe
            .find() // Find all recipes
            .sort({ averageRating: -1 }) // Sort by averageRating in descending order
            .limit(20) // Limit the result to 20 recipes
        res.send(list) // Send the list of recipes as the response
    } catch (err) {
        res.send(err) // Send the error if any occurs
    }
}
// Function to get the completes list of all recipes
var getRecipeList = async function (req, res) {
    try {
        // Find all recipes in the database
        const list = await Recipe.find()
        // Send the list of recipes as the response
        res.send(list)
    } catch (err) {
        // send the error message if any error occurs
        res.send(err)
    }
}
// Function to find a recipe by its Id
var findRecipe = async function (req, res) {
    try {
        // Find the recipe by the id provided in the request parameters
        const recipe = await Recipe.findById(req.params.id)

        // Send the found recipe as the response
        res.send(recipe)
    } catch (err) {
        // Send the error message if any error occurs
        res.send(err)
    }
}
// Function to add anew recipe to the database
var addRecipe = async function (req, res) {
    try {
        // Create a new recipe using the data from the request body
        const recipe = new Recipe(req.body)
        await recipe.save()    // Save the recipe to the database
        res.send(recipe)       // Send the saved recipe as the response
    } catch (err) {          // send the error message if any error occurs
        res.send(err);
    }
}
// Function to update an existing recipe by its ID
var updateRecipe = async function (req, res) {
    try {
        // Find recipe by its id and update it with the data from request body
        const recipe = await Recipe.findByIdAndUpdate(req.params.id, req.body, { new: true })
        res.send(recipe)         // send the update recipe as the response
    }
    catch (err) {                  // send the error message if any error occurs
        res.send(err)
    }
}
// function to delete a recipe by its ID
var deleteRecipe = async function (req, res) {
    try {                                         //find the recipe by its Id and delete it
        const recipe = await Recipe.findByIdAndDelete(req.params.id)
        res.send(recipe)          // Send the deleted recipe as the response
    }
    catch (err) {                // Send error message if any error occurs
        res.send(err)
    }
}
// Export all the functions 
module.exports = {
    topRecipe,
    getRecipeList,
    findRecipe,
    addRecipe,
    updateRecipe,
    deleteRecipe
}

