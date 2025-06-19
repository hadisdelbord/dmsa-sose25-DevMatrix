package de.fh.mapservice.services;

import de.fh.mapservice.dtos.LocationCreationDTO;
import de.fh.mapservice.dtos.LocationDTO;
import de.fh.mapservice.models.Location;
import de.fh.mapservice.repositories.LocationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public Location convertFromDtoToModel(LocationCreationDTO locationCreationDTO, LocationDTO locationDTO) {
        // construct location from LocationCreationDTO
        Location location = modelMapper.map(locationCreationDTO, Location.class);

        // we set null to generate ID by database automatically
        location.setId(null);

        // add additional parameters from LocationDTO
        location.setLatitude(locationDTO.getLat());
        location.setLongitude(locationDTO.getLon());

        return location;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public List<Location> getAllLocations(int zipcode) {
        return locationRepository.findByZipCode(zipcode);
    }
}
