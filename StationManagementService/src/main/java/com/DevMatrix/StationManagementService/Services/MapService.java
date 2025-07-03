package com.DevMatrix.StationManagementService.Services;

import org.springframework.web.client.RestTemplate;

import com.DevMatrix.StationManagementService.Dtos.MapResponseDto;
import com.DevMatrix.StationManagementService.domain.entity.Address;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class MapService {

    private final RestTemplate restTemplate;
    public MapService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "mapServiceCircuitBreaker", fallbackMethod = "getMapDataFallback")
    public MapResponseDto getLocationFromAddress(Address address) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Address> requestEntity = new HttpEntity<Address>(address, headers);
        String url = "http://MAP-SERVICE/api/v1/convert/";
        return restTemplate
            .postForEntity(url, requestEntity, MapResponseDto.class)
            .getBody();
    }

    public MapResponseDto getUserDataFallback(double latitude, double longitude, Throwable t) {
        MapResponseDto fallback = new MapResponseDto();
        fallback.setLatitude(latitude);
        fallback.setLongitude(longitude);
        return fallback;
    }
}
