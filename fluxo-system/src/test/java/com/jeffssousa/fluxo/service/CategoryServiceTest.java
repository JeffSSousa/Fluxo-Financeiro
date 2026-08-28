package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.builders.CategoryTestBuilder;
import com.jeffssousa.fluxo.builders.UserTestBuilder;
import com.jeffssousa.fluxo.dto.category.CategoryRequestDTO;
import com.jeffssousa.fluxo.dto.category.CategoryResponseDTO;
import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.enums.CategoryType;
import com.jeffssousa.fluxo.exception.business.UnauthorizedResourceAccessException;
import com.jeffssousa.fluxo.mapper.CategoryMapper;
import com.jeffssousa.fluxo.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {


    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CurrentUserService userService;

    @Mock
    private CategoryMapper mapper;

    @Captor
    private ArgumentCaptor<Category> categoryCaptor;

    @InjectMocks
    private CategoryService service;

    @Nested
    class createCategory{

        @Test
        @DisplayName("Deve criar uma categoria com sucesso")
        void shouldCreateACategoryWithSuccess(){

            User user = UserTestBuilder.aUser().build();
            Category category = CategoryTestBuilder.aCategory().withUser(user).build();
            CategoryRequestDTO dto = new CategoryRequestDTO(
                    category.getName(),
                    category.getType()
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(mapper.toEntity(any(CategoryRequestDTO.class))).thenReturn(category);
            when(categoryRepository.save(category)).thenReturn(category);

            Category returnedCategory = service.createCategory(dto);

            verify(userService, times(1)).getAuthenticatedUser();
            verify(mapper, times(1)).toEntity(any(CategoryRequestDTO.class));
            verify(categoryRepository,times(1)).save(any(Category.class));

            assertEquals(category.getName(),returnedCategory.getName());
            assertEquals(category.getType(),returnedCategory.getType());
        }

    }

    @Nested
    class getAll{

        @Test
        @DisplayName("Deve buscar todas as categorias com sucesso")
        void shouldFindAllCategoriesWithSuccess(){

            User user = UserTestBuilder.aUser().build();
            Category house = CategoryTestBuilder.aCategory().
                    withCategoryId(1L)
                    .withName("Casa")
                    .withUser(user)
                    .build();

            Category marketplace = CategoryTestBuilder.aCategory().
                    withCategoryId(2L)
                    .withName("Mercado")
                    .withUser(user)
                    .build();

            List<Category> list = List.of(house, marketplace);

            CategoryResponseDTO houseDTO = new CategoryResponseDTO(
                    house.getCategoryId(),
                    house.getName(),
                    house.getType());

            CategoryResponseDTO marketplaceDTO = new CategoryResponseDTO(
                    marketplace.getCategoryId(),
                    marketplace.getName(),
                    marketplace.getType());

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(mapper.toDto(house)).thenReturn(houseDTO);
            when(mapper.toDto(house)).thenReturn(marketplaceDTO);
            when(categoryRepository.findAllByUser(user)).thenReturn(list);

            List<CategoryResponseDTO> response = service.getAll();

            verify(userService,times(1)).getAuthenticatedUser();
            verify(mapper,times(2)).toDto(any(Category.class));
            verify(categoryRepository, times(1)).findAllByUser(user);

            assertNotNull(response);
            assertEquals(list.size(),response.size());
        }
    }

    @Nested
    class getById{

        @Test
        @DisplayName("Deve buscar uma categoria pelo ID com sucesso")
        void shouldFindCategoryByIdWithSuccess(){

            User user = UserTestBuilder.aUser().build();
            Long id = 1L;
            Category category = CategoryTestBuilder.aCategory()
                    .withCategoryId(id)
                    .withUser(user)
                    .build();

            CategoryResponseDTO dto = new CategoryResponseDTO(
                    category.getCategoryId(),
                    category.getName(),
                    category.getType()
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(mapper.toDto(any(Category.class))).thenReturn(dto);
            when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

            CategoryResponseDTO response = service.getById(id);

            verify(mapper, times(1)).toDto(any(Category.class));
            verify(categoryRepository, times(1)).findById(anyLong());

            assertEquals(dto.categoryId(), response.categoryId());
            assertEquals(dto.name(), response.name());
            assertEquals(dto.type(), response.type());

        }

        @Test
        @DisplayName("Deve lançar uma exceção quando não encontrar a categoria")
        void shouldThrowExceptionWhenCategoryNotFound(){

            User user = UserTestBuilder.aUser().build();
            Long id = 1L;

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

            EntityNotFoundException e = assertThrows(EntityNotFoundException.class, () -> service.getById(id));

            verify(userService, times(1)).getAuthenticatedUser();
            verify(categoryRepository, times(1)).findById(anyLong());

            assertEquals("Categoria não encontrada!", e.getMessage());


        }

        @Test
        @DisplayName("Deve lançar uma exceção quando acessar uma categoria de um usuario diferente")
        void shouldThrowExceptionWhenAccessingCategoryOfAnotherUser(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            User categoryUser = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            Long id = 1L;

            Category category = CategoryTestBuilder.aCategory()
                    .withCategoryId(id)
                    .withUser(categoryUser)
                    .build();

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

            UnauthorizedResourceAccessException e = assertThrows(
                    UnauthorizedResourceAccessException.class,
                    () -> service.getById(id)
            );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(categoryRepository, times(1)).findById(anyLong());

            assertEquals("Você não pode acessar essa categoria", e.getMessage());


        }
    }

    @Nested
    class deleteById{

        @Test
        @DisplayName("Deve deletar uma categoria pelo ID com successo")
        void shouldDeleteCategoryByIdWithSuccess(){

            User user = UserTestBuilder.aUser().build();
            Long id = 1L;
            Category category = CategoryTestBuilder.aCategory()
                    .withCategoryId(id)
                    .withUser(user)
                    .build();

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

            service.deleteById(id);

            verify(userService, times(1)).getAuthenticatedUser();
            verify(categoryRepository, times(1)).delete(any(Category.class));

        }

        @Test
        @DisplayName("Deve lançar exceção quando não encontrar a categoria para deletar")
        void shouldThrowExceptionWhenCategoryNotFound(){

            User user = UserTestBuilder.aUser().build();
            Long id = 1L;

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

            EntityNotFoundException e = assertThrows(
                    EntityNotFoundException.class,
                    () -> service.deleteById(id)
            );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(categoryRepository, times(1)).findById(anyLong());

            assertEquals("Categoria não encontrada!", e.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar deletar categoria de outro usuário")
        void shouldThrowExceptionWhenDeletingCategoryFromAnotherUser(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            User categoryUser = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            Long id = 1L;

            Category category = CategoryTestBuilder.aCategory()
                    .withCategoryId(id)
                    .withUser(categoryUser)
                    .build();

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

            UnauthorizedResourceAccessException e = assertThrows(
                    UnauthorizedResourceAccessException.class,
                    () -> service.deleteById(id)
            );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(categoryRepository, times(1)).findById(anyLong());

            assertEquals("Você não pode acessar essa categoria", e.getMessage());
        }
    }

    @Nested
    class updateById{

        @Test
        @DisplayName("Deve Atualizar  a categoria pelo ID com sucesso")
        void shouldUpdateCategoryByIdWithSuccess(){

            User user = UserTestBuilder.aUser().build();
            Long id = 1L;
            Category category = CategoryTestBuilder.aCategory()
                    .withCategoryId(id)
                    .withUser(user)
                    .build();

            CategoryRequestDTO dto = new CategoryRequestDTO("House", CategoryType.EXPENSE);
            CategoryResponseDTO updatedResponse = new CategoryResponseDTO(
                    category.getCategoryId(),
                    dto.name(),
                    dto.type()
                    );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
            when(categoryRepository.save(any(Category.class))).thenReturn(category);
            when(mapper.toDto(any(Category.class))).thenReturn(updatedResponse);

            CategoryResponseDTO response = service.updateById(dto, id);

            verify(userService, times(1)).getAuthenticatedUser();
            verify(categoryRepository, times(1)).findById(anyLong());
            verify(categoryRepository, times(1)).save(categoryCaptor.capture());

            Category captured = categoryCaptor.getValue();

            assertEquals(dto.name(), captured.getName());
            assertEquals(dto.type(), captured.getType());
            assertEquals(dto.name(), response.name());
            assertEquals(dto.type(), response.type());
        }

        @Test
        @DisplayName("Deve lançar exceção quando não encontrar a categoria para atualizar")
        void shouldThrowExceptionWhenCategoryNotFound(){

            User user = UserTestBuilder.aUser().build();
            Long id = 1L;

            CategoryRequestDTO dto = new CategoryRequestDTO("House", CategoryType.EXPENSE);

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

            EntityNotFoundException e = assertThrows(
                    EntityNotFoundException.class,
                    () -> service.updateById(dto, id)
            );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(categoryRepository, times(1)).findById(anyLong());

            assertEquals("Categoria não encontrada!", e.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar atualizar categoria de outro usuário")
        void shouldThrowExceptionWhenUpdatingCategoryFromAnotherUser(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            User categoryUser = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            Long id = 1L;

            CategoryRequestDTO dto = new CategoryRequestDTO("House", CategoryType.EXPENSE);

            Category category = CategoryTestBuilder.aCategory()
                    .withCategoryId(id)
                    .withUser(categoryUser)
                    .build();

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

            UnauthorizedResourceAccessException e = assertThrows(
                    UnauthorizedResourceAccessException.class,
                    () -> service.updateById(dto, id)
            );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(categoryRepository, times(1)).findById(anyLong());

            assertEquals("Acesso negado para atualização dessa categoria", e.getMessage());
        }

    }

}