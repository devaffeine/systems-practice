import { apiUrl, randomUser, signUpUser, signInUser, userProfile } from './common.js';

export const options = {
    stages: [
        { duration: '3m', target: 20 }, // simulate ramp-up of traffic from 1 to 20 users over 3 minutes.
        { duration: '5m', target: 20 }, // stay at 20 users for 5 minutes
        { duration: '3m', target: 50 }, // ramp-up to 50 users over 3 minutes
        { duration: '2m', target: 50 }, // stay at 50 users for short amount of time
        { duration: '3m', target: 20 }, // ramp-down to 20 users over 3 minutes
        { duration: '5m', target: 20 }, // continue at 60 for additional 5 minutes
        { duration: '5m', target: 0 }, // ramp-down to 0 users
    ],
    thresholds: {
        checks: ['rate>0.95'], // the rate of successful checks should be higher than 95%
        http_req_duration: [
            'p(90) < 1000', // 90% of requests must finish within 1s.
            'p(95) < 2000', // 95% of requests must finish within 2s.
            'p(99.9) < 2500', // 99.9% of requests must finish within 2.5s.
        ],
        http_req_failed: ['rate<0.05'], // http errors should be less than 5%
    },
};

export function setup() {
    console.log(`Running load tests on ${apiUrl('')}...`);
}

export default function () {
    const user = randomUser();
    signUpUser(user);
    for (let i = 0; i < 5; i++) {
        const token = signInUser(user);
        userProfile(token);
    }
}