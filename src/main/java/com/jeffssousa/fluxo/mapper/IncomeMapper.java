package com.jeffssousa.fluxo.mapper;

import com.jeffssousa.fluxo.dto.IncomeRequestDTO;
import com.jeffssousa.fluxo.entities.Income;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IncomeMapper {

    Income toEntity (IncomeRequestDTO dto);
}
