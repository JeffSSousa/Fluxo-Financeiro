package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.category.CategoryRequestDTO;
import com.jeffssousa.fluxo.dto.category.CategoryResponseDTO;
import com.jeffssousa.fluxo.enums.CategoryType;
import com.jeffssousa.fluxo.exception.business.UnauthorizedResourceAccessException;
import com.jeffssousa.fluxo.exception.handler.RestExceptionHandler;
import com.jeffssousa.fluxo.security.JwtAuthenticationFilter;
import com.jeffssousa.fluxo.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RestExceptionHandler.class)
public class CategoryControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    CategoryService service;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

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
    class getAll{

        @Test
        @DisplayName("Deve retornar todas as categoria com sucesso")
        void shouldReturnAllCategoryWithSuccess() throws Exception {

            CategoryResponseDTO house = new CategoryResponseDTO(
                    1L,
                    "Casa",
                    CategoryType.EXPENSE
            );
            CategoryResponseDTO salary = new CategoryResponseDTO(
                    1L,
                    "salary",
                    CategoryType.INCOME
            );
            List<CategoryResponseDTO> reponse = List.of(house, salary);

            when(service.getAll()).thenReturn(reponse);

            ResultActions result = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/category")
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).getAll();

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(reponse.size()));


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

    @Nested
    class deleteById{

        @Test
        @DisplayName("Deve deletar uma categoria pelo id com sucesso")
        void shouldDeleteCategoryByIdWithSuccess() throws Exception {

            Long id = 1L;

            doNothing().when(service).deleteById(id);

            ResultActions result = mvc.perform(
                    delete("/category/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            verify(service, times(1)).deleteById(id);

            result.andExpect(status().isNoContent());


        }

        @Test
        @DisplayName("Deve retornar 404 quando não encontrar a categoria")
        void shouldReturn404WhenCategoryDoesNotExist() throws Exception{

            doThrow(new EntityNotFoundException("Categoria não encontrada!"))
                    .when(service)
                    .deleteById(1L);

            mvc.perform(
                    delete("/category/1"))
                    .andExpect(status().isNotFound());

            verify(service, times(1)).deleteById(1L);

        }

        @Test
        @DisplayName("Deve retornar 403 quando não tiver permissão para acessar a categoria")
        void shouldReturn403WhenCategoryIsUnauthorized() throws Exception {

            doThrow(new UnauthorizedResourceAccessException("Você não pode acessar essa categoria"))
                    .when(service)
                    .deleteById(1L);

            mvc.perform(delete("/category/1"))
                    .andExpect(status().isForbidden());

            verify(service, times(1)).deleteById(1L);

        }


    }

    @Nested
    class updateById{

        @Test
        @DisplayName("Deve atualizar uma categoria com sucesso")
        void shouldUpdateCategoryByIdWithSuccess() throws Exception{

            CategoryRequestDTO request = new CategoryRequestDTO(
                    "Aluguel",
                    CategoryType.EXPENSE
            );

            String json = objectMapper.writeValueAsString(request);

            CategoryResponseDTO response = new CategoryResponseDTO(
                    1L,
                    request.name(),
                    request.type()
            );

            when(service.updateById(request, 1L))
                    .thenReturn(response);

            ResultActions result = mvc.perform(
                    put("/category/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            );

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(response.name()))
                    .andExpect(jsonPath("$.type").value(response.type().toString()));
        }


        @Test
        @DisplayName("Deve retornar 404 quando não encontrar a categoria")
        void shouldReturn404WhenCategoryDoesNotExist() throws Exception{

            CategoryRequestDTO request = new CategoryRequestDTO(
                    "Aluguel",
                    CategoryType.EXPENSE
            );

            String json = objectMapper.writeValueAsString(request);

            when(service.updateById(request,1L))
                    .thenThrow(new EntityNotFoundException("Categoria não encontrada!"));

            mvc.perform(
                            put("/category/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                            .andExpect(status().isNotFound()
            );

            verify(service, times(1)).updateById(request,1L);

        }

        @Test
        @DisplayName("Deve retornar 403 quando não tiver permissão para acessar a categoria")
        void shouldReturn403WhenCategoryIsUnauthorized() throws Exception {

            CategoryRequestDTO request = new CategoryRequestDTO(
                    "Aluguel",
                    CategoryType.EXPENSE
            );

            String json = objectMapper.writeValueAsString(request);


            when(service.updateById(request, 1L))
                    .thenThrow(new UnauthorizedResourceAccessException("Acesso negado para atualização dessa categoria"));


            mvc.perform(
                    put("/category/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isForbidden());

            verify(service, times(1)).updateById(request, 1L);

        }



    }

}
