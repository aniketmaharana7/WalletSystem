package com.wallet.app.wallet.controller;

import com.wallet.app.common.utility.SecurityContextUtil;
import com.wallet.app.wallet.dto.DepositRequest;
import com.wallet.app.wallet.dto.DepositResponse;
import com.wallet.app.wallet.dto.WalletResponse;
import com.wallet.app.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
@SecurityRequirement(name = "bearerAuth")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public ResponseEntity<WalletResponse> findByUserId() {
        UUID userId = SecurityContextUtil.getCurrentUserId();
        WalletResponse response = walletService.getWalletDetails(userId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit(@RequestBody DepositRequest depositRequest) {
        // to wallet deposit
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(walletService.toWalletDeposit(depositRequest));
    }

}