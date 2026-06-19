package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.category.CategoryResponseDTO;
import com.jeffssousa.fluxo.dto.expense.ExpenseRequestDTO;
import com.jeffssousa.fluxo.dto.expense.ExpenseResponseDTO;
import com.jeffssousa.fluxo.enums.CategoryType;
import com.jeffssousa.fluxo.enums.ExpenseStatus;
import com.jeffssousa.fluxo.exception.business.TransactionNotFound;
import com.jeffssousa.fluxo.exception.business.UnauthorizedResourceAccessException;
import com.jeffssousa.fluxo.exception.handler.RestExceptionHandler;
import com.jeffssousa.fluxo.service.ExpenseService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
@Import(RestExceptionHandler.class)
public class ExpenseControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    ExpenseService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class addExpense{

        @Test
        @DisplayName("Deve criar uma despesa com sucesso")
        void shouldCreateAExpenseWithSuccess() throws Exception {

            ExpenseRequestDTO request = new ExpenseRequestDTO(
                    "Aluguel",
                    BigDecimal.valueOf(1200.0),
                    LocalDateTime.now().plusDays(2),
                    LocalDate.now().plusDays(5),
                    ExpenseStatus.NOT_PAID,
                    "Casa");

            String json = objectMapper.writeValueAsString(request);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/expense")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            );

            verify(service, times(1)).addExpense(request);

            result
                    .andExpect(status().isCreated());

        }

