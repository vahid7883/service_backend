package com.numjew.service_backend.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private Jwt accessToken;
    private Jwt refreshToken;
}