import api from './statistics_api';

export default {
  // GET /bookings?ownerId=123
  getAll(ownerId) {
    return api.get('/bookings', {
      params: { ownerId }
    });
  },

  // POST /bookings/filter
  filter({ ownerId, stationIds, dateRange }) {
    return api.post('/bookings/filter', {
      ownerId,
      stationIds,
      dateRange
    });
  }
};
