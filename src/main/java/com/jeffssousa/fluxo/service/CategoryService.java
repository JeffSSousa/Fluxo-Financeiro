package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.CategoryRequestDTO;
import com.jeffssousa.fluxo.dto.CategoryResponseDTO;
import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.mapper.CategoryMapper;
import com.jeffssousa.fluxo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CategoryResponseDTO> getAll() {

        User user = userService.getAuthenticatedUser();
        log.info("Buscando todas as categorias - user: {}",user.getEmail());

        List<Category> categories = categoryRepository.findAllByUser(user);
        log.info("Encontrado {} categorias - user: {}", categories.size(),user.getEmail());

        return categories.stream()
                .map(mapper::toDto)
                .toList();

    }
}
