// filename: routes.js
// Student name: Rajveer Kaur Gill
// Student Id: 200546782
// Date: 15 November 2024

var { authenticateToken } = require('./middleware/authenticateToken'); // Import middleware

var express = require('express');
var { topRecipe, getRecipeList, findRecipe, addRecipe, updateRecipe, deleteRecipe } = require("./controller/recipe");
var router = express.Router();
const { createUser, login,logout } = require('./controller/user');
// Set up the route
var setRoute = function (){
    router.get('/getTopRecipe', topRecipe)   // Fetches the top recipe
    router.get('/recipe/list',  getRecipeList)  //Retrieves a list of all recipes
    router.get('/recipe/find/:id',authenticateToken, findRecipe)  // Finds a recipe by its ID
    router.post('/recipe/add', authenticateToken,addRecipe)    //Adds a new recipe
    router.put('/recipe/update/:id',authenticateToken, updateRecipe)     //Updates an existing recipe by Id
    router.delete('/recipe/delete/:id', deleteRecipe)   // Deletes a recipe by id

    
    // user routes
    router.post('/user/create', createUser)      //Creates a new user
    router.post('/user/login', login)           //Logs in a user
    router.get('/user/logout', logout)          //Logs out a user
    return router
}
module.exports = {setRoute}
