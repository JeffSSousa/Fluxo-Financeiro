package com.jeffssousa.fluxo.mapper;

import com.jeffssousa.fluxo.dto.ExpenseRequestDTO;
import com.jeffssousa.fluxo.entities.Expense;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    Expense toEntity(ExpenseRequestDTO dto);
}
