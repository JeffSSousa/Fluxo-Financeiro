package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.transaction.TransactionDTO;
import com.jeffssousa.fluxo.exception.handler.RestExceptionHandler;
import com.jeffssousa.fluxo.service.TransactionService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@Import(RestExceptionHandler.class)
public class TransactionControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    TransactionService service;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class getTransactions{

        @Test
        @DisplayName("Deve retornar a lista de transações com sucesso")
        void shouldReturnTransactionListWithSuccess() throws Exception{

            TransactionDTO income = new TransactionDTO(
                    "Salario",
                    "RECEITA",
                    BigDecimal.valueOf(1200.0),
                    LocalDateTime.now().minusDays(10));

            TransactionDTO expense = new TransactionDTO(
                    "Aluguel",
                    "DESPESA",
                    BigDecimal.valueOf(1100.0),
                    LocalDateTime.now().minusDays(11));

            List<TransactionDTO> response = List.of(income, expense);

            when(service.findAll()).thenReturn(response);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/transactions")
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).findAll();

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(response.size()));

        }

    }

}
