package com.DevMatrix.StationManagementService.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.DevMatrix.StationManagementService.Dtos.OfferSlotDto;
import com.DevMatrix.StationManagementService.Dtos.UserResponse;

@Service
public class userClient {
    private final RestTemplate restTemplate;
    @Autowired
    public userClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserResponse GetUserDataByEmail(String email) {
    String url = "http://USER-SERVICE/users/GetUserInfo?email=" + email;
    ResponseEntity<UserResponse> response = restTemplate.getForEntity(url, UserResponse.class);
    return response.getBody();
    }
}
