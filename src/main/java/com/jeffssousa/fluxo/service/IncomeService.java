package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.IncomeRequestDTO;
import com.jeffssousa.fluxo.entities.Income;
import com.jeffssousa.fluxo.mapper.IncomeMapper;
import com.jeffssousa.fluxo.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;

    private final IncomeMapper mapper;

    public Income addIncome(IncomeRequestDTO dto){

        log.info("Iniciando criação de Income. Description={}, Amount={}",
                dto.description(),
                dto.amount());

        Income income = mapper.toEntity(dto);

        income = incomeRepository.save(income);

        log.info("Income salvo com sucesso. ID={}, Amount={}",
                income.getIncomeId(),
                income.getAmount());

        return income;
    }

}
