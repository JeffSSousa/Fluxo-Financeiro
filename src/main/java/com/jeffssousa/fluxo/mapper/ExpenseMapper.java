package com.jeffssousa.fluxo.mapper;

import com.jeffssousa.fluxo.dto.ExpenseRequestDTO;
import com.jeffssousa.fluxo.dto.ExpenseResponseDTO;
import com.jeffssousa.fluxo.entities.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(target = "category", ignore = true)
    Expense toEntity(ExpenseRequestDTO dto);

    ExpenseResponseDTO toDto(Expense expense);
}
