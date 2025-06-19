import api from './station_api';

export default {
  getAll() {
    return api.get('GetAll');
  },
};
