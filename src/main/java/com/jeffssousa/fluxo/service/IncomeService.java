package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.ExpenseRequestDTO;
import com.jeffssousa.fluxo.dto.IncomeRequestDTO;
import com.jeffssousa.fluxo.dto.IncomeResponseDTO;
import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.Expense;
import com.jeffssousa.fluxo.entities.Income;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.enums.CategoryType;
import com.jeffssousa.fluxo.exception.business.TransactionNotFound;
import com.jeffssousa.fluxo.exception.business.UnauthorizedResourceAccessException;
import com.jeffssousa.fluxo.mapper.IncomeMapper;
import com.jeffssousa.fluxo.repository.CategoryRepository;
import com.jeffssousa.fluxo.repository.IncomeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;

    private final CategoryRepository categoryRepository;

    private final CurrentUserService userService;

    private final IncomeMapper mapper;

    @Transactional
    public Income addIncome(IncomeRequestDTO dto){

        log.info("Iniciando criação de Income. Description={}, Amount={}",
                dto.description(),
                dto.amount());

        User user = userService.getAuthenticatedUser();

        Category category = categoryRepository.findByName(dto.category())
                .orElseGet(() -> new Category(
                        dto.category(),
                        CategoryType.INCOME,
                        user
                        ));

        if (category.getCategoryId() == null){
            category = categoryRepository.save(category);
            log.info("categoria salva com sucesso. ID={}, Name={}, User={}",
                    category.getCategoryId(),
                    category.getName(),
                    category.getUser().getEmail());
        }

        Income income = mapper.toEntity(dto);
        income.setCategory(category);
        income.setUser(user);

        income = incomeRepository.save(income);

        log.info("Income salvo com sucesso. ID={}, Amount={}",
                income.getIncomeId(),
                income.getAmount());

        return income;
    }

    public List<IncomeResponseDTO> getAll() {

        User user = userService.getAuthenticatedUser();

        log.info("Iniciando busca de todos incomes - user: {}", user.getEmail());

        List<Income> response = incomeRepository.findAllByUser(user);

        log.info("Encontrado {} incomes - user: {}", response.size(), user.getEmail());

        return response.stream()
                .sorted((i1,i2) -> i1.getTransactionDate().compareTo(i2.getTransactionDate()))
                .map(mapper::toDTO)
                .toList();

    }

    public IncomeResponseDTO getById(UUID id) {

        User user = userService.getAuthenticatedUser();

        log.info("iniciando busca de income por id - user: {}", user.getEmail());

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFound("Income não encontrada!"));
        UUID incomeUserId = income.getUser().getUserId();

        if (!user.getUserId().equals(incomeUserId)){
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa transação");
        }

        log.info("Busca de receita por id realizada  com sucesso - user: {}",user.getEmail());

        return mapper.toDTO(income);
    }

    public void deleteById(UUID id){

        User user = userService.getAuthenticatedUser();

        log.info("deletando receita por ID - user: {}", user.getEmail());


        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFound("Expense não encontrada!"));
        UUID incomeUserId = income.getUser().getUserId();

        if (!user.getUserId().equals(incomeUserId)){
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa transação");
        }

        incomeRepository.deleteById(income.getIncomeId());
        log.info("Receita do ID: {} eliminada com sucesso - user: {}", user.getUserId(),user.getEmail());

    }

    public IncomeResponseDTO updateById(UUID id, IncomeRequestDTO dto){

        User user = userService.getAuthenticatedUser();
        log.info("iniciando um update income - user: {}", user.getEmail());

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFound("Receita não encontrada!"));
        UUID incomeUserId = income.getUser().getUserId();

        if (!user.getUserId().equals(incomeUserId)){
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa transação");
        }

        updateIncome(dto, income);
        incomeRepository.save(income);

        log.info("Receita com ID: {} foi atualizado com sucesso - user: {}",income.getIncomeId(),user.getEmail());

        return mapper.toDTO(income);

    }

    private void updateIncome(IncomeRequestDTO dto, Income income) {

        if (dto.description() != null){
            income.setDescription(dto.description());
        }
        if (dto.amount() != null){
            income.setAmount(dto.amount());
        }
        if (dto.transactionDate() != null){
            income.setTransactionDate(dto.transactionDate());
        }
        // colocar enum para status pois ocorre
        // implementar alteração de categoria
    }
}
