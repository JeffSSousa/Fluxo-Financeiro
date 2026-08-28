package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.user.AlterPasswordDTO;
import com.jeffssousa.fluxo.dto.user.ProfileResponseDTO;
import com.jeffssousa.fluxo.service.UserProfileService;
import com.jeffssousa.fluxo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuario", description = "Endpoints para gerenciamento de conta do usuario.")
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService profileService;

    private final UserService userService;

    @Operation(
            summary = "Busca perfil de usuario",
            description = "Retorna o perfil de usuario logado."
    )
    @GetMapping("/profile")
    public ResponseEntity<ProfileResponseDTO> getProfile(){
        return ResponseEntity.ok(profileService.getProfile());
    }

    @Operation(
            summary = "Alterar Senha",
            description = "Recebe um DTO com senha antiga e nova, depois realiza a alteração da senha."
    )
    @PutMapping("/password")
    public ResponseEntity<String> alterPassword(@Valid @RequestBody AlterPasswordDTO dto){
        String response = userService.alterPassword(dto);
        return ResponseEntity.ok(response);
    }

}