        @Test
        @DisplayName("Deve retornar 400 Bad Request quando a requisição for inválida")
        void shouldReturn400BadRequestWhenRequestIsInvalid() throws Exception {

            ExpenseRequestDTO request = new ExpenseRequestDTO(
                    null,
                    BigDecimal.valueOf(1200.0),
                    LocalDateTime.now().plusDays(2),
                    LocalDate.now().plusDays(5),
                    ExpenseStatus.NOT_PAID,
                    "Casa");

            String json = objectMapper.writeValueAsString(request);

            verify(service, never()).addExpense(request);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/expense")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            );


            result
                    .andExpect(status().isBadRequest());

        }

    }

    @Nested
    class getAll{

        @Test
        @DisplayName("Deve procurar todas as desepesas com sucesso")
        void shouldFindAllExpensesWithSuccess() throws Exception {

            ExpenseResponseDTO expense1 = new ExpenseResponseDTO(
                    UUID.randomUUID(),
                    "Internet Bill",
                    new BigDecimal("99.90"),
                    LocalDateTime.of(2026, 6, 18, 10, 30),
                    LocalDate.of(2026, 6, 25),
                    ExpenseStatus.NOT_PAID,
                    new CategoryResponseDTO(
                            1L,
                            "Utilities",
                            CategoryType.EXPENSE
                    )
            );

            ExpenseResponseDTO expense2 = new ExpenseResponseDTO(
                    UUID.randomUUID(),
                    "Gym Membership",
                    new BigDecimal("120.00"),
                    LocalDateTime.of(2026, 6, 17, 14, 15),
                    LocalDate.of(2026, 6, 20),
                    ExpenseStatus.PAID,
                    new CategoryResponseDTO(
                            2L,
                            "Health",
                            CategoryType.EXPENSE
                    )
            );

            List<ExpenseResponseDTO> response = List.of(expense1, expense2);

            when(service.getAll()).thenReturn(response);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/expense")
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).getAll();

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(response.size()));


        }

    }

    @Nested
    class getExpenseById{

        @Test
        @DisplayName("Deve buscar uma despesa pelo ID com sucesso")
        void shouldFindExpenseByIdWithSuccess() throws Exception {

            UUID id = UUID.randomUUID();

            ExpenseResponseDTO response = new ExpenseResponseDTO(
                    id,
                    "Internet Bill",
                    new BigDecimal("99.9"),
                    LocalDateTime.of(2026, 6, 18, 10, 30),
                    LocalDate.of(2026, 6, 25),
                    ExpenseStatus.NOT_PAID,
                    new CategoryResponseDTO(
                            1L,
                            "Utilities",
                            CategoryType.EXPENSE
                    )
            );


            when(service.getById(id)).thenReturn(response);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/expense/" + id.toString())
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).getById(id);

            String expectedDate = response.transactionDate()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.expenseId").value(response.expenseId().toString()))
                    .andExpect(jsonPath("$.description").value(response.description()))
                    .andExpect(jsonPath("$.amount").value(response.amount()))
                    .andExpect(jsonPath("$.transactionDate").value(expectedDate))
                    .andExpect(jsonPath("$.dueDate").value(response.dueDate().toString()))
                    .andExpect(jsonPath("$.status").value(response.status().name()))
                    .andExpect(jsonPath("$.category.categoryId").value(response.category().categoryId()))
                    .andExpect(jsonPath("$.category.name").value(response.category().name()))
                    .andExpect(jsonPath("$.category.type").value(response.category().type().name()));

        }

        @Test
        @DisplayName("Deve retornar 404 - Not Found quando não encontrar a despesa")
        void shouldReturn404WhenExpenseNotFound() throws Exception {

            UUID id = UUID.randomUUID();

            when(service.getById(id))
                    .thenThrow(new TransactionNotFound("Despesa não encontrada!"));

            mvc.perform(
                    get("/expense/" + id.toString())
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
                            get("/expense/" + id.toString())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden()
                    );

            verify(service, times(1)).getById(id);

        }

    }

    @Nested
    class deleteById{

        @Test
        @DisplayName("Deve deletar um despesa pelo ID com sucesso")
        void shouldDeleteExpenseByIdWithSuccess() throws Exception {

            UUID id = UUID.randomUUID();

            ResultActions result = mvc.perform(
                    delete("/expense/" + id.toString())
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).deleteById(id);

            result
                    .andExpect(status().isNoContent());

        }

        @Test
        @DisplayName("Deve retornar 404 - Not Found quando não encontrar a despesa")
        void shouldReturn404WhenExpenseNotFound() throws Exception {

            UUID id = UUID.randomUUID();

            doThrow(new TransactionNotFound("Despesa não encontrada!"))
                    .when(service)
                    .deleteById(id);

            mvc.perform(
                    delete("/expense/" + id.toString())
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
                            delete("/expense/" + id.toString())
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden()
                    );

            verify(service, times(1)).deleteById(id);


        }

    }

    @Nested
    class updateById{

        @Test
        @DisplayName("Deve Atualizar um despesa pelo ID com sucesso")
        void shouldUpdateExpenseByIdWithSuccess() throws Exception {


            UUID id = UUID.randomUUID();


            ExpenseResponseDTO response = new ExpenseResponseDTO(
                    id,
                    "Internet Bill",
                    new BigDecimal("99.9"),
                    LocalDateTime.of(2026, 6, 18, 10, 30),
                    LocalDate.of(2026, 6, 25),
                    ExpenseStatus.NOT_PAID,
                    new CategoryResponseDTO(
                            1L,
                            "Utilities",
                            CategoryType.EXPENSE
                    )
            );

            ExpenseRequestDTO request = new ExpenseRequestDTO(
                    response.description(),
                    response.amount(),
                    response.transactionDate(),
                    response.dueDate(),
                    response.status(),
                    response.category().name()
            );

            String json = objectMapper.writeValueAsString(request);

            when(service.updateById(id, request)).thenReturn(response);

            ResultActions result = mvc.perform(
                    put("/expense/" + id.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            );


            verify(service, times(1)).updateById(id, request);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.expenseId").value(response.expenseId().toString()))
                    .andExpect(jsonPath("$.description").value(response.description()))
                    .andExpect(jsonPath("$.amount").value(response.amount()))
                    .andExpect(jsonPath("$.transactionDate").value(response.transactionDate()
                                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                    .andExpect(jsonPath("$.dueDate").value(response.dueDate().toString()))
                    .andExpect(jsonPath("$.status").value(response.status().name()))
                    .andExpect(jsonPath("$.category.categoryId").value(response.category().categoryId()))
                    .andExpect(jsonPath("$.category.name").value(response.category().name()))
                    .andExpect(jsonPath("$.category.type").value(response.category().type().name()));
        }

        @Test
        @DisplayName("Deve retornar 404 - Not Found quando não encontrar a despesa")
        void shouldReturn404WhenExpenseNotFound() throws Exception {

            UUID id = UUID.randomUUID();

            ExpenseRequestDTO request = new ExpenseRequestDTO(
                    "Internet Bill",
                    new BigDecimal("99.9"),
                    LocalDateTime.of(2026, 6, 18, 10, 30),
                    LocalDate.of(2026, 6, 25),
                    ExpenseStatus.NOT_PAID,
                    "Utilities"
            );

            String json = objectMapper.writeValueAsString(request);

            doThrow(new TransactionNotFound("Despesa não encontrada!"))
                    .when(service)
                    .updateById(id, request);

            mvc.perform(
                            put("/expense/" + id.toString())
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

            ExpenseRequestDTO request = new ExpenseRequestDTO(
                    "Internet Bill",
                    new BigDecimal("99.9"),
                    LocalDateTime.of(2026, 6, 18, 10, 30),
                    LocalDate.of(2026, 6, 25),
                    ExpenseStatus.NOT_PAID,
                    "Utilities"
            );

            String json = objectMapper.writeValueAsString(request);

            doThrow(new UnauthorizedResourceAccessException("Você não pode acessar essa transação"))
                    .when(service)
                    .updateById(id, request);

            mvc.perform(
                            put("/expense/" + id.toString())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                    .andExpect(status().isForbidden()
                    );

            verify(service, times(1)).updateById(id,request);


        }

    }

    @Nested
    class getUpcoming15Expenses{

        @Test
        @DisplayName("Deve retornar as 15 depesas futuras com sucesso")
        void shouldReturnUpcoming15ExpensesWithSuccess() throws Exception {

            ExpenseResponseDTO expense1 = new ExpenseResponseDTO(
                    UUID.randomUUID(),
                    "Internet Bill",
                    new BigDecimal("99.90"),
                    LocalDateTime.of(2026, 6, 18, 10, 30),
                    LocalDate.now().plusDays(5),
                    ExpenseStatus.NOT_PAID,
                    new CategoryResponseDTO(
                            1L,
                            "Utilities",
                            CategoryType.EXPENSE
                    )
            );

            List<ExpenseResponseDTO> response = List.of(expense1);

            when(service.getUpcoming15Expenses()).thenReturn(response);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/expense/upcoming-expenses")
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).getUpcoming15Expenses();

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(response.size()));
        }

    }


}
