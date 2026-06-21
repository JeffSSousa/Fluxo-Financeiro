package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.user.UserCreateDTO;
import com.jeffssousa.fluxo.exception.business.EmailAlreadyExistsException;
import com.jeffssousa.fluxo.exception.handler.RestExceptionHandler;
import com.jeffssousa.fluxo.service.AuthService;
import com.jeffssousa.fluxo.service.UserService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(RestExceptionHandler.class)
public class AuthControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    UserService service;

    @MockitoBean
    AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class register{

        @Test
        @DisplayName("Deve registrar um novo usuario com sucesso")
        void shouldRegisterNewUserWithSuccess() throws Exception {


            String json = """
                    {
                      "email": "adm@email.com",
                      "password": "123",
                    
                      "name": "Victoria",
                      "lastName": "Vieira",
                      "birthDate": "2002-09-10"
                    }
                    """;

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            );

            verify(service, times(1)).register(any(UserCreateDTO.class));

            result
                    .andExpect(status().isCreated())
                    .andExpect(content().string(""));



        }

        @Test
        @DisplayName("Deve retornar conflito ao tentar cadastrar um cliente com e-mail já existente")
        void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {

            String json = """
                    {
                      "email": "adm@email.com",
                      "password": "123",
                    
                      "name": "Victoria",
                      "lastName": "Vieira",
                      "birthDate": "2002-09-10"
                    }
                    """;

            doThrow(new EmailAlreadyExistsException(
                    "Este e-mail já está vinculado a uma conta"))
                    .when(service)
                    .register(any(UserCreateDTO.class));


            mvc.perform(
                    MockMvcRequestBuilders
                            .post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            )
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message")
                            .value("Este e-mail já está vinculado a uma conta"));





        }



    }

}
