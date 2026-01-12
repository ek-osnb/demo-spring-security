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