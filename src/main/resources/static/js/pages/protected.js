import {fetchProtectedData, fetchProtectedDataAdmin, fetchProtectedDataUser} from "../api/protected.js";

export function initProtectedPage() {
    console.log("Protected page initialized.");

    // Check if token is present in local storage
    const token = localStorage.getItem('token');
    if (!token) {
        // Redirect to login page if no token is found
        window.location.href = "login.html";
        return;
    }

    // Authenticated??


    // Fetch protected data
    fetchProtectedData().then(data => {
        // Display protected data on the page
        const protectedDataDiv = document.getElementById("protectedData");
        protectedDataDiv.textContent = JSON.stringify(data, null, 2);
    }).catch(error => {
        window.location.href = "login.html";
    });


    const protectedUserData = document.querySelector("#userData");
    fetchProtectedDataUser()
        .then(data => displayRoleSuccess(protectedUserData, true))
        .catch(e => displayRoleSuccess(protectedUserData, false))

    const protectedAdminData = document.querySelector("#adminData");
    fetchProtectedDataAdmin()
        .then(data => displayRoleSuccess(protectedAdminData, true))
        .catch(e => displayRoleSuccess(protectedAdminData, false))


}

function displayRoleSuccess(element, targetRole) {
    if (targetRole) {
        element.textContent = "YES"
        element.style.color = "green";
    } else {
        element.textContent = "NO"
        element.style.color = "red";
    }
}

