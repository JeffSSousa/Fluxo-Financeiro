package com.jeffssousa.fluxo.controller;


import com.jeffssousa.fluxo.dto.ExpenseRequestDTO;
import com.jeffssousa.fluxo.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Void> addExpense(@RequestBody ExpenseRequestDTO dto){
        expenseService.addExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
