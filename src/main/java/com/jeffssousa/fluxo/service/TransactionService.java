package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.TransactionDTO;
import com.jeffssousa.fluxo.entities.Expense;
import com.jeffssousa.fluxo.entities.Income;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.repository.ExpenseRepository;
import com.jeffssousa.fluxo.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final IncomeRepository incomeRepository;

    private final ExpenseRepository expenseRepository;

    private final CurrentUserService userService;

    public List<TransactionDTO> findAll() {

        User user = userService.getAuthenticatedUser();

        log.info("Ininicando busca de todas as transações - user: {}", user.getEmail());

        List<Income> incomes = incomeRepository.findAllByUser(user);
        log.info("encontrado {} receitas - user: {}",incomes.size(),user.getEmail());
        List<Expense> expenses = expenseRepository.findAllByUser(user);
        log.info("encontrado {} receitas - user: {}",expenses.size(),user.getEmail());

        List<TransactionDTO> response = new ArrayList<>();
        response.addAll(convertIncomes(incomes));
        response.addAll(convertExpenses(expenses));
        log.info("total de {} transações encontradas - user: {}", response.size(),user.getEmail());

        return response.stream()
                .sorted(
                        (t1,t2) -> t1.transactionDate().compareTo(t2.transactionDate())
                ).toList();

    }

    private List<TransactionDTO> convertIncomes(List<Income> list){
        List<TransactionDTO> response = new ArrayList<>();

        for(Income income: list){
            response.add(new TransactionDTO(
                    income.getDescription(),
                    "RECEITA",
                    income.getAmount(),
                    income.getTransactionDate(),
                    income.isStatus()
            ));
        }
        return response;
    }

    private List<TransactionDTO> convertExpenses(List<Expense> list){
        List<TransactionDTO> response = new ArrayList<>();

        for(Expense expense: list){
            response.add(new TransactionDTO(
                    expense.getDescription(),
                    "DESPESA",
                    expense.getAmount(),
                    expense.getTransactionDate(),
                    expense.isStatus()
            ));
        }
        return response;
    }

}
