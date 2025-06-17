package com.parkandcharge.payment_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.parkandcharge.payment_service.dto.OfferSlotDto;

@Service
public class OfferClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public OfferSlotDto getOfferById(Long offerId) {
        return restTemplate.getForObject(
            "http://STATIONMANAGEMENTSERVICE/api/offers/" + offerId,
            OfferSlotDto.class
        );
    }
}
