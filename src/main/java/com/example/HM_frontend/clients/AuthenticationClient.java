package com.example.HM_frontend.clients;


import com.example.HM_frontend.dtos.auth.AuthenticationRequest;
import com.example.HM_frontend.dtos.auth.AuthenticationResponse;
import com.example.HM_frontend.dtos.auth.RefreshTokenBodyDTO;
import com.example.HM_frontend.dtos.auth.RegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;

@FeignClient(name = "hm-authentication", url = "${backend.base-url}/auth")
public interface AuthenticationClient {

    @PostMapping("/register")
    ResponseEntity<AuthenticationResponse>  register(@RequestBody RegisterRequest request);

    @PostMapping("/authenticate")
    ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request);

    @PostMapping("/refresh-token")
    AuthenticationResponse refreshToken(@RequestBody RefreshTokenBodyDTO refreshTokenBody) throws IOException;

    @GetMapping("/logout")
    void logout(@RequestHeader("Authorization") String auth);

}
