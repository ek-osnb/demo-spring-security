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