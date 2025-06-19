import axios from 'axios';

export default axios.create({
  baseURL: 'http://localhost:8081/api/ChargingStations',
  headers: {
    'Content-Type': 'application/json'
  }
});
