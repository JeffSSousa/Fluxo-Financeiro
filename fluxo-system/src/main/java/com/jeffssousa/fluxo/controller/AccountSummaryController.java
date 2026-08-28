package com.jeffssousa.fluxo.controller;

import com.jeffssousa.fluxo.dto.summary.AccountSummaryDTO;
import com.jeffssousa.fluxo.dto.summary.YearlySummaryDTO;
import com.jeffssousa.fluxo.service.AccountSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Resumo da Conta", description = "Endpoints para resumos e dashboards")
@RestController
@RequestMapping("summary")
@RequiredArgsConstructor
public class AccountSummaryController {

    private final AccountSummaryService service;

    @Operation(
            summary = "Busca resumo financeiro.",
            description = "Retornar o valor total das despesas, receitas e a diferença."
    )
    @GetMapping("/resume")
    public ResponseEntity<AccountSummaryDTO> getSummary(){
        return ResponseEntity.ok(service.getSummary());
    }

    @Operation(
            summary = "Busca resumo mes a mes.",
            description = "Retorna o resumo mensal do ano selecionado."
    )
    @GetMapping("/yearly")
    public ResponseEntity<YearlySummaryDTO> getYearlySummary(@RequestParam(required = false) Integer year){
        return ResponseEntity.ok(service.getYearlyFinancialSummary(year));
    }

}
