package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.IncomeRequestDTO;
import com.jeffssousa.fluxo.dto.IncomeResponseDTO;
import com.jeffssousa.fluxo.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("income")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<Void> addIncome(@RequestBody IncomeRequestDTO dto){
        incomeService.addIncome(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponseDTO>> getAll(){
        return ResponseEntity.ok(incomeService.getAll());
    }

}
