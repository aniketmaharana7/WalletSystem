package com.wallet.app.ledger.dto;

import com.wallet.app.ledger.entity.LedgerEntryType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LedgerEntryResponse(
        UUID ledgerEntryId,
        UUID transactionId,
        UUID walletId,
        LedgerEntryType type,
        BigDecimal amount,
        String currency,
        Instant createdAt
) {
}