import axios from 'axios';

export default axios.create({
  baseURL: 'http://localhost:8085/api/bookings',
  headers: {
    'Content-Type': 'application/json'
  }
});
