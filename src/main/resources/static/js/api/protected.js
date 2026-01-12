
// Example of a protected API call using Fetch API

import {get} from "../utils/fetchUtils.js";
import {getJwtToken} from "../utils/token.js";

const PROTECTED_API_URL = "/api/protected";
const PROTECTED_API_URL_ADMIN = "/api/protected/admin";
const PROTECTED_API_URL_USER = "/api/protected/user";

export async function fetchProtectedData() {
    const token = getJwtToken();
    if (!token) {
        // Navigate to login page if no token is found
        window.location.href = "login.html";
        return;
    }

    const data = get(PROTECTED_API_URL, {"Authorization": `Bearer ${token}`});

    return data;
}

export async function fetchProtectedDataAdmin() {
    const token = getJwtToken();
    if (!token) {
        return;
    }

    const data = get(PROTECTED_API_URL_ADMIN, {"Authorization": `Bearer ${token}`});
    return data;
}

export async function fetchProtectedDataUser() {
    const token = getJwtToken();
    if (!token) {
        return;
    }

    const data = get(PROTECTED_API_URL_USER, {"Authorization": `Bearer ${token}`});
    return data;
}