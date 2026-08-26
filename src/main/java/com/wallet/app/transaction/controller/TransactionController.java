package com.wallet.app.transaction.controller;

import com.wallet.app.transaction.dto.TransactionListResponse;
import com.wallet.app.transaction.dto.TransactionResponse;
import com.wallet.app.transaction.entity.TransactionStatus;
import com.wallet.app.transaction.entity.TransactionType;
import com.wallet.app.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/recent")
    public TransactionListResponse getRecentTransactions() {
        return transactionService.getRecentTransactions();
    }

    @GetMapping("/month")
    public TransactionListResponse getCurrentMonthTransactions() {
        return transactionService.getCurrentMonthTransactions();
    }

    @GetMapping
    public List<TransactionResponse> getTransactions(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) TransactionStatus status
    ) {
        return transactionService.getTransactions(
                from,
                to,
                type,
                status
        );
    }
}
