package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.AccountSummaryDTO;
import com.jeffssousa.fluxo.dto.YearlySummaryDTO;
import com.jeffssousa.fluxo.service.AccountSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("summary")
@RequiredArgsConstructor
public class AccountSummaryController {

    private final AccountSummaryService service;

    @GetMapping("/resume")
    public ResponseEntity<AccountSummaryDTO> getSummary(){
        return ResponseEntity.ok(service.getSummary());
    }

    @GetMapping("/yearly")
    public ResponseEntity<YearlySummaryDTO> getYearlySummary(@RequestParam(required = false) Integer year){
        return ResponseEntity.ok(service.getYearlyFinancialSummary(year));
    }

}
