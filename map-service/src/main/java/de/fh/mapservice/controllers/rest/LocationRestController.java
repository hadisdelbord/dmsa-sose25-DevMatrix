package de.fh.mapservice.controllers.rest;

import de.fh.mapservice.dtos.LocationAddressConvertDTO;
import de.fh.mapservice.dtos.LocationCreationDTO;
import de.fh.mapservice.dtos.LocationDTO;
import de.fh.mapservice.models.Location;
import de.fh.mapservice.repositories.LocationRepository;
import de.fh.mapservice.services.GeoLocationService;
import de.fh.mapservice.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class LocationRestController {

    @Autowired
    private LocationRepository locationRepository;

    private final GeoLocationService geoLocationService;
    private final LocationService locationService;

    public LocationRestController(GeoLocationService geoLocationService, LocationService locationService) {
        this.geoLocationService = geoLocationService;
        this.locationService = locationService;
    }

    @GetMapping(path = {"/locations"})
    public ResponseEntity<List<Location>> allLocations() {
        return new ResponseEntity<>(locationService.getAllLocations(), HttpStatus.OK);
    }

    @GetMapping(path = {"/locations/zipcode/{zipCode}"})
    public ResponseEntity<List<Location>> allLocationsByZipCode(@PathVariable int zipCode) {
        return new ResponseEntity<>(locationService.getAllLocations(zipCode), HttpStatus.OK);
    }

    @PostMapping(path = {"/convert/"})
    public ResponseEntity<LocationDTO> convert(@RequestBody LocationAddressConvertDTO locationAddressConvertDTO) {
        // 1. Convert RequestBody to query
        String query = geoLocationService.convertLocationAddressToQuery(locationAddressConvertDTO);

        // 2. Convert LocationAddressConvertDTO to LocationDTO
        LocationDTO locationDTO = geoLocationService.getLocationDTO(query);
        if (locationDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(locationDTO, HttpStatus.OK);
    }

    @PostMapping(path = {"/location/"})
    public ResponseEntity<Location> location(@RequestBody LocationCreationDTO locationCreationDTO) {
        // 1. Convert RequestBody to query
        String query = geoLocationService.convertLocationCreationToQuery(locationCreationDTO);

        // 2. Convert LocationCreationDTO to LocationDTO
        LocationDTO locationDTO = geoLocationService.getLocationDTO(query);
        if (locationDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 3. Convert LocationDTO to Location and save into database
        Location location = locationService.convertFromCreationDtoToModel(
                locationCreationDTO, locationDTO);

        // 4. Create location and respond
        // Return if exists
        return locationRepository.findByStationId(location.getStationId())
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(locationRepository.save(location), HttpStatus.OK));

    }

    @PutMapping("/location/station/{stationId}/")
    public ResponseEntity<?> updateLocationByStation(@PathVariable Long stationId, @RequestBody LocationCreationDTO locationCreationDTO) {
        return locationRepository.findByStationId(stationId).map(value -> {
            String query = geoLocationService.convertLocationCreationToQuery(locationCreationDTO);

            LocationDTO locationDTO = geoLocationService.getLocationDTO(query);
            if (locationDTO == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Location location = locationService.convertFromCreationDtoToModel(
                    locationCreationDTO, locationDTO);
            location.setId(value.getId());
            return new ResponseEntity<>(locationRepository.save(location), HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
