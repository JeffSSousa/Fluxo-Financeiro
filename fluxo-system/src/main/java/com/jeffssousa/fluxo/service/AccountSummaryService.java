package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.summary.AccountSummaryDTO;
import com.jeffssousa.fluxo.dto.summary.MonthlySummaryDTO;
import com.jeffssousa.fluxo.dto.summary.YearlySummaryDTO;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.repository.ExpenseRepository;
import com.jeffssousa.fluxo.repository.IncomeRepository;
import com.jeffssousa.fluxo.repository.projection.MonthlyAmountProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountSummaryService {

    private final IncomeRepository incomeRepository;

    private final ExpenseRepository expenseRepository;

    private final CurrentUserService userService;

    public AccountSummaryDTO getSummary() {

        User user = userService.getAuthenticatedUser();

        log.info("[READ] Account Summary - user: {}", user.getEmail());

        BigDecimal totalIncomes = incomeRepository.sumByUser(user);
        BigDecimal totalExpenses = expenseRepository.sumByUser(user);

        BigDecimal balance = totalIncomes.subtract(totalExpenses);

        log.info(
                "[READ RESULT] Account Summary - user: {}, incomes: {}, expenses: {}, balance: {}",
                user.getEmail(),
                totalIncomes,
                totalExpenses,
                balance
        );

        return new AccountSummaryDTO(
                totalIncomes,
                totalExpenses,
                balance
        );

    }

    public YearlySummaryDTO getYearlyFinancialSummary(Integer year) {

        int targetYear = Optional.ofNullable(year)
                .orElse(Year.now().getValue());

        User user = userService.getAuthenticatedUser();

        log.info("[READ] Yearly Summary - user: {}, year: {}", user.getEmail(), targetYear);

        List<MonthlyAmountProjection> incomes = incomeRepository.findMonthlyIncomes(user.getUserId(),targetYear);
        List<MonthlyAmountProjection> expenses = expenseRepository.findMonthlyExpenses(user.getUserId(),targetYear);

        Map<Integer, BigDecimal> incomesMap = convertToMap(incomes);
        Map<Integer, BigDecimal> expensesMap = convertToMap(expenses);

        List<MonthlySummaryDTO> months = new ArrayList<>();

        for(int i = 1; i<=12; i++){

            BigDecimal income = incomesMap.getOrDefault(i,BigDecimal.ZERO);
            BigDecimal expense = expensesMap.getOrDefault(i,BigDecimal.ZERO);

            BigDecimal balance = income.subtract(expense);

            months.add(new MonthlySummaryDTO(
                    convertMonth(i),
                    income,
                    expense,
                    balance
            ));

        }

        log.info("[READ RESULT] Yearly Summary - user: {}, year: {}, months: {}",
                user.getEmail(),
                targetYear,
                months.size());

        return new YearlySummaryDTO(targetYear, months);

    }

    private Map<Integer,BigDecimal> convertToMap(List<MonthlyAmountProjection> list){
        return list.stream()
                .collect(Collectors.toMap(
                   MonthlyAmountProjection::getMes,
                   MonthlyAmountProjection::getTotal
                ));
    }

    private String convertMonth(int month) {
        return Month.of(month)
                .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"))
                .toUpperCase();
    }
}
