package de.fh.mapservice.controllers.web;

import de.fh.mapservice.dtos.LocationDTO;
import de.fh.mapservice.services.GeoLocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/map")
public class LocationController {

    private final GeoLocationService geoLocationService;

    public LocationController(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }

    @GetMapping(path = {"/"})
    public String showMap() {
            return "map/show";
    }

    @GetMapping(path = {"/location/"})
    public ResponseEntity<LocationDTO> getLocation(@RequestParam String query) {
        return ResponseEntity.ok(geoLocationService.getLocationDTO(query));
    }

}
