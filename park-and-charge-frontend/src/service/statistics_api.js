import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/statistics',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add an interceptor to include JWT token on each request
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token'); // <-- your token key
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

export default api;
