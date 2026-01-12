/*
* This serves as the entry point for the frontend application.
*
* This file is referenced in every HTML page.
* It initializes page-specific logic based on the current URL path.
*
* It imports and calls initialization functions from page modules:
* - initHomePage for index.html
* - initLoginPage for login.html
* - initProtectedPage for protected.html
*
* Each page module sets up event listeners and UI logic specific to that page.
*/


import {initLoginPage} from "./pages/login.js";
import {initProtectedPage} from "./pages/protected.js";
import {initHomePage} from "./pages/home.js";

document.addEventListener("DOMContentLoaded", initRouteBasedLogic);

function initRouteBasedLogic() {
    if (window.location.pathname.endsWith("index.html") || window.location.pathname === "/") {
        initHomePage();
    }
    if (window.location.pathname.endsWith("login.html")) {
        initLoginPage();
    }
    if (window.location.pathname.endsWith("protected.html")) {
        initProtectedPage();
    }
}