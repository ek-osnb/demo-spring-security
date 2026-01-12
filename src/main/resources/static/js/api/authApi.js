
// url, body, headers
import {post} from "../utils/fetchUtils.js";
import {setJwtToken} from "../utils/token.js";

const LOGIN_URL = "/api/auth/login";

export default function performLogin(body) {
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