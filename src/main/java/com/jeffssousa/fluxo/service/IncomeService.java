package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.income.IncomeRequestDTO;
import com.jeffssousa.fluxo.dto.income.IncomeResponseDTO;
import com.jeffssousa.fluxo.entities.Category;
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

        User user = userService.getAuthenticatedUser();

        log.info("[CREATE] Income - user: {}, description: {}, amount: {}",
                user.getEmail(),
                dto.description(),
                dto.amount());


        Income income = mapper.toEntity(dto);
        Category category = findOrCreateCategory(dto.category(), income);
        income.setUser(user);

        income = incomeRepository.save(income);

        log.info("[CREATE SUCCESS] Income - user: {}, incomeId: {}, amount: {}, categoryId: {}",
                user.getEmail(),
                income.getIncomeId(),
                income.getAmount(),
                category.getCategoryId());

        return income;
    }

    public List<IncomeResponseDTO> getAll() {

        User user = userService.getAuthenticatedUser();

        log.info("[LIST] Income - user: {}", user.getEmail());

        List<Income> response = incomeRepository.findAllByUser(user);

        log.info("[LIST RESULT] Income - user: {}, total: {}", user.getEmail(), response.size());

        return response.stream()
                .sorted((i1,i2) -> i1.getTransactionDate().compareTo(i2.getTransactionDate()))
                .map(mapper::toDTO)
                .toList();

    }

    public IncomeResponseDTO getById(UUID id) {

        User user = userService.getAuthenticatedUser();

        log.info("[READ] Income - user: {}, incomeId: {}", user.getEmail(), id);

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[READ] Income NOT FOUND - user: {}, incomeId: {}", user.getEmail(), id);
                    return new TransactionNotFound("Income não encontrada!");
                });

        UUID incomeUserId = income.getUser().getUserId();

        if (!user.getUserId().equals(incomeUserId)){
            log.warn("[READ] Income UNAUTHORIZED - user: {}, incomeId: {}", user.getEmail(), id);
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa transação");
        }

        log.info("[READ SUCCESS] Income - user: {}, incomeId: {}", user.getEmail(), id);

        return mapper.toDTO(income);
    }

    public void deleteById(UUID id){

        User user = userService.getAuthenticatedUser();

        log.info("[DELETE] Income - user: {}, incomeId: {}", user.getEmail(), id);


        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[DELETE] Income NOT FOUND - user: {}, incomeId: {}", user.getEmail(), id);
                    return new TransactionNotFound("Income não encontrada!");
                });

        UUID incomeUserId = income.getUser().getUserId();

        if (!user.getUserId().equals(incomeUserId)){
            log.warn("[DELETE] Income UNAUTHORIZED - user: {}, incomeId: {}", user.getEmail(), id);
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa transação");
        }

        incomeRepository.deleteById(income.getIncomeId());
        log.info("[DELETE SUCCESS] Income - user: {}, incomeId: {}", user.getEmail(), id);

    }

    public IncomeResponseDTO updateById(UUID id, IncomeRequestDTO dto){

        User user = userService.getAuthenticatedUser();
        log.info("[UPDATE] Income - user: {}, incomeId: {}", user.getEmail(), id);

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[UPDATE] Income NOT FOUND - user: {}, incomeId: {}", user.getEmail(), id);
                    return new TransactionNotFound("Receita não encontrada!");
                });

        UUID incomeUserId = income.getUser().getUserId();

        if (!user.getUserId().equals(incomeUserId)){
            log.warn("[UPDATE] Income UNAUTHORIZED - user: {}, incomeId: {}", user.getEmail(), id);
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa transação");
        }

        updateIncome(dto, income);
        incomeRepository.save(income);

        log.info("[UPDATE SUCCESS] Income - user: {}, incomeId: {}", user.getEmail(), id);

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
        if (dto.status() != null){
            income.setStatus(dto.status());
        }
        if (dto.category() != null){
            findOrCreateCategory(dto.category(), income);
        }
    }

    private Category findOrCreateCategory(String name, Income income){

        User user = userService.getAuthenticatedUser();

        Category category = categoryRepository.findByNameAndUserUserId(name, user.getUserId())
                .orElseGet(() -> new Category(
                        name,
                        CategoryType.INCOME,
                        user
                ));

        if (category.getCategoryId() == null){
            category = categoryRepository.save(category);

            log.info("[CREATE] Category (AUTO) - user: {}, categoryId: {}, name: {}",
                    user.getEmail(),
                    category.getCategoryId(),
                    category.getName());
        }

        income.setCategory(category);
        return category;
    }

}
