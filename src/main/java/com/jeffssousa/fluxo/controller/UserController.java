package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.ProfileResponseDTO;
import com.jeffssousa.fluxo.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService service;

    @GetMapping("profile")
    public ResponseEntity<ProfileResponseDTO> getProfile(){
        return ResponseEntity.ok(service.getProfile());
    }

}
