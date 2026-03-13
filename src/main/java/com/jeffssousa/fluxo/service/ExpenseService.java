package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.ExpenseRequestDTO;
import com.jeffssousa.fluxo.dto.ExpenseResponseDTO;
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

    public ExpenseResponseDTO getById(UUID id) {

        User user = userService.getAuthenticatedUser();

        log.info("iniciando busca de expense por id - user: {}", user.getEmail());

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFound("Expense não encontrada!"));
        UUID expenseUserId = expense.getUser().getUserId();

        if (!user.getUserId().equals(expenseUserId)){
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa transação");
        }

        log.info("Busca de despesa por id realizada com sucesso - user: {}",user.getEmail());

        return mapper.toDto(expense);

    }

    public void deleteById(UUID id){

        User user = userService.getAuthenticatedUser();

        log.info("deletando despesa por ID - user: {}", user.getEmail());

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFound("Expense não encontrada!"));
        UUID expenseUserId = expense.getUser().getUserId();

        if (!user.getUserId().equals(expenseUserId)){
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa transação");
        }

        expenseRepository.deleteById(expense.getExpenseId());
        log.info("Despesa do ID: {} eliminada com sucesso - user: {}", user.getUserId(),user.getEmail());

    }

    public ExpenseResponseDTO updateById(UUID id, ExpenseRequestDTO dto){

        User user = userService.getAuthenticatedUser();
        log.info("iniciando um update expense - user: {}", user.getEmail());

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFound("Despesa não encontrada!"));
        UUID expenseUserId = expense.getUser().getUserId();

        if (!user.getUserId().equals(expenseUserId)){
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa transação");
        }

        updateExpense(dto, expense);
        expenseRepository.save(expense);

        log.info("Despesa com ID: {} foi atualizado com sucesso - user: {}",expense.getExpenseId(),user.getEmail());

        return mapper.toDto(expense);

    }

    private void updateExpense(ExpenseRequestDTO dto, Expense expense) {

        if (dto.description() != null){
            expense.setDescription(dto.description());
        }
        if (dto.amount() != null){
            expense.setAmount(dto.amount());
        }
        if (dto.transactionDate() != null){
            expense.setTransactionDate(dto.transactionDate());
        }
        // colocar enum para status pois ocorre
        // implementar alteração de categoria

    }

}
