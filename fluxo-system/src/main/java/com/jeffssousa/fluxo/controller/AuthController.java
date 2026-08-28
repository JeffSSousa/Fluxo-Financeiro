package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.login.LoginRequestDTO;
import com.jeffssousa.fluxo.dto.login.LoginResponseDTO;
import com.jeffssousa.fluxo.dto.user.UserCreateDTO;
import com.jeffssousa.fluxo.service.AuthService;
import com.jeffssousa.fluxo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Endpoints para autenticação.")
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final AuthService authService;

    @Operation(
            summary = "Registra novo usuario",
            description = "Recebe um DTO com dados de cadastro e cria usuario no sistema."
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreateDTO dto){

        userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Fazer Login",
            description = "Recebe um DTO com email e senha, e retorna um token para acesso."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request){

        LoginResponseDTO response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}
