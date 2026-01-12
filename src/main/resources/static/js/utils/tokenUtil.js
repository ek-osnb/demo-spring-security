/*
* Utility functions for managing JWT tokens.
* Basic implementation using localStorage for demo purposes.
* In production, consider more secure storage mechanisms.
*/

function setJwtToken(token) {
    localStorage.setItem('token', token);
}

function getJwtToken() {
    return localStorage.getItem('token');
}

function removeJwtToken() {
    localStorage.removeItem('token');
}

export { setJwtToken, getJwtToken, removeJwtToken };