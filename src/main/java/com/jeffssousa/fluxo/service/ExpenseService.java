package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.ExpenseRequestDTO;
import com.jeffssousa.fluxo.dto.ExpenseResponseDTO;
import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.Expense;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.enums.CategoryType;
import com.jeffssousa.fluxo.mapper.ExpenseMapper;
import com.jeffssousa.fluxo.repository.CategoryRepository;
import com.jeffssousa.fluxo.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final CategoryRepository categoryRepository;

    private final CurrentUserService userService;

    private final ExpenseMapper mapper;

    @Transactional
    public Expense addExpense(ExpenseRequestDTO dto) {

        log.info("Iniciando criação de expense. Description={}, Amount={}",
                dto.description(),
                dto.amount());

        User user = userService.getAuthenticatedUser();

        Category category = categoryRepository.findByNameAndUserUserId(dto.category(), user.getUserId())
                .orElseGet(() -> new Category(
                        dto.category(),
                        CategoryType.EXPENSE,
                        user
                        ));

        if (category.getCategoryId() == null){
            category = categoryRepository.save(category);
            log.info("categoria salva com sucesso. ID={}, Name={}, User={}",
                    category.getCategoryId(),
                    category.getName(),
                    category.getUser().getEmail());
        }

        Expense expense = mapper.toEntity(dto);
        expense.setCategory(category);
        expense.setUser(user);

        expense = expenseRepository.save(expense);

        log.info("expense salvo com sucesso. ID={}, Amount={}",
                expense.getExpenseId(),
                expense.getAmount());

        return expense;
    }

    public List<ExpenseResponseDTO> getAll() {

        User user = userService.getAuthenticatedUser();
        log.info("Iniciando busca de todas as despesas - user: {}", user.getEmail());

        List<Expense> list = expenseRepository.findAllByUser(user);
        log.info("encontrado {} despesas - user: {}", list.size(),user.getEmail());

        return list.stream()
                .sorted((e1,e2) -> e1.getTransactionDate().compareTo(e2.getTransactionDate()))
                .map(mapper::toDto)
                .toList();
    }
}
