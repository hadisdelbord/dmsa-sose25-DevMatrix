package com.parkandcharge.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private Long userId;
    private String name;
    private String email;
    private String role;
}
