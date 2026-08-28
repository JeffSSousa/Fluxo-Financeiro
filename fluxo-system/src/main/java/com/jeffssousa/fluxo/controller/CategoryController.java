package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.category.CategoryRequestDTO;
import com.jeffssousa.fluxo.dto.category.CategoryResponseDTO;
import com.jeffssousa.fluxo.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categoria", description = "Endpoints para gerenciamento de categorias.")
@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @Operation(
            summary = "Cria categoria",
            description = "Recebe um DTO com dados e cria uma nova categoria."
    )
    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryRequestDTO dto){
        service.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Busca todas as categorias",
            description = "Retorna todas as categorias do usuario."
    )
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(
            summary = "Busca categoria pelo ID.",
            description = "Recebe um ID e retorna uma categoria pelo ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(
            summary = "Deleta categoria pelo ID",
            description = "Recebe um ID e deleta uma categoria pelo ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualiza categoria pelo ID",
            description = "Recebe um ID e DTO, retorna a categoria atualizada."
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateById(@Valid @RequestBody CategoryRequestDTO dto, @PathVariable Long id){
        return ResponseEntity.ok(service.updateById(dto,id));
    }


}
