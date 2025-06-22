import axios from 'axios';
import router from '@/router';

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
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response) {
      switch (error.response.status) {
        case 403: {
          alert('Access denied. You don\'t have permission to access this resource.');
          const user = JSON.parse(localStorage.getItem('user') || '{}');
          if (user.role === 'DRIVER') {
            router.push('/driver');
          } else if (user.role === 'OWNER') {
            router.push('/owner');
          } else {
            router.push('/login');
          }
          break;
        }
        
        case 401: {
          alert('Your session has expired. Please login again.');
          localStorage.removeItem('user');
          router.push('/login');
          break;
        }
        
        case 404: {
          alert('Resource not found.');
          break;
        }
        
        case 500: {
          alert('Server error. Please try again later.');
          break;
        }
        
        default: {
          alert('An error occurred: ' + (error.response.data?.message || error.message));
          break;
        }
      }
    } else {
      alert('Network error. Please check your connection.');
    }
    
    return Promise.reject(error);
  }
);

export default api;
