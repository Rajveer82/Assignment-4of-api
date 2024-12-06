// filename: User.js
// Student name: Rajveer Kaur Gill
// Student Id: 200546782
// Date: 15 November 2024
var mongoose = require("mongoose");
// Define the schema for the Recipe model
var schema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    username: {
        type: String,
        required: true
    },
    email: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true
    },
    contact: {
        type: Number,
        required: true
    }
});
module.exports = mongoose.model('User', schema);
