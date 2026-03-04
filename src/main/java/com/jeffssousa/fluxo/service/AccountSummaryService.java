package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.AccountSummaryDTO;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.repository.ExpenseRepository;
import com.jeffssousa.fluxo.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountSummaryService {

    private final IncomeRepository incomeRepository;

    private final ExpenseRepository expenseRepository;

    private final CurrentUserService userService;

    public AccountSummaryDTO getSummary() {

        User user = userService.getAuthenticatedUser();

        BigDecimal totalIncomes = incomeRepository.sumByUser(user);
        BigDecimal totalExpenses = expenseRepository.sumByUser(user);

        log.info(totalIncomes.toString());
        log.info(totalExpenses.toString());

        BigDecimal balance = totalIncomes.subtract(totalExpenses);

        log.info("Busca de summary - usuario: {} ",  user.getEmail());

        return new AccountSummaryDTO(
                totalIncomes,
                totalExpenses,
                balance
        );

    }
}
