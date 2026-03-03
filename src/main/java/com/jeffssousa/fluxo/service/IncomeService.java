package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.IncomeRequestDTO;
import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.Income;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.enums.CategoryType;
import com.jeffssousa.fluxo.mapper.IncomeMapper;
import com.jeffssousa.fluxo.repository.CategoryRepository;
import com.jeffssousa.fluxo.repository.IncomeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

}
