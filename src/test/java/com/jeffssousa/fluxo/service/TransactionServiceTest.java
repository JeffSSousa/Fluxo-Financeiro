package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.builders.ExpenseTestBuilder;
import com.jeffssousa.fluxo.builders.IncomeTestBuilder;
import com.jeffssousa.fluxo.builders.UserTestBuilder;
import com.jeffssousa.fluxo.dto.transaction.TransactionDTO;
import com.jeffssousa.fluxo.entities.Expense;
import com.jeffssousa.fluxo.entities.Income;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.repository.ExpenseRepository;
import com.jeffssousa.fluxo.repository.IncomeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CurrentUserService userService;

    @InjectMocks
    private TransactionService transactionService;


    @Nested
    class findAll{

        @Test
        @DisplayName("Deve retornar todas as transações em ordenada(da mais recente para mais antiga) com sucesso.")
        void shouldReturnAllTransactionsSorted() {

            User user = UserTestBuilder.aUser().build();

            Income salario = IncomeTestBuilder.anIncome()
                    .withDescription("Salario")
                    .withAmount(BigDecimal.valueOf(5000))
                    .withTransactionDate(LocalDateTime.of(2024, 1, 10,2,10))
                    .build();

            Income freelancer = IncomeTestBuilder.anIncome()
                    .withDescription("Freelancer")
                    .withAmount(BigDecimal.valueOf(1000))
                    .withTransactionDate(LocalDateTime.of(2024, 1, 5,5,10))
                    .build();

            Expense aluguel = ExpenseTestBuilder.anExpense()
                    .withDescription("Aluguel")
                    .withAmount(BigDecimal.valueOf(1350))
                    .withTransactionDate(LocalDateTime.of(2024, 1, 15,8,10))
                    .build();

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findAllByUser(user)).thenReturn(List.of(salario,freelancer));
            when(expenseRepository.findAllByUser(user)).thenReturn(List.of(aluguel));


            List<TransactionDTO> response = transactionService.findAll();

            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1)).findAllByUser(user);
            verify(expenseRepository, times(1)).findAllByUser(user);

            assertNotNull(response);
            assertEquals(3,response.size());

            assertEquals(freelancer.getTransactionDate(), response.getFirst().transactionDate());
            assertEquals(salario.getTransactionDate(), response.get(1).transactionDate());
            assertEquals(aluguel.getTransactionDate(), response.get(2).transactionDate());

            assertEquals("RECEITA", response.getFirst().type());
            assertEquals("RECEITA", response.get(1).type());
            assertEquals("DESPESA", response.get(2).type());



        }

    }
}
