import api from './map_api.js';


export default {
  getAllLocations() {
    return api.get("/locations")
  }
}
