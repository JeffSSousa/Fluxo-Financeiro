package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.user.AlterPasswordDTO;
import com.jeffssousa.fluxo.dto.user.ProfileResponseDTO;
import com.jeffssousa.fluxo.exception.business.InvalidPasswordException;
import com.jeffssousa.fluxo.exception.business.PasswordMismatchException;
import com.jeffssousa.fluxo.exception.handler.RestExceptionHandler;
import com.jeffssousa.fluxo.service.UserProfileService;
import com.jeffssousa.fluxo.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(RestExceptionHandler.class)
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    UserProfileService profileService;

    @MockitoBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    @Nested
    class getProfile{

        @Test
        @DisplayName("Deve retornar um perfil de usuario com sucesso")
        void shouldReturnAUserProfileWithSuccess() throws Exception {

            ProfileResponseDTO response = new ProfileResponseDTO(
                    "Victoria",
                    "Vieira",
                    LocalDate.of(1998,10,5)
            );

            when(profileService.getProfile()).thenReturn(response);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/user/profile")
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(profileService, times(1)).getProfile();

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(response.name()))
                    .andExpect(jsonPath("$.lastName").value(response.lastName()))
                    .andExpect(jsonPath("$.birthDate").value(response.birthDate().toString()));

        }

        @Test
        @DisplayName("Deve retornar 404 quando não encontrar o perfil")
        void shouldReturn404WhenProfileNotFound() throws Exception {

            when(profileService.getProfile())
                    .thenThrow(new EntityNotFoundException("Perfil não encontrado"));

            mvc.perform(
                    MockMvcRequestBuilders
                            .get("/user/profile"))
                    .andExpect(status().isNotFound());

            verify(profileService, times(1)).getProfile();

        }

    }


    @Nested
    class alterPassword{

        @Test
        @DisplayName("Deve alterar a senha com sucesso")
        void shouldAlterPasswordWithSuccess() throws Exception {

            AlterPasswordDTO request = new AlterPasswordDTO(
                    "1234567",
                    "12345",
                    "12345"
            );

            String json = objectMapper.writeValueAsString(request);

            String response = "Senha alterada com sucesso";

            when(userService.alterPassword(request))
                    .thenReturn(response);


            ResultActions result = mvc.perform(
                    put("/user/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            );

            verify(userService, times(1)).alterPassword(request);

            result
                    .andExpect(status().isOk())
                    .andExpect(content().string(response));

        }

        @Test
        @DisplayName("Deve retornar 401 - Unauthorized quando a senha estiver invalida")
        void shouldReturn401UnauthorizedWhenPasswordIsInvalid() throws Exception {

            AlterPasswordDTO request = new AlterPasswordDTO(
                    "1234567",
                    "12345",
                    "12345"
            );

            String json = objectMapper.writeValueAsString(request);

            when(userService.alterPassword(request))
                    .thenThrow(new InvalidPasswordException("Senha invalida!"));

            mvc.perform(
                    put("/user/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isUnauthorized()
                    );

            verify(userService, times(1)).alterPassword(request);

        }

        @Test
        @DisplayName("Deve retornar 409 - Conflict quando as senhas não coincidem")
        void shouldReturn409ConflictWhenPasswordsDoNotMatch() throws Exception {

            AlterPasswordDTO request = new AlterPasswordDTO(
                    "1234567",
                    "1234",
                    "12345"
            );

            String json = objectMapper.writeValueAsString(request);

            when(userService.alterPassword(request))
                    .thenThrow(new PasswordMismatchException("As senhas não coincidem"));

            mvc.perform(
                    put("/user/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isConflict()
                    );

            verify(userService, times(1)).alterPassword(request);

        }

    }

}
