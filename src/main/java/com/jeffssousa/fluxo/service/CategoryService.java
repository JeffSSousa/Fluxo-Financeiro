package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.CategoryRequestDTO;
import com.jeffssousa.fluxo.dto.CategoryResponseDTO;
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

    public CategoryResponseDTO getById(Long id) {

        User user = userService.getAuthenticatedUser();
        log.info("Buscando categoria pelo id - user: {}", user.getEmail());

        Category category = categoryRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada!"));

        if (!category.getUser().equals(user)){
            throw new UnauthorizedResourceAccessException("Você não pode acessar essa categoria");
        }

        return mapper.toDto(category);

    }

    public void deleteById(Long id){

        User user = userService.getAuthenticatedUser();
        log.info("Deletando categoria - user: {}", user.getEmail());

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada!"));

        if (!category.getUser().equals(user)){
            throw new UnauthorizedResourceAccessException("Você não tem permissão de deletar");
        }

        categoryRepository.delete(category);
        log.info("Deletado com sucesso - user: {}", user.getEmail());

    }

    public CategoryResponseDTO updateById(CategoryRequestDTO dto, Long id){

        User user = userService.getAuthenticatedUser();
        log.info("Atualizando categoria - user: {}", user.getEmail());

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        if (!category.getUser().equals(user)){
            throw new UnauthorizedResourceAccessException("Acesso negado para atualização dessa categoria");
        }

        updateCategory(dto,category);
        Category response  = categoryRepository.save(category);
        log.info("Categoria alterada com sucesso - user: {}",user.getEmail());
        return mapper.toDto(response);

    }

    private void updateCategory(CategoryRequestDTO dto, Category category){

        if (dto.name() != null) category.setName(dto.name());
        if (dto.type() != null) category.setType(dto.type());

    }


}
