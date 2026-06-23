package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.transaction.TransactionDTO;
import com.jeffssousa.fluxo.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Transações", description = "Endpoints para consultas unificadas de receitas e despesas.")
@RestController
@RequestMapping("transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getTransactions(){

        List<TransactionDTO> list = service.findAll();
        return ResponseEntity.ok(list);

    }


}
