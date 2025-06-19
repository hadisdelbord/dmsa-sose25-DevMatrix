import api from './location_api';

export default {
  getAll() {
    return api.get('GetAll');
  },
  saveLocation(location, isEdit) {
    const id = isEdit ? location.id : 0;
    return api.post(`/Update/isEdit/${isEdit}?id=${id}`, location);
  }
};
