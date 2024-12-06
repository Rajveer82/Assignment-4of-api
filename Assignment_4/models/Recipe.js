// filename: Recipe.js
// Student name: Rajveer Kaur Gill
// Student Id: 200546782
// Date: 15 November 2024
var mongoose = require("mongoose");
// Define the schema for the Recipe model
var schema = new mongoose.Schema({
    recipeName: {
        type: String
    },
    description: {
        type: String
    },
    averageRating: {
        type: Number
    },
    cookingTime: {
        type: Number
    },
    difficulty: {
        type: String
    },
    cuisine: {
        type: String
    },
    photoLink: {
        type: String
    },
    ingredients: {
        type: [String]
    },
});
module.exports = mongoose.model('Recipe', schema);
