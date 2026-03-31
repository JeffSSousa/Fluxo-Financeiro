package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.CategoryRequestDTO;
import com.jeffssousa.fluxo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
