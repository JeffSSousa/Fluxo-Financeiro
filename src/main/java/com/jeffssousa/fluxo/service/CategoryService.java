package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.category.CategoryRequestDTO;
import com.jeffssousa.fluxo.dto.category.CategoryResponseDTO;
import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.exception.business.UnauthorizedResourceAccessException;
import com.jeffssousa.fluxo.mapper.CategoryMapper;
import com.jeffssousa.fluxo.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        log.info("[CREATE] Category - user: {}, name: {}", user.getEmail(), dto.name());

        Category category = mapper.toEntity(dto);
        category.setUser(user);

        category = categoryRepository.save(category);
        log.info("[CREATE SUCCESS] Category - user: {}, categoryId: {}, name: {}",
                user.getEmail(),
                category.getCategoryId(),
                category.getName());

        return category;

    }

    public List<CategoryResponseDTO> getAll() {

        User user = userService.getAuthenticatedUser();
        log.info("[LIST] Category - user: {}", user.getEmail());

        List<Category> categories = categoryRepository.findAllByUser(user);
        log.info("[LIST RESULT] Category - user: {}, total: {}", user.getEmail(), categories.size());

        return categories.stream()
                .map(mapper::toDto)
                .toList();

    }

    public CategoryResponseDTO getById(Long id) {

        User user = userService.getAuthenticatedUser();
        log.info("[READ] Category - user: {}, categoryId: {}", user.getEmail(), id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[READ] Category NOT FOUND - user: {}, categoryId: {}", user.getEmail(), id);
                    return new EntityNotFoundException("Categoria não encontrada!");
                });

        if (!category.getUser().equals(user)){
            log.warn("[READ] Category UNAUTHORIZED - user: {}, categoryId: {}", user.getEmail(), id);
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa categoria");
        }

        log.info("[READ SUCCESS] Category - user: {}, categoryId: {}", user.getEmail(), id);

        return mapper.toDto(category);

    }

    public void deleteById(Long id){

        User user = userService.getAuthenticatedUser();
        log.info("[DELETE] Category - user: {}, categoryId: {}", user.getEmail(), id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[DELETE] Category NOT FOUND - user: {}, categoryId: {}", user.getEmail(), id);
                    return new EntityNotFoundException("Categoria não encontrada!");
                });

        if (!category.getUser().equals(user)){
            log.warn("[DELETE] Category UNAUTHORIZED - user: {}, categoryId: {}", user.getEmail(), id);
            throw new UnauthorizedResourceAccessException("Você não tem permissão de deletar");
        }

        categoryRepository.delete(category);
        log.info("[DELETE SUCCESS] Category - user: {}, categoryId: {}", user.getEmail(), id);

    }

    public CategoryResponseDTO updateById(CategoryRequestDTO dto, Long id){

        User user = userService.getAuthenticatedUser();
        log.info("[UPDATE] Category - user: {}, categoryId: {}", user.getEmail(), id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[UPDATE] Category NOT FOUND - user: {}, categoryId: {}", user.getEmail(), id);
                    return new EntityNotFoundException("Categoria não encontrada");
                });

        if (!category.getUser().equals(user)){
            log.warn("[UPDATE] Category UNAUTHORIZED - user: {}, categoryId: {}", user.getEmail(), id);
            throw new UnauthorizedResourceAccessException("Acesso negado para atualização dessa categoria");
        }

        updateCategory(dto,category);
        Category response  = categoryRepository.save(category);

        log.info("[UPDATE SUCCESS] Category - user: {}, categoryId: {}", user.getEmail(), id);

        return mapper.toDto(response);

    }

    private void updateCategory(CategoryRequestDTO dto, Category category){

        if (dto.name() != null) category.setName(dto.name());
        if (dto.type() != null) category.setType(dto.type());

    }


}
