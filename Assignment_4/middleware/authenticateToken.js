// Filename: authenticateToken.js
// Student name: Rajveer Kaur Gill
// Student Id: 200546782
// Date: 15 November 2024
// middleware/authenticateToken.js

const jwt = require('jsonwebtoken');

// Middleware function to verify JWT token
const authenticateToken = (req, res, next) => {
    // Retrieve token from the 'Authorization' header
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1]; // Bearer <token>

    if (!token) {
        return res.status(403).json({ message: 'Token is required' });
    }

    // Verify the token
    jwt.verify(token, 'your_secret_key', (err, decoded) => {
        if (err) {
            return res.status(403).json({ message: 'Invalid token' });
        }
        // Store the decoded user ID in the request object for further use
        req.userId = decoded.userId;
        next();
    });
};

module.exports = { authenticateToken };
