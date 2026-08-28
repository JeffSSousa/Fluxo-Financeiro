package com.jeffssousa.fluxo.mapper;

import com.jeffssousa.fluxo.dto.income.IncomeRequestDTO;
import com.jeffssousa.fluxo.dto.income.IncomeResponseDTO;
import com.jeffssousa.fluxo.entities.Income;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IncomeMapper {

    @Mapping(target = "category", ignore = true)
    Income toEntity (IncomeRequestDTO dto);

    IncomeResponseDTO toDTO (Income income);
}
