// filename: database.js
// Student name: Rajveer Kaur Gill
// Student Id: 200546782
// Date: 15 November 2024
var mongoose = require('mongoose');
var databaseConnection = async function (){
    var connectionString = process.env.dbConnectionString || 'mongodb+srv://Rajveer:Rajjo123@cluster0.jdl76.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0';
    try {
        console.log("Connecting to mongoDB...");
        await mongoose.connect(connectionString);
        console.log("Connected to mongoDB...");
    } catch (err) {
        console.error("Error connecting to mongoDB: ", err);
        process.exit(1);
    }
};
module.exports = {databaseConnection};
