// filename: user.js
// Student name: Rajveer Kaur Gill
// Student Id: 200546782
// Date: 15 November 2024

//Import the user model
var User = require("../models/User");
var jwt = require('jsonwebtoken');


// Function to create a new user
var createUser = async function (req, res) {
    try {
        // Create anew user using the request body data
        const user = new User(req.body)
        // Save the user to the database
        await user.save()
        // Send the newly created user back in the response
        res.send(user)
    } catch (err) {
        // If there is an error, send the error message
        res.send(err);
    }
}


var login = async function (req, res) {
    var username = req.body.username;
    var password = req.body.password;

    try {
        const user = await User.findOne({ username: username, password: password });

        if (user) {
            // Generate a JWT token
            const token = jwt.sign({ userId: user._id }, 'your_secret_key');
            res.send({ message: 'User logged in', token: token });
        } else {
            res.status(401).send('Invalid username or password');
        }
    } catch (err) {
        res.status(500).send(err);
    }
}





// Function to log out a user
var logout = function (req, res) {
    // Send a success message for logging out
    res.send('User logged out');
}
// Export the functions for use in other parts of the application
module.exports = {
    createUser,
    login,
    logout
}
