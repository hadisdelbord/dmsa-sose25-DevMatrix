import api from './offerSlot_api';

export default {
  getByStationIdAndDate(stationId, slotDate) {
    return api.get(`/GetOfferByStation/station/${stationId}`, {
      params: { slotDate }
    });
  },
  saveSlots(slots) {
    return api.post('/CreateOrUpdate', slots);
  },
  getAvailableOffers(postalCode) {
    return api.get(`/GetAvailableOffer/postalcode/${postalCode}`);
  },
  getOfferAndStationByOfferId(offerId) {
    return api.get(`/GetOfferById/OfferId/${offerId}`);
  },
  UpdateOfferSlot(offerId, dto) {
  return api.put(`/UpdateOffer/OfferId/${offerId}`, dto);
}

};
