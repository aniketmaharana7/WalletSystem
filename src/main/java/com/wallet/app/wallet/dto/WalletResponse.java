package com.wallet.app.wallet.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(
        UUID walletId,
        BigDecimal balance,
        String currency,
        String status
) {
}