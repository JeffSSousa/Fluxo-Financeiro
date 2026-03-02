package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.ExpenseRequestDTO;
import com.jeffssousa.fluxo.entities.Expense;
import com.jeffssousa.fluxo.mapper.ExpenseMapper;
import com.jeffssousa.fluxo.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final ExpenseMapper mapper;

    public Expense addExpense(ExpenseRequestDTO dto) {

        log.info("Iniciando criação de expense. Description={}, Amount={}",
                dto.description(),
                dto.amount());

        Expense expense = mapper.toEntity(dto);

        expense = expenseRepository.save(expense);

        log.info("expense salvo com sucesso. ID={}, Amount={}",
                expense.getExpenseId(),
                expense.getAmount());

        return expense;

    }
}
