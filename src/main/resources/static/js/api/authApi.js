
/*
* API functions for authentication
* This module sends a login request to the backend (username and password),
* saves the returned JWT to local storage,
* and redirects the user to index.html.
*
* It uses the post helper to call /api/auth/login and setJwtToken
* to persist the token.
*
* If login fails an alert is shown.
 */

import {post} from "../utils/fetchUtils.js";
import {setJwtToken} from "../utils/tokenUtil.js";

const LOGIN_URL = "/api/auth/login";

export default function performLogin(username, password) {
    const body = {username, password};
    post(LOGIN_URL, body)
        .then(data => {
            // Store token in local storage
            setJwtToken(data.token);
            // Redirect to index.html
            window.location.href = "index.html";
        })
        .catch(error => {
            console.error("Login failed:", error);
            alert("Login failed: " + error.message);
        });
}