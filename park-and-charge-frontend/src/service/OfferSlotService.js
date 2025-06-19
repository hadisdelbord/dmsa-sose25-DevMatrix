import api from './offerSlot_api';

export default {
  getByStationIdAndDate(stationId, slotDate) {
    return api.get(`/GetOfferByStation/station/${stationId}`, {
      params: {  slotDate}
    });
  },
  saveSlots(slots) {
    return api.post('/CreateOrUpdate', slots);
  }
};
