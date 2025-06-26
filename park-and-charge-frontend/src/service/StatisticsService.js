import api from './statistics_api';

export default {
  // GET /bookings
  getAll() {
    return api.get('/bookings'); // No params!
  },

  // POST /bookings/filter
  filter({ stationIds, dateRange }) {
    return api.post('/bookings/filter', {
      stationIds,
      dateRange
    });
  }
};
