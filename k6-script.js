import http from 'k6/http';
import { check } from 'k6';

const hosts = [__ENV.APP_HOST_1, __ENV.APP_HOST_2];

export const options = {
    scenarios: {
        background_load: {
            executor: 'ramping-arrival-rate',
            startRate: 0,
            timeUnit: '1s',
            preAllocatedVUs: 50,
            maxVUs: 100,
            stages: [
                { duration: '2m', target: 40 },
                { duration: '1h', target: 40 },
            ],
        },
    },
};

export default function () {
    const host = hosts[Math.floor(Math.random() * hosts.length)];
    const baseUrl = `http://${host}:8080`;
    const action = Math.random();

    if (action < 0.25) {
        const res = http.post(`${baseUrl}/items?content=test-item`);
        check(res, { 'POST status is 201': (r) => r.status === 201 });
    }
    else if (action < 0.5) {
        const res = http.get(`${baseUrl}/items/1`);
        check(res, { 'GET status is 200': (r) => r.status === 200 });
    }
    else if (action < 0.75) {
        const res = http.put(`${baseUrl}/items/1?newContent=updated`);
        check(res, { 'PUT status is 200': (r) => r.status === 200 });
    }
    else if (action < 0.85) {
        const res = http.get(`${baseUrl}/items/-1`);
        check(res, { 'GET status is 404': (r) => r.status === 404 });
    }
    else if (action < 0.95) {
        const res = http.post(`${baseUrl}/items?content=2`);
        check(res, { 'POST status is 400': (r) => r.status === 400 });
    }
    else if (action < 0.97) {
        const res = http.post(`${baseUrl}/items?content=1`);
        check(res, { 'POST status is 503': (r) => r.status === 503 });
    }
    else if (action < 0.98) {
        const res = http.put(`${baseUrl}/items/1?newContent=1`);
        check(res, { 'PUT status is 503': (r) => r.status === 503 });
    }
    else {
        const res = http.put(`${baseUrl}/items/1?newContent=2`);
        check(res, { 'PUT status is 400': (r) => r.status === 400 });
    }
}