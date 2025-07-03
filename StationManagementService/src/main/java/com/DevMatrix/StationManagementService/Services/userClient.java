package com.DevMatrix.StationManagementService.Services;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.DevMatrix.StationManagementService.Dtos.UserResponse;



@Service
public class userClient {

    private final RestTemplate restTemplate;

    public userClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "getUserDataFallback")
    public UserResponse GetUserDataByEmail(String email) {
        String url = "http://USER-SERVICE/users/GetUserInfo?email=" + email;
        ResponseEntity<UserResponse> response = restTemplate.getForEntity(url, UserResponse.class);
        return response.getBody();
    }

    public UserResponse getUserDataFallback(String email, Throwable t) {
        UserResponse fallback = new UserResponse();
        fallback.setEmail(email);
        fallback.setName("Unavailable");
        return fallback;
    }
}
