import api from './station_api';

export default {
  getAll() {
    return api.get('GetAll');
  },
  updateStation(station, isEdit) {
    const id = isEdit ? station.id : 0;
    return api.post(`/UpdateStation/isEdit/${isEdit}?id=${id}`, station);
  }
};
