import api from './booking_api';

export default {
  getAll() {
    return api.get();
  },
  create(booking) {
    return api.post('', booking);
  },
  getById(id) {
    return api.get(`/${id}`);
  },
  update(id, booking) {
    return api.put(`/${id}`, booking);
  },
  delete(id) {
    return api.delete(`/${id}`);
  },
  confirm(id) {
    return api.put(`/${id}/confirm`);
  },
  complete(id) {
    return api.put(`/${id}/complete`);
  },
  cancel(id) {
    return api.put(`/${id}/cancel`);
  },
  getByUserId(userId) {
    return api.get(`/user/${userId}`);
  },
  getByOfferIds(offerIds) {
    return api.post('/by-offer-ids', offerIds);
  },
};
