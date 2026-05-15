package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.category.CategoryRequestDTO;
import com.jeffssousa.fluxo.dto.category.CategoryResponseDTO;
import com.jeffssousa.fluxo.enums.CategoryType;
import com.jeffssousa.fluxo.exception.business.UnauthorizedResourceAccessException;
import com.jeffssousa.fluxo.exception.handler.RestExceptionHandler;
import com.jeffssousa.fluxo.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(CategoryController.class)
@Import(RestExceptionHandler.class)
public class CategoryControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    CategoryService service;


    @Nested
    class createCategory{

        @Test
        @DisplayName("Deve criar uma categoria com sucesso")
        void shouldCreateCategorySuccessfully() throws Exception{

            String json = """
                    {
                        "name": "House",
                        "type": "EXPENSE"
                    }
                    """;

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/category")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            );

            result
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().string("")
            );

            verify(service).createCategory(any(CategoryRequestDTO.class));

        }


    }

    @Nested
    class getById{

        @Test
        @DisplayName("Deve retornar uma categoria com sucesso")
        void shouldReturnCategoryByIdSuccessfully() throws Exception {

            CategoryResponseDTO response = new CategoryResponseDTO(
                    1L,
                    "Casa",
                    CategoryType.EXPENSE
            );

            when(service.getById(1L)).thenReturn(response);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/category/1")
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).getById(1L);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.categoryId").value(response.categoryId()))
                    .andExpect(jsonPath("$.name").value(response.name()))
                    .andExpect(jsonPath("$.type").value(response.type().name()));

        }


        @Test
        @DisplayName("Deve retornar 404 quando não encontrar a categoria")
        void shouldReturn404WhenCategoryDoesNotExist() throws Exception {

            when(service.getById(1L))
                    .thenThrow(new EntityNotFoundException("Categoria não encontrada!"));

            mvc.perform(get("/category/1"))
                    .andExpect(status().isNotFound());

            verify(service).getById(1L);
        }

        @Test
        @DisplayName("Deve retornar 403 quando não tiver permissão para acessar a categoria")
        void shouldReturn403WhenCategoryIsUnauthorized() throws Exception {

            when(service.getById(1L))
                    .thenThrow(new UnauthorizedResourceAccessException(
                            "Você não pode acessar essa categoria")
                    );

            mvc.perform(
                    get("/category/1"))
                    .andExpect(status().isForbidden()
            );

            verify(service).getById(1L);

        }

    }


}
