import api from './station_api';

export default {
  getAll() {
    return api.get('/GetAll');
  },
  getAllStationsForMap() {
    return api.get('/getAllStationsForMap');
  },
  updateStation(station, isEdit) {
    const id = isEdit ? station.id : 0;
    return api.post(`/UpdateStation/isEdit/${isEdit}?id=${id}`, station);
  },
  getStationsByOwnerId(userId) {
    return api.get(`/GetByUserId`, {
      params: { userId }
    });
  }
};
