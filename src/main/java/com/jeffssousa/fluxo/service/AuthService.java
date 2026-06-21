package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.login.LoginRequestDTO;
import com.jeffssousa.fluxo.dto.login.LoginResponseDTO;
import com.jeffssousa.fluxo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO request){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String token = jwtService.generateToken(authentication);

        return new LoginResponseDTO(
                token,
                "Bearer"
        );
    }

}
