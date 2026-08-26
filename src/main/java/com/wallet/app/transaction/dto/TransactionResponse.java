package com.wallet.app.transaction.dto;

import com.wallet.app.transaction.entity.TransactionStatus;
import com.wallet.app.transaction.entity.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
        UUID transactionId,
        TransactionType type,
        BigDecimal amount,
        String currency,
        TransactionStatus status,
        UUID senderWalletId,
        UUID receiverWalletId,
        Instant createdAt,
        Instant completedAt
) {
}