package com.wallet.app.wallet.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositResponse(
        UUID transactionId,
        BigDecimal amount,
        BigDecimal balance,
        String currency,
        String type,
        String status
) {}