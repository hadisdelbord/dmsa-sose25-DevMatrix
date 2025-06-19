import axios from 'axios';

export default axios.create({
  baseURL: 'http://localhost:8081/api/Addresses',
  headers: {
    'Content-Type': 'application/json'
  }
});
