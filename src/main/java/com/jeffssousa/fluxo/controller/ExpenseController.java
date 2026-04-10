package com.jeffssousa.fluxo.controller;


import com.jeffssousa.fluxo.dto.expense.ExpenseRequestDTO;
import com.jeffssousa.fluxo.dto.expense.ExpenseResponseDTO;
import com.jeffssousa.fluxo.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Void> addExpense(@Valid @RequestBody ExpenseRequestDTO dto){
        expenseService.addExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getAll(){
        return ResponseEntity.ok(expenseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable UUID id){
        ExpenseResponseDTO dto = expenseService.getById(id);
        return ResponseEntity.ok(dto);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        expenseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateById(@PathVariable UUID id, @RequestBody ExpenseRequestDTO dto){
        return ResponseEntity.ok(expenseService.updateById(id,dto));
    }

    @GetMapping("upcoming-expenses")
    public ResponseEntity<List<ExpenseResponseDTO>> getUpcoming15Expenses(){
        List<ExpenseResponseDTO> response = expenseService.getUpcoming15Expenses();
        return ResponseEntity.ok(response);
    }

}
