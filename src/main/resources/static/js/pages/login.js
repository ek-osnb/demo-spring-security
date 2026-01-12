import performLogin from "../api/authApi.js";

export function initLoginPage() {
    console.log("Login page initialized");

    // Add event listener to login form
    const loginForm = document.querySelector("#loginForm");
    loginForm.addEventListener("submit", handleLoginSubmit);
}

async function handleLoginSubmit(event) {
    event.preventDefault();

    console.log("Login form submitted");

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    performLogin({ username, password });

}

