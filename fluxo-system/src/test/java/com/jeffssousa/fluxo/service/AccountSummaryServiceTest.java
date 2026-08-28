package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.builders.UserTestBuilder;
import com.jeffssousa.fluxo.dto.summary.AccountSummaryDTO;
import com.jeffssousa.fluxo.dto.summary.MonthlySummaryDTO;
import com.jeffssousa.fluxo.dto.summary.YearlySummaryDTO;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.repository.ExpenseRepository;
import com.jeffssousa.fluxo.repository.IncomeRepository;
import com.jeffssousa.fluxo.repository.projection.MonthlyAmountProjection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountSummaryServiceTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CurrentUserService userService;

    @InjectMocks
    private AccountSummaryService service;


    @Nested
    class getSummary{

        @Test
        @DisplayName("Deve retornar o resumo com sucesso")
        void shouldGetFinancialSummaryWithSuccess(){

            User user = UserTestBuilder.aUser().build();

            BigDecimal totalIncomes = BigDecimal.valueOf(12171.1);
            BigDecimal totalExpenses = BigDecimal.valueOf(10256.04);
            BigDecimal balance = totalIncomes.subtract(totalExpenses);

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.sumByUser(user)).thenReturn(totalIncomes);
            when(expenseRepository.sumByUser(user)).thenReturn(totalExpenses);

            AccountSummaryDTO response = service.getSummary();

            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1)).sumByUser(any(User.class));
            verify(expenseRepository, times(1)).sumByUser(any(User.class));

            assertEquals(totalIncomes.toString(), response.totalIncomes().toString());
            assertEquals(totalExpenses.toString(), response.totalExpense().toString());
            assertEquals(balance.toString(), response.balance().toString());
        }

    }

    @Nested
    class getYearlyFinancialSummary{

        @Test
        @DisplayName("Deve retonar um resumo do ano, separado por mes quando o ano não é informado.")
        void shouldReturnYearlyFinancialSummaryGroupedByMonthWhenYearIsNotProvided(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();
            Integer currentYear = Year.now().getValue();

            List<MonthlyAmountProjection> incomes = List.of(
                    mockProjection(1,BigDecimal.valueOf(1000))
            );

            List<MonthlyAmountProjection> expenses = List.of(
                    mockProjection(1,BigDecimal.valueOf(900))
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findMonthlyIncomes(any(UUID.class), anyInt())).thenReturn(incomes);
            when(expenseRepository.findMonthlyExpenses(any(UUID.class), anyInt())).thenReturn(expenses);

            YearlySummaryDTO response = service.getYearlyFinancialSummary(null);

            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1)).findMonthlyIncomes(any(),anyInt());
            verify(expenseRepository, times(1)).findMonthlyExpenses(any(),anyInt());

            MonthlySummaryDTO august = response.months().get(7);
            MonthlySummaryDTO january = response.months().getFirst();

            BigDecimal expectedBalance = incomes.getFirst().getTotal().subtract(expenses.getFirst().getTotal());

            assertEquals(currentYear,response.year());
            assertEquals(12,response.months().size());
            assertEquals("JANEIRO", january.month());
            assertEquals(expectedBalance, january.balance());
            assertEquals(BigDecimal.ZERO, august.income());
            assertEquals(BigDecimal.ZERO, august.expense());
            assertEquals(BigDecimal.ZERO, august.balance());

        }

        @Test
        @DisplayName("Deve retonar um resumo do ano, separado por mes quando o ano é informado.")
        void shouldReturnYearlyFinancialSummaryGroupedByMonthWhenYearIsProvided(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();
            Integer currentYear = 2025;

            List<MonthlyAmountProjection> incomes = List.of(
                    mockProjection(1,BigDecimal.valueOf(1000))
            );

            List<MonthlyAmountProjection> expenses = List.of(
                    mockProjection(1,BigDecimal.valueOf(900))
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findMonthlyIncomes(any(UUID.class), anyInt())).thenReturn(incomes);
            when(expenseRepository.findMonthlyExpenses(any(UUID.class), anyInt())).thenReturn(expenses);

            YearlySummaryDTO response = service.getYearlyFinancialSummary(currentYear);

            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1)).findMonthlyIncomes(any(),anyInt());
            verify(expenseRepository, times(1)).findMonthlyExpenses(any(),anyInt());

            MonthlySummaryDTO august = response.months().get(7);
            MonthlySummaryDTO january = response.months().getFirst();

            BigDecimal expectedBalance = incomes.getFirst().getTotal().subtract(expenses.getFirst().getTotal());

            assertEquals(currentYear,response.year());
            assertEquals(12,response.months().size());
            assertEquals("JANEIRO", january.month());
            assertEquals(expectedBalance, january.balance());
            assertEquals(BigDecimal.ZERO, august.income());
            assertEquals(BigDecimal.ZERO, august.expense());
            assertEquals(BigDecimal.ZERO, august.balance());

        }

    }

    private MonthlyAmountProjection mockProjection(int month, BigDecimal amount) {
        MonthlyAmountProjection projection = Mockito.mock(MonthlyAmountProjection.class);

        when(projection.getMes()).thenReturn(month);
        when(projection.getTotal()).thenReturn(amount);

        return projection;
    }

}
