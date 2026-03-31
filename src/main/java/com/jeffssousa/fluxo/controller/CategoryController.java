package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.CategoryRequestDTO;
import com.jeffssousa.fluxo.dto.CategoryResponseDTO;
import com.jeffssousa.fluxo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody CategoryRequestDTO dto){
        service.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }



}
