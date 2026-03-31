package com.jeffssousa.fluxo.mapper;

import com.jeffssousa.fluxo.dto.CategoryRequestDTO;
import com.jeffssousa.fluxo.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequestDTO dto);

}
