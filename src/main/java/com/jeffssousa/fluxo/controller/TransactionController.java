package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.TransactionDTO;
import com.jeffssousa.fluxo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
