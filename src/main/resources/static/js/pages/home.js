import {getJwtToken, removeJwtToken} from "../utils/tokenUtil.js";


export function initHomePage() {
    // JWT present??
    const token = getJwtToken();
    const loginLink = document.getElementById("loginLink");
    const logoutLink = document.getElementById("logoutLink");
    if (!token) {
        // Show login button
        loginLink.classList.remove("hidden");
        logoutLink.classList.add("hidden");
    } else {
        // Show logout button
        loginLink.classList.add("hidden");
        logoutLink.classList.remove("hidden");
        // Add event listener to logout button
        logoutLink.addEventListener("click", (e) => {
            e.preventDefault();
            // Remove token from local storage
            removeJwtToken();
            // Redirect to login page
            window.location.href = "/";
        } );

    }
}