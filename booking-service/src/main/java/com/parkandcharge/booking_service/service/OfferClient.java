package com.parkandcharge.booking_service.service;

import com.parkandcharge.booking_service.dto.OfferSlotDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OfferClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public OfferSlotDto getOfferById(Long offerId) {
        return restTemplate.getForObject(
            "http://localhost:8083/api/offers/" + offerId,
            OfferSlotDto.class
        );
    }
}
