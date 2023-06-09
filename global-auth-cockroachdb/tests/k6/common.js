import { check } from 'k6';
import { randomString } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';
import http from 'k6/http';

const baseUrl = __ENV.BASE_URL || 'http://localhost:8090';
export const apiUrl = (endpoint) => `${baseUrl}${endpoint}`;

export function randomUser() {
    return {
        username: "user-" + randomString(10),
        password: randomString(10),
        name: "User " + randomString(10),
    };
}

export function signUpUser(user) {
    const userJson = JSON.stringify(user);
    const resp = http.post(apiUrl('/sign-up'), userJson, jsonParams({}));
    check(resp, {
        'sign-up has status 201': (r) => r.status === 201,
        'sign-up has token': (r) => r.body && r.body.includes('token')
    });
    const jsonResp = resp.json();
    return `${jsonResp['type']} ${jsonResp['token']}`;
}

export function signInUser(user) {
    const userJson = JSON.stringify(user);
    const resp = http.post(apiUrl('/sign-in'), userJson, jsonParams({}));
    check(resp, {
        'sign-in has status 200': (r) => r.status === 200,
        'sign-in has token': (r) => r.body && r.body.includes('token')
    });
    const jsonResp = resp.json();
    return `${jsonResp['type']} ${jsonResp['token']}`;
}

export function userProfile(token) {
    const resp = http.get(apiUrl('/me'), jsonParams({
        'Authorization': token
    }));
    check(resp, {
        'me has status 200': (r) => r.status === 200,
        'me has name': (r) => r.body && r.body.includes('name'),
    });
}

const jsonParams = (headers = {}) => {
    const headersMap = {
        'Content-Type': 'application/json',
    };
    Object.assign(headersMap, headers);
    return {
        headers: headersMap,
    };
};
