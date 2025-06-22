import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8081/api/OfferSlots',
  headers: {
    'Content-Type': 'application/json'
  }
});
api.interceptors.request.use((config) => {
  const user = JSON.parse(localStorage.getItem('user'));
  if (user && user.token) {
    config.headers.Authorization = `Bearer ${user.token}`;
  }
  return config;
});

export default api;
