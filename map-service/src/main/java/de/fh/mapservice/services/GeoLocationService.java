package de.fh.mapservice.services;

import de.fh.mapservice.dtos.LocationCreationDTO;
import de.fh.mapservice.dtos.LocationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class GeoLocationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org/search?format=json&q=";

    public LocationDTO getLocationDTO(String address) {
        String url = NOMINATIM_BASE_URL + address;
        ResponseEntity<LocationDTO[]> response = restTemplate.getForEntity(url, LocationDTO[].class);
        if (response.getStatusCode() == HttpStatus.OK && Objects.requireNonNull(response.getBody()).length > 0) {
            return response.getBody()[0];
        }
        return null;
    }

    public String convertLocationCreationToQuery(LocationCreationDTO locationCreationDTO) {
        // <Street> <Building number>, <Zip code> <City>, <State/Country>
        return String.format("%s, %d %s, %s",
                locationCreationDTO.getStreet(),
                locationCreationDTO.getZipCode(),
                locationCreationDTO.getCity(),
                locationCreationDTO.getState()
        );
    }

}
