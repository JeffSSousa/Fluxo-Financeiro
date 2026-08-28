package com.jeffssousa.fluxo.controller;


import com.jeffssousa.fluxo.dto.summary.AccountSummaryDTO;
import com.jeffssousa.fluxo.dto.summary.MonthlySummaryDTO;
import com.jeffssousa.fluxo.dto.summary.YearlySummaryDTO;
import com.jeffssousa.fluxo.exception.handler.RestExceptionHandler;
import com.jeffssousa.fluxo.security.JwtAuthenticationFilter;
import com.jeffssousa.fluxo.service.AccountSummaryService;
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
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountSummaryController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RestExceptionHandler.class)
public class AccountSummaryControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    AccountSummaryService service;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Nested
    class getSummary{

        @Test
        @DisplayName("Deve retornar o resumo com sucesso")
        void ShouldReturnSummaryWithSuccess() throws Exception {

            AccountSummaryDTO response = new AccountSummaryDTO(
                    BigDecimal.valueOf(1200),
                    BigDecimal.valueOf(2000),
                    BigDecimal.valueOf(800)
            );

            when(service.getSummary()).thenReturn(response);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/summary/resume")
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).getSummary();

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalIncomes").value(response.totalIncomes()))
                    .andExpect(jsonPath("$.totalExpense").value(response.totalExpense()))
                    .andExpect(jsonPath("$.balance").value(response.balance()));

        }

    }


    @Nested
    class getYearlyFinancialSummary{

        @Test
        @DisplayName("Deve retornar o resumo de todos os meses do ano")
        void shouldReturnSummaryForAllMonthsOfTheYear() throws Exception {

            List<MonthlySummaryDTO> summaries = List.of(
                    new MonthlySummaryDTO("JAN", new BigDecimal("12500.50"), new BigDecimal("8300.20"), new BigDecimal("4200.30")),
                    new MonthlySummaryDTO("FEB", new BigDecimal("14200.75"), new BigDecimal("9100.40"), new BigDecimal("5100.35")),
                    new MonthlySummaryDTO("MAR", new BigDecimal("13800.00"), new BigDecimal("8700.15"), new BigDecimal("5099.85")),
                    new MonthlySummaryDTO("APR", new BigDecimal("15100.90"), new BigDecimal("9400.60"), new BigDecimal("5700.30")),
                    new MonthlySummaryDTO("MAY", new BigDecimal("16350.30"), new BigDecimal("10200.80"), new BigDecimal("6149.50")),
                    new MonthlySummaryDTO("JUN", new BigDecimal("14780.40"), new BigDecimal("8950.25"), new BigDecimal("5830.15")),
                    new MonthlySummaryDTO("JUL", new BigDecimal("17120.60"), new BigDecimal("10850.10"), new BigDecimal("6270.50")),
                    new MonthlySummaryDTO("AUG", new BigDecimal("16890.45"), new BigDecimal("10540.90"), new BigDecimal("6349.55")),
                    new MonthlySummaryDTO("SEP", new BigDecimal("15970.80"), new BigDecimal("9860.35"), new BigDecimal("6110.45")),
                    new MonthlySummaryDTO("OCT", new BigDecimal("17650.20"), new BigDecimal("11230.50"), new BigDecimal("6419.70")),
                    new MonthlySummaryDTO("NOV", new BigDecimal("18200.75"), new BigDecimal("11800.40"), new BigDecimal("6400.35")),
                    new MonthlySummaryDTO("DEC", new BigDecimal("19500.00"), new BigDecimal("12450.70"), new BigDecimal("7049.30"))
            );

            Integer currentYear = LocalDate.now().getYear();

            YearlySummaryDTO year2026 = new YearlySummaryDTO(
                    currentYear,
                    summaries
            );

            when(service.getYearlyFinancialSummary(null)).thenReturn(year2026);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/summary/yearly")
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).getYearlyFinancialSummary(null);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.year").value(currentYear))
                    .andExpect(jsonPath("$.months.length()").value(summaries.size()));

        }


        @Test
        @DisplayName("Deve retornar o resumo de todos os meses do ano selecionado")
        void shouldReturnSummaryForAllMonthsOfYearSelected() throws Exception {

            List<MonthlySummaryDTO> summaries = List.of(
                    new MonthlySummaryDTO("JAN", new BigDecimal("12500.50"), new BigDecimal("8300.20"), new BigDecimal("4200.30")),
                    new MonthlySummaryDTO("FEB", new BigDecimal("14200.75"), new BigDecimal("9100.40"), new BigDecimal("5100.35")),
                    new MonthlySummaryDTO("MAR", new BigDecimal("13800.00"), new BigDecimal("8700.15"), new BigDecimal("5099.85")),
                    new MonthlySummaryDTO("APR", new BigDecimal("15100.90"), new BigDecimal("9400.60"), new BigDecimal("5700.30")),
                    new MonthlySummaryDTO("MAY", new BigDecimal("16350.30"), new BigDecimal("10200.80"), new BigDecimal("6149.50")),
                    new MonthlySummaryDTO("JUN", new BigDecimal("14780.40"), new BigDecimal("8950.25"), new BigDecimal("5830.15")),
                    new MonthlySummaryDTO("JUL", new BigDecimal("17120.60"), new BigDecimal("10850.10"), new BigDecimal("6270.50")),
                    new MonthlySummaryDTO("AUG", new BigDecimal("16890.45"), new BigDecimal("10540.90"), new BigDecimal("6349.55")),
                    new MonthlySummaryDTO("SEP", new BigDecimal("15970.80"), new BigDecimal("9860.35"), new BigDecimal("6110.45")),
                    new MonthlySummaryDTO("OCT", new BigDecimal("17650.20"), new BigDecimal("11230.50"), new BigDecimal("6419.70")),
                    new MonthlySummaryDTO("NOV", new BigDecimal("18200.75"), new BigDecimal("11800.40"), new BigDecimal("6400.35")),
                    new MonthlySummaryDTO("DEC", new BigDecimal("19500.00"), new BigDecimal("12450.70"), new BigDecimal("7049.30"))
            );

            Integer year = 2024;

            YearlySummaryDTO year2024 = new YearlySummaryDTO(
                    year,
                    summaries
            );

            when(service.getYearlyFinancialSummary(year)).thenReturn(year2024);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/summary/yearly")
                            .param("year", "2024")
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).getYearlyFinancialSummary(year);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.year").value(year))
                    .andExpect(jsonPath("$.months.length()").value(summaries.size()));

        }

    }
}
