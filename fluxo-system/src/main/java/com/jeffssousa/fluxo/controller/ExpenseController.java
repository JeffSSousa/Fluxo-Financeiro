package com.jeffssousa.fluxo.controller;


import com.jeffssousa.fluxo.dto.expense.ExpenseRequestDTO;
import com.jeffssousa.fluxo.dto.expense.ExpenseResponseDTO;
import com.jeffssousa.fluxo.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Despesa", description = "Endpoints para gerenciamento de Despesa.")
@RestController
@RequestMapping("expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(
            summary = "Cria despesa",
            description = "Recebe um DTO com dados e cria uma nova despesa."
    )
    @PostMapping
    public ResponseEntity<Void> addExpense(@Valid @RequestBody ExpenseRequestDTO dto){
        expenseService.addExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Busca todas as despesas",
            description = "Retorna todas as despesas do usuario."
    )
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getAll(){
        return ResponseEntity.ok(expenseService.getAll());
    }

    @Operation(
            summary = "Busca despesa pelo ID.",
            description = "Recebe um ID e retorna uma despesa pelo ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable UUID id){
        ExpenseResponseDTO dto = expenseService.getById(id);
        return ResponseEntity.ok(dto);

    }

    @Operation(
            summary = "Deleta despesa pelo ID",
            description = "Recebe um ID e deleta uma despesa pelo ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        expenseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualiza desesa pelo ID",
            description = "Recebe um ID e DTO, retorna a despesa atualizada."
    )
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateById(@Valid @PathVariable UUID id, @RequestBody ExpenseRequestDTO dto){
        return ResponseEntity.ok(expenseService.updateById(id,dto));
    }

    @Operation(
            summary = "Buscar proximas 15",
            description = "Retorna 15 despesas proximas do vencimento."
    )
    @GetMapping("upcoming-expenses")
    public ResponseEntity<List<ExpenseResponseDTO>> getUpcoming15Expenses(){
        List<ExpenseResponseDTO> response = expenseService.getUpcoming15Expenses();
        return ResponseEntity.ok(response);
    }

}
