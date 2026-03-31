package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.CategoryRequestDTO;
import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.mapper.CategoryMapper;
import com.jeffssousa.fluxo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CurrentUserService userService;

    private final CategoryMapper mapper;

    public Category createCategory(CategoryRequestDTO dto) {

        User user = userService.getAuthenticatedUser();
        log.info("Criando Categoria - user: {}", user.getEmail());

        Category category = mapper.toEntity(dto);
        category.setUser(user);

        category = categoryRepository.save(category);
        log.info("Categoria {} criada com sucesso - user: {}", category.getName(),user.getEmail());

        return category;

    }
}
