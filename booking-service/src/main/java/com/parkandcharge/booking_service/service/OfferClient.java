package com.parkandcharge.booking_service.service;

import com.parkandcharge.booking_service.dto.OfferSlotDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OfferClient {

    private final RestTemplate restTemplate;

    @Autowired
    public OfferClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OfferSlotDto getOfferById(Long offerId) {
        return restTemplate.getForObject(
                "http://STATIONMANAGEMENTSERVICE/api/offers/" + offerId,
                OfferSlotDto.class);
    }

    public List<OfferSlotDto> getOffers() {
        ResponseEntity<List<OfferSlotDto>> response = restTemplate.exchange(
                "http://STATIONMANAGEMENTSERVICE/api/OfferSlots/GetAll",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OfferSlotDto>>() {
                });
        return response.getBody();
    }
}
