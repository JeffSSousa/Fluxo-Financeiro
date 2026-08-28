package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.income.IncomeRequestDTO;
import com.jeffssousa.fluxo.dto.income.IncomeResponseDTO;
import com.jeffssousa.fluxo.service.IncomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Receita", description = "Endpoints para gerenciamento de Receita.")
@RestController
@RequestMapping("income")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @Operation(
            summary = "Cria receita",
            description = "Recebe um DTO com dados e cria uma nova receita."
    )
    @PostMapping
    public ResponseEntity<Void> addIncome(@Valid @RequestBody IncomeRequestDTO dto){
        incomeService.addIncome(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Busca todas as receitas",
            description = "Retorna todas as receitas do usuario."
    )
    @GetMapping
    public ResponseEntity<List<IncomeResponseDTO>> getAll(){
        return ResponseEntity.ok(incomeService.getAll());
    }

    @Operation(
            summary = "Busca receita pelo ID.",
            description = "Recebe um ID e retorna uma receita pelo ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponseDTO> getIncomeById(@PathVariable UUID id){
        IncomeResponseDTO dto = incomeService.getById(id);
        return ResponseEntity.ok(dto);

    }

    @Operation(
            summary = "Deleta receita pelo ID",
            description = "Recebe um ID e deleta uma receita pelo ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        incomeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualiza receita pelo ID",
            description = "Recebe um ID e DTO, retorna a receita atualizada."
    )
    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponseDTO> updateById(@Valid @PathVariable UUID id, @RequestBody IncomeRequestDTO dto){
        return ResponseEntity.ok(incomeService.updateById(id,dto));

    }

}
