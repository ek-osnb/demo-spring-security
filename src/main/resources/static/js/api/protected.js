
/**
 * Protected API usage demonstration.
 *
 * These calls must include a JWT in the Authorization header as a Bearer token.
 *
 * Example usage:
 *   const token = getJwtToken();
 *   get(PROTECTED_API_URL, { Authorization: `Bearer ${token}` });
 *
 * Security note:
 * - `getJwtToken()` reads from localStorage for demo convenience only. Avoid storing JWTs in localStorage in production.
 */

import {get} from "../utils/fetchUtils.js";
import {getJwtToken} from "../utils/tokenUtil.js";

const PROTECTED_API_URL = "/api/protected";
const PROTECTED_API_URL_ADMIN = "/api/protected/admin";
const PROTECTED_API_URL_USER = "/api/protected/user";

export async function retrieveProtectedMessage() {
    const token = getJwtToken();
    if (!token) {
        // Navigate to login page if no token is found
        window.location.href = "login.html";
        return;
    }

    const data = get(PROTECTED_API_URL, {"Authorization": `Bearer ${token}`});

    return data;
}

export async function retrieveProtectedMessageForAdminRole() {
    const token = getJwtToken();
    if (!token) {
        return;
    }

    const data = get(PROTECTED_API_URL_ADMIN, {"Authorization": `Bearer ${token}`});
    return data;
}

export async function retrieveProtectedMessageForUserRole() {
    const token = getJwtToken();
    if (!token) {
        return;
    }

    const data = get(PROTECTED_API_URL_USER, {"Authorization": `Bearer ${token}`});
    return data;
}