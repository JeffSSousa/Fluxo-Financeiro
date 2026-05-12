package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.expense.ExpenseRequestDTO;
import com.jeffssousa.fluxo.dto.expense.ExpenseResponseDTO;
import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.Expense;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.enums.CategoryType;
import com.jeffssousa.fluxo.exception.business.TransactionNotFound;
import com.jeffssousa.fluxo.exception.business.UnauthorizedResourceAccessException;
import com.jeffssousa.fluxo.mapper.ExpenseMapper;
import com.jeffssousa.fluxo.repository.CategoryRepository;
import com.jeffssousa.fluxo.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

        User user = userService.getAuthenticatedUser();

        log.info("[CREATE] Expense - user: {}, description: {}, amount: {}",
                user.getEmail(),
                dto.description(),
                dto.amount());

        Expense expense = mapper.toEntity(dto);
        Category category = findOrCreateCategory(dto.category(),user);
        expense.setCategory(category);
        expense.setUser(user);

        expense = expenseRepository.save(expense);

        log.info("[CREATE SUCCESS] Expense - user: {}, expenseId: {}, amount: {}, categoryId: {}",
                user.getEmail(),
                expense.getExpenseId(),
                expense.getAmount(),
                category.getCategoryId());

        return expense;
    }

    public List<ExpenseResponseDTO> getAll() {

        User user = userService.getAuthenticatedUser();
        log.info("[LIST] Expense - user: {}", user.getEmail());

        List<Expense> expenses = expenseRepository.findAllByUser(user);
        log.info("[LIST RESULT] Expense - user: {}, total: {}", user.getEmail(), expenses.size());

        return expenses.stream()
                .sorted((e1,e2) -> e1.getTransactionDate().compareTo(e2.getTransactionDate()))
                .map(mapper::toDto)
                .toList();
    }

    public ExpenseResponseDTO getById(UUID id) {

        User user = userService.getAuthenticatedUser();

        log.info("[READ] Expense - user: {}, expenseId: {}", user.getEmail(), id);

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[READ] Expense NOT FOUND - user: {}, expenseId: {}", user.getEmail(), id);
                    return new TransactionNotFound("Expense não encontrada!");
                });

        UUID expenseUserId = expense.getUser().getUserId();

        if (!user.getUserId().equals(expenseUserId)){
            log.warn("[READ] Expense UNAUTHORIZED - user: {}, expenseId: {}", user.getEmail(), id);
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa transação");
        }

        log.info("[READ SUCCESS] Expense - user: {}, expenseId: {}", user.getEmail(), id);

        return mapper.toDto(expense);

    }

    public void deleteById(UUID id){

        User user = userService.getAuthenticatedUser();

        log.info("[DELETE] Expense - user: {}, expenseId: {}", user.getEmail(), id);

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[DELETE] Expense NOT FOUND - user: {}, expenseId: {}", user.getEmail(), id);
                    return new TransactionNotFound("Despesa não encontrada!");
                });

        UUID expenseUserId = expense.getUser().getUserId();

        if (!user.getUserId().equals(expenseUserId)){
            log.warn("[DELETE] Expense UNAUTHORIZED - user: {}, expenseId: {}", user.getEmail(), id);
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa transação");
        }

        expenseRepository.deleteById(expense.getExpenseId());
        log.info("[DELETE SUCCESS] Expense - user: {}, expenseId: {}", user.getEmail(), id);

    }

    public ExpenseResponseDTO updateById(UUID id, ExpenseRequestDTO dto){

        User user = userService.getAuthenticatedUser();

        log.info("[UPDATE] Expense - user: {}, expenseId: {}", user.getEmail(), id);

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[UPDATE] Expense NOT FOUND - user: {}, expenseId: {}", user.getEmail(), id);
                    return new TransactionNotFound("Despesa não encontrada!");
                });

        UUID expenseUserId = expense.getUser().getUserId();

        if (!user.getUserId().equals(expenseUserId)){
            log.warn("[UPDATE] Expense UNAUTHORIZED - user: {}, expenseId: {}", user.getEmail(), id);
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa transação");
        }

        Integer affectedFields = updateExpense(dto, expense, user);
        expenseRepository.save(expense);

        log.info("[UPDATE SUCCESS] Expense - affected fields: {} , user: {}, expenseId: {}",affectedFields ,user.getEmail(), id);

        return mapper.toDto(expense);

    }

    public List<ExpenseResponseDTO> getUpcoming15Expenses(){

        User user = userService.getAuthenticatedUser();

        LocalDate now = LocalDate.now();
        LocalDate next15Days = now.plusDays(15);

        log.info("[LIST] Expense UPCOMING - user: {}, from: {}, to: {}",
                user.getEmail(), now, next15Days);

        List<Expense> response = expenseRepository.findTop15ByUserAndDueDateBetweenOrderByDueDateAsc(
                user,
                now,
                next15Days
        );

        log.info("[LIST RESULT] Expense UPCOMING - user: {}, total: {}",
                user.getEmail(), response.size());

        return response.stream()
                .map(mapper::toDto)
                .toList();

    }


    private Integer updateExpense(ExpenseRequestDTO dto, Expense expense, User user) {

        int affectedFields = 0;

        if (dto.description() != null){
            expense.setDescription(dto.description());
            affectedFields++;
        }
        if (dto.amount() != null){
            expense.setAmount(dto.amount());
            affectedFields++;
        }
        if (dto.transactionDate() != null){
            expense.setTransactionDate(dto.transactionDate());
            affectedFields++;
        }
        if (dto.status() !=  null){
            expense.setStatus(dto.status());
            affectedFields++;
        }
        if (dto.category() != null){
          findOrCreateCategory(dto.category(), user);
            affectedFields++;
        }

        return affectedFields;
    }

    private Category findOrCreateCategory(String name, User user){

        Category category = categoryRepository.findByNameAndUserUserId(name, user.getUserId())
                .orElseGet(() -> new Category(
                        name,
                        CategoryType.EXPENSE,
                        user
                ));

        if (category.getCategoryId() == null){
            category = categoryRepository.save(category);

            log.info("[CREATE] Category (AUTO) - user: {}, categoryId: {}, name: {}",
                    user.getEmail(),
                    category.getCategoryId(),
                    category.getName());
        }

        return category;

    }

}
