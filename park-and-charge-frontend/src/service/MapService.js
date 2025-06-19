import api from './map_api.js';


export default {
  getAllLocations() {
    return api.get("/locations")
  },
  getAllLocationsByZipCode(zipcode) {
    return api.get('/locations/zipcode/' + zipcode)
  }
}
