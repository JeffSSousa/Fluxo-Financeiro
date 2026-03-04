package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.AccountSummaryDTO;
import com.jeffssousa.fluxo.service.AccountSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountSummaryController {

    private final AccountSummaryService service;

    @GetMapping("/summary")
    public ResponseEntity<AccountSummaryDTO> getSummary(){
        return ResponseEntity.ok(service.getSummary());
    }

}
