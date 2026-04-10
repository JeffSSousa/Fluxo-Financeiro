package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.user.AlterPasswordDTO;
import com.jeffssousa.fluxo.dto.user.ProfileResponseDTO;
import com.jeffssousa.fluxo.service.UserProfileService;
import com.jeffssousa.fluxo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService profileService;

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponseDTO> getProfile(){
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PutMapping("/password")
    public ResponseEntity<String> alterPassword(@Valid @RequestBody AlterPasswordDTO dto){
        String response = userService.alterPassword(dto);
        return ResponseEntity.ok(response);
    }

}
