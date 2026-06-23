package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.category.CategoryResponseDTO;
import com.jeffssousa.fluxo.dto.income.IncomeRequestDTO;
import com.jeffssousa.fluxo.dto.income.IncomeResponseDTO;
import com.jeffssousa.fluxo.enums.CategoryType;
import com.jeffssousa.fluxo.enums.IncomeStatus;
import com.jeffssousa.fluxo.exception.business.TransactionNotFound;
import com.jeffssousa.fluxo.exception.business.UnauthorizedResourceAccessException;
import com.jeffssousa.fluxo.exception.handler.RestExceptionHandler;
import com.jeffssousa.fluxo.security.JwtAuthenticationFilter;
import com.jeffssousa.fluxo.service.IncomeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncomeController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RestExceptionHandler.class)
public class IncomeControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    IncomeService service;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Nested
    class addIncome{

        @Test
        @DisplayName("Deve criar uma receita com sucesso")
        void shouldCreateAnIncomeWithSuccess() throws Exception {

            IncomeRequestDTO request = new IncomeRequestDTO(
                    "Salário",
                    new BigDecimal("5000.00"),
                    LocalDateTime.now(),
                    IncomeStatus.RECEIVED,
                    "SALARIO"
            );

            String json = objectMapper.writeValueAsString(request);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/income")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            );

            verify(service, times(1)).addIncome(request);

            result
                    .andExpect(status().isCreated());

        }

        @Test
        @DisplayName("Deve retornar 400 Bad Request quando a requisição for inválida")
        void shouldReturn400BadRequestWhenRequestIsInvalid() throws Exception {

            IncomeRequestDTO request = new IncomeRequestDTO(
                    null,
                    new BigDecimal("5000.00"),
                    LocalDateTime.now(),
                    IncomeStatus.RECEIVED,
                    "SALARIO"
            );

            String json = objectMapper.writeValueAsString(request);


            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/income")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            );

            verify(service, never()).addIncome(request);

            result
                    .andExpect(status().isBadRequest());

        }

    }

    @Nested
    class getAll{

        @Test
        @DisplayName("Deve procurar todas as receitas com sucesso")
        void shouldFindAllIncomesWithSuccess() throws Exception {

            IncomeResponseDTO income1 = new IncomeResponseDTO(
                    UUID.randomUUID(),
                    "Salário Junho",
                    new BigDecimal("5000.00"),
                    LocalDateTime.of(2026, 6, 1, 9, 0),
                    IncomeStatus.RECEIVED,
                    new CategoryResponseDTO(
                            1L,
                            "SALARIO",
                            CategoryType.INCOME
                    )
            );

            IncomeResponseDTO income2 = new IncomeResponseDTO(
                    UUID.randomUUID(),
                    "Projeto Freelancer",
                    new BigDecimal("1500.00"),
                    LocalDateTime.of(2026, 6, 15, 14, 30),
                    IncomeStatus.PENDING,
                    new CategoryResponseDTO(
                            2L,
                            "EXTRA",
                            CategoryType.INCOME
                    )
            );

            List<IncomeResponseDTO> response = List.of(income1, income2);

            when(service.getAll()).thenReturn(response);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/income")
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).getAll();

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(response.size()));


        }

    }

    @Nested
    class getIncomeById{

        @Test
        @DisplayName("Deve buscar uma receita pelo ID com sucesso")
        void shouldFindIncomeByIdWithSuccess() throws Exception {

            UUID id = UUID.randomUUID();

            IncomeResponseDTO response = new IncomeResponseDTO(
                    UUID.randomUUID(),
                    "Salário Junho",
                    new BigDecimal("5000.0"),
                    LocalDateTime.of(2026, 6, 1, 9, 0),
                    IncomeStatus.RECEIVED,
                    new CategoryResponseDTO(
                            1L,
                            "SALARIO",
                            CategoryType.INCOME
                    )
            );

            when(service.getById(id)).thenReturn(response);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/income/" + id.toString())
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).getById(id);

            String expectedDate = response.transactionDate()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.incomeId").value(response.incomeId().toString()))
                    .andExpect(jsonPath("$.description").value(response.description()))
                    .andExpect(jsonPath("$.amount").value(response.amount()))
                    .andExpect(jsonPath("$.transactionDate").value(expectedDate))
                    .andExpect(jsonPath("$.status").value(response.status().name()))
                    .andExpect(jsonPath("$.category.categoryId").value(response.category().categoryId()))
                    .andExpect(jsonPath("$.category.name").value(response.category().name()))
                    .andExpect(jsonPath("$.category.type").value(response.category().type().name()));

        }

        @Test
        @DisplayName("Deve retornar 404 - Not Found quando não encontrar a receita")
        void shouldReturn404WhenIncomeNotFound() throws Exception {

            UUID id = UUID.randomUUID();

            when(service.getById(id))
                    .thenThrow(new TransactionNotFound("Receita não encontrada!"));

            mvc.perform(
                            get("/income/" + id.toString())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound()
                    );

            verify(service, times(1)).getById(id);
        }

        @Test
        @DisplayName("Deve retornar 403 - Forbidden quando não tiver autorização para acessar")
        void shouldReturn403WhenUserDoesNotHavePermission() throws Exception {

            UUID id = UUID.randomUUID();

            when(service.getById(id))
                    .thenThrow(new UnauthorizedResourceAccessException("Você não pode acessar essa transação"));

            mvc.perform(
                            get("/income/" + id.toString())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden()
                    );

            verify(service, times(1)).getById(id);

        }

    }

    @Nested
    class deleteById{

        @Test
        @DisplayName("Deve deletar um receita pelo ID com sucesso")
        void shouldDeleteIncomeByIdWithSuccess() throws Exception {

            UUID id = UUID.randomUUID();

            ResultActions result = mvc.perform(
                    delete("/income/" + id.toString())
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).deleteById(id);

            result
                    .andExpect(status().isNoContent());

        }

        @Test
        @DisplayName("Deve retornar 404 - Not Found quando não encontrar a despesa")
        void shouldReturn404WhenIncomeNotFound() throws Exception {

            UUID id = UUID.randomUUID();

            doThrow(new TransactionNotFound("Receita não encontrada!"))
                    .when(service)
                    .deleteById(id);

            mvc.perform(
                            delete("/income/" + id.toString())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound()
                    );

            verify(service, times(1)).deleteById(id);

        }

        @Test
        @DisplayName("Deve retornar 403 - Forbidden quando não tiver autorização para acessar")
        void shouldReturn403WhenUserDoesNotHavePermission() throws Exception {

            UUID id = UUID.randomUUID();

            doThrow(new UnauthorizedResourceAccessException("Você não pode acessar essa transação"))
                    .when(service)
                    .deleteById(id);

            mvc.perform(
                            delete("/income/" + id.toString())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden()
                    );

            verify(service, times(1)).deleteById(id);


        }

    }

    @Nested
    class updateById{

        @Test
        @DisplayName("Deve Atualizar um receita pelo ID com sucesso")
        void shouldUpdateIncomeByIdWithSuccess() throws Exception {

            UUID id = UUID.randomUUID();

            IncomeResponseDTO response = new IncomeResponseDTO(
                    UUID.randomUUID(),
                    "Salário Junho",
                    new BigDecimal("5000.0"),
                    LocalDateTime.of(2026, 6, 1, 9, 0),
                    IncomeStatus.RECEIVED,
                    new CategoryResponseDTO(
                            1L,
                            "SALARIO",
                            CategoryType.INCOME
                    )
            );

            IncomeRequestDTO request = new IncomeRequestDTO(
                    response.description(),
                    response.amount(),
                    response.transactionDate(),
                    response.status(),
                    response.category().name()
            );

            String json = objectMapper.writeValueAsString(request);

            when(service.updateById(id, request)).thenReturn(response);

            ResultActions result = mvc.perform(
                    put("/income/" + id.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            );


            verify(service, times(1)).updateById(id, request);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.incomeId").value(response.incomeId().toString()))
                    .andExpect(jsonPath("$.description").value(response.description()))
                    .andExpect(jsonPath("$.amount").value(response.amount()))
                    .andExpect(jsonPath("$.transactionDate").value(response.transactionDate()
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                    .andExpect(jsonPath("$.status").value(response.status().name()))
                    .andExpect(jsonPath("$.category.categoryId").value(response.category().categoryId()))
                    .andExpect(jsonPath("$.category.name").value(response.category().name()))
                    .andExpect(jsonPath("$.category.type").value(response.category().type().name()));
        }

        @Test
        @DisplayName("Deve retornar 404 - Not Found quando não encontrar a receita")
        void shouldReturn404WhenIncomeNotFound() throws Exception {

            UUID id = UUID.randomUUID();

            IncomeRequestDTO request = new IncomeRequestDTO(
                    "Salário",
                    new BigDecimal("5000.00"),
                    LocalDateTime.now(),
                    IncomeStatus.RECEIVED,
                    "SALARIO"
            );

            String json = objectMapper.writeValueAsString(request);

            doThrow(new TransactionNotFound("Receita não encontrada!"))
                    .when(service)
                    .updateById(id, request);

            mvc.perform(
                            put("/income/" + id.toString())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                    .andExpect(status().isNotFound()
                    );

            verify(service, times(1)).updateById(id,request);

        }

        @Test
        @DisplayName("Deve retornar 403 - Forbidden quando não tiver autorização para acessar")
        void shouldReturn403WhenUserDoesNotHavePermission() throws Exception {

            UUID id = UUID.randomUUID();

            IncomeRequestDTO request = new IncomeRequestDTO(
                    "Salário",
                    new BigDecimal("5000.00"),
                    LocalDateTime.now(),
                    IncomeStatus.RECEIVED,
                    "SALARIO"
            );

            String json = objectMapper.writeValueAsString(request);

            doThrow(new UnauthorizedResourceAccessException("Você não pode acessar essa transação"))
                    .when(service)
                    .updateById(id, request);

            mvc.perform(
                            put("/income/" + id.toString())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                    .andExpect(status().isForbidden()
                    );

            verify(service, times(1)).updateById(id,request);


        }

    }


}
